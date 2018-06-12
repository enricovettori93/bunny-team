package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

public class ActivityNewAccount extends AppCompatActivity {
    private Button registrati, cancel;
    private EditText nome,email,psw,repeatpsw;
    private TextView textcheck;
    private CheckBox checkBox;
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
        checkBox = (CheckBox) findViewById(R.id.checkBoxRegister);
        textcheck = (TextView) findViewById(R.id.textView25);
        textcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://unfinitaly.altervista.org/#privacy"));
                startActivity(i);
            }
        });
        //Associo i listener ai bottoni
        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nome.getText().toString().trim().isEmpty() && !email.getText().toString().trim().isEmpty() && !psw.getText().toString().trim().isEmpty() && !repeatpsw.getText().toString().trim().isEmpty()){
                    if(!psw.getText().toString().trim().equals(repeatpsw.getText().toString().trim()))
                        Toast.makeText(getApplicationContext(),"Le password sono diverse",Toast.LENGTH_SHORT).show();
                    else
                        if(psw.getText().toString().trim().length() < 6)
                            Toast.makeText(getApplicationContext(),"Password corta, almeno 6 caratteri",Toast.LENGTH_SHORT).show();
                    else
                        if(!isEmailValid(email.getText().toString().trim()))
                            Toast.makeText(getApplicationContext(),"Formato email non valido",Toast.LENGTH_SHORT).show();
                        else
                            if(checkBox.isChecked())
                                register();
                            else
                                Toast.makeText(getApplicationContext(),"La casella di consenso non è stata spuntata",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"Dati mancanti",Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    /**
     * Controlla se il parametro corrisponde a una email scritta in modo corretto
     * @param email
     * @return
     */
    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void cancel(){
        Toast.makeText(getApplicationContext(),"Operazione annullata",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this,LoginActivity.class);
        i.putExtra("Action","newAccount");
        startActivity(i);
    }

    /**
     * Registra l'account su firebase e gestisce eventuali eccezioni
     */
    private void register(){
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email.getText().toString().trim(),psw.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                        Toast.makeText(getApplicationContext(),"Password troppo corta",Toast.LENGTH_SHORT).show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getApplicationContext(), "Email già usata", Toast.LENGTH_SHORT).show();
                    }catch(FirebaseAuthEmailException e){
                        Toast.makeText(getApplicationContext(),"Email malformata",Toast.LENGTH_SHORT).show();
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
                .setDisplayName(nome.getText().toString().trim())
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
