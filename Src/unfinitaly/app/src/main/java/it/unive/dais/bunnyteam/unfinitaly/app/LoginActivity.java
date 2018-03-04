package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;

public class LoginActivity extends AppCompatActivity {
    private TextView skip;
    private com.google.android.gms.common.SignInButton login;
    public static final String TAG = "LoginActivity";
    public static final int RequestSignInCode = 0;
    public FirebaseAuth firebaseAuth;
    public FirebaseAuth.AuthStateListener firebaseAuthListener;
    public GoogleApiClient googleApiClient;
    private boolean onBackPressed = false;
    private Intent i;
    private String intentcontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        skip = (TextView)findViewById(R.id.textViewSkip);
        login = (com.google.android.gms.common.SignInButton)findViewById(R.id.buttonLogin);
        i = getIntent();
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

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInFunction();
            }
        });
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    startMapsActivity();
                }
                /*else{
                    Toast.makeText(getApplicationContext(),"User disconnected",Toast.LENGTH_SHORT).show();
                }*/
            }
        };
    }

    public void SignInFunction(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent,RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("RESULT ACTIVITY","MESSAGGIO E DATI DI RITORNO, REQUEST CODE: " + requestCode + " RESULT CODE: "+ resultCode);
        if(requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(googleSignInResult.isSuccess()){
                Log.d("LOGIN","SUCCESSO");
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
            else
                Log.d("LOGIN","INSUCCESSO");
        }
    }
    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        Log.d("Login with google","Dovrei entrare qui, idToken:"+googleSignInAccount.getIdToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            User.getIstance().setName(firebaseUser.getDisplayName().toString());
                            Log.d("NOME FIREBASE",firebaseUser.getDisplayName().toString());
                            User.getIstance().setEmail(firebaseUser.getEmail().toString());
                            Log.d("EMAIL FIREBASE",firebaseUser.getEmail().toString());
                            Toast.makeText(getApplicationContext(),"Accesso effettuato con successo.",Toast.LENGTH_SHORT).show();
                            if(intentcontent.equals("Account")){
                                Intent i = new Intent(getApplicationContext(),AccountActivity.class);
                                startActivity(i);
                            }
                            else
                                startMapsActivity();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Errore durante il login.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public void startMapsActivity(){
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
        if(intentcontent.equals("Loading")){
            //Sono stato chiamato da loading activity
            if(onBackPressed){
                /*è stato premuto una volta. Lo ripremiamo, quindi dovremmo uscire*/
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
}
