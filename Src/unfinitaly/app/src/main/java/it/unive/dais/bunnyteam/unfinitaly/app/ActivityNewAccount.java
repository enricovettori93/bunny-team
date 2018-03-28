package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

public class ActivityNewAccount extends AppCompatActivity {
    private Button registrati, cancel;
    private EditText nome,email,psw,repeatpsw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        //Becco le view
        registrati = (Button)findViewById(R.id.buttonNuovoRegistrati);
        cancel = (Button)findViewById(R.id.buttonNuovoCancella);
        nome = (EditText) findViewById(R.id.editTextNuovoNome);
        email = (EditText)findViewById(R.id.editTextNuovoEmail);
        psw = (EditText)findViewById(R.id.editTextNuovoPsw);
        repeatpsw = (EditText)findViewById(R.id.editTextNuovoRipetiPsw);
        //Associo i listener ai bottoni
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !psw.getText().toString().isEmpty() && !repeatpsw.getText().toString().isEmpty()){
                    if(!psw.getText().toString().equals(repeatpsw.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Le password sono diverse",Toast.LENGTH_SHORT).show();
                    else
                        if(psw.getText().toString().length() < 6)
                            Toast.makeText(getApplicationContext(),"Password corta, almeno 6 caratteri",Toast.LENGTH_SHORT).show();
                        else
                            register();
                }
                else
                    alertMissingData();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void alertMissingData(){
        Toast.makeText(getApplicationContext(),"Dati mancanti",Toast.LENGTH_SHORT).show();
    }

    private void cancel(){
        Toast.makeText(getApplicationContext(),"Operazione annullata",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,LoginActivity.class);
        i.putExtra("Action","newAccount");
        startActivity(i);
    }

    private void register(){
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email.getText().toString(),psw.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("REGISTRAZIONE","RIUSCITA " + task.getResult().toString());
                    if(!nome.getText().toString().isEmpty())
                        insertName();
                    else
                        continueAfterRegistration();
                }
                else{
                    try{
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(getApplicationContext(),"Password debole",Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getApplicationContext(),"Email già usata",Toast.LENGTH_SHORT).show();
                    } catch(Exception e) {
                        Log.d("REGISTRAZIONE","FALLITA " + task.getResult().toString());
                    }
                    Toast.makeText(getApplicationContext(),"Errore durante la registrazione",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome.getText().toString())
                .build();
        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("NEWACCOUNT","OK NOME");
                    continueAfterRegistration();
                }
                else{
                    Log.d("NEWACCOUNT","ERRORE NOME");
                }
            }
        });
    }

    private void continueAfterRegistration(){
        Toast.makeText(getApplicationContext(),"Registrazione completata.",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(),AccountActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        cancel();
    }
}
