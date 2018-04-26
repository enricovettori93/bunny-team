package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{
    private TextView skip;
    private com.google.android.gms.common.SignInButton login;
    public static final String TAG = "LoginActivity";
    public static final int RequestSignInCode = 0;
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;
    public GoogleApiClient googleApiClient;
    private Button newAccount, loginNoGoogle, resetPsw;
    private boolean onBackPressed = false;
    private Intent i;
    private String intentcontent;
    private String action;
    private EditText editTextEmail, editTextPassword;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        skip = (TextView)findViewById(R.id.textViewSkip);
        login = (com.google.android.gms.common.SignInButton)findViewById(R.id.buttonLogin);
        loginNoGoogle = (Button)findViewById(R.id.buttonAccedi);
        editTextEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextLoginPsw);
        i = getIntent();
        newAccount = (Button)findViewById(R.id.buttonNewAccount);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityNewAccount.class);
                startActivity(i);
            }
        });

        intentcontent = i.getStringExtra("Activity");
        //Creazione e configurazione Sign In con Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Configurazione Google Api Client
        googleApiClient =  new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Errore durante il login",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        googleApiClient.registerConnectionCallbacks(this);
        firebaseAuth = FirebaseAuth.getInstance();

        //Listener login e skip
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInFunction();
            }
        });

        //Aggiungo il listener al bottone per loggarsi senza google
        loginNoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInNoGoogle();
            }
        });

        //Controllo se è arrivato l'intent di uscire dalla account activity
        action = i.getStringExtra("Action");
        Log.d(TAG,"AZIONE INTENT: "+action);
        if(action != null) {
            if (googleApiClient.isConnected()) {
                signOutFunction();
            }
            else{
                googleApiClient.connect();
            }
        }

        //Creo il listener per il pulsante per il reset della password
        resetPsw = (Button)findViewById(R.id.buttonLoginReset);
        resetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextEmail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Campo email vuoto.",Toast.LENGTH_SHORT).show();
                    editTextEmail.setFocusable(true);
                }
                else
                    if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches())
                        Toast.makeText(getApplicationContext(),"Indirizzo email non valido.",Toast.LENGTH_SHORT).show();
                    else
                        resettaPassword();
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //startMapsActivity();
                }
            }
        };

        //Creo il dialog per il caricamento
        dialog = new AlertDialog.Builder(this).setMessage("Accesso in corso...").setCancelable(false).create();
    }

    @Override
    protected void onStart(){
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * Funzione per resettare la password
     */
    private void resettaPassword(){
        FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Invio email riuscito, controlla la casella per resettare la password",Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN","INVIO EMAIL RESET RIUSCITO");
                }
                 else{
                    Toast.makeText(getApplicationContext(),"Invio email non riuscito",Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN","INVIO EMAIL RESET FALLITO");
                }
            }
        });
    }

    /**
     * Logga l'utente con email e password
     */
    public void signInNoGoogle(){
        String email, password;
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        if(password.length() >= 6 && !password.isEmpty() && !email.isEmpty()){
            showDialogLoading(true);
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    showDialogLoading(false);
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),R.string.login_success,Toast.LENGTH_LONG).show();
                        continueAfterLogin();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),R.string.login_failed,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            if(password.isEmpty() || email.isEmpty())
                Toast.makeText(getApplicationContext(),"Inserire i campi richiesti",Toast.LENGTH_SHORT).show();
            else
                if(password.length() < 6)
                    Toast.makeText(getApplicationContext(),"Password troppo corta",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Logga l'utente dall'app chiedendo un result all'autenticazione di Google
     */
    public void signInFunction(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent,RequestSignInCode);
    }

    /**
     * Slogga l'utente dall'app
     */
    public void signOutFunction(){
        Log.d(TAG, "LOGOUT");
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient);
        //Toast.makeText(getApplicationContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        showDialogLoading(true);
        editTextEmail.clearFocus();
        editTextPassword.clearFocus();
        Log.d(TAG, "REQUEST CODE: " + requestCode + " RESULT CODE: "+ resultCode);
        if(requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                Log.d(TAG, String.format("login success. Status message: %s", googleSignInResult.getStatus().getStatusCode()));
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
            else{
                showDialogLoading(false);
                Log.d(TAG, String.format("login failed. Status message: %s", googleSignInResult.getStatus().getStatusCode()));
                Toast.makeText(getApplicationContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        }
        else{
            showDialogLoading(false);
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        Log.d(TAG,"Login con Google, dovrei entrare qui, idToken:"+googleSignInAccount.getIdToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),R.string.login_success,Toast.LENGTH_SHORT).show();
                            continueAfterLogin();
                        }
                        else{
                            showDialogLoading(false);
                            Toast.makeText(getApplicationContext(),"Errore durante il login",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDialogLoading(boolean status){
        if(status){
            dialog.show();
        }
        else{
            dialog.dismiss();
        }
    }

    private void continueAfterLogin(){
        showDialogLoading(false);
        if(intentcontent != null && intentcontent.equals("Base")){
            Intent i = new Intent(getApplicationContext(),AccountActivity.class);
            startActivity(i);
        }
        else
            startMapsActivity();
    }

    private void startMapsActivity(){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }

    @Override
    public void onResume(){
        super.onResume();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    public void onBackPressed(){
        Log.d(TAG,"ACT BEFORE: "+intentcontent);
        if(intentcontent != null && intentcontent.equals("Loading")){
            Log.d(TAG,"LOADING ACTIVITY");
            //Sono stato chiamato da loading activity
            if(onBackPressed){
                Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
            else{
                onBackPressed=true;
                Toast.makeText(this, R.string.maps_onmapbackpress, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed=false;
                    }
                }, 2000);
            }
        }
        else{
            //Sono stato chiamato da altre activity
            Intent i = new Intent(this,MapsActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG,"CONNESSO");
        if(action != null){
            Log.d(TAG,"SLOGGO");
            signOutFunction();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Nothing
    }
}
