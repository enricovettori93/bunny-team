package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

public class AccountActivity extends BaseActivity {
    Toolbar toolbar;
    private TextView nome, email;
    private Button logout, reset_psw;
    private Uri urlfoto;
    private CircleImageView immagineprofilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        buildDrawer(toolbar);
        toolbar.setTitle(R.string.account_toolbar);
        nome = (TextView)findViewById(R.id.textViewNome);
        email = (TextView)findViewById(R.id.textViewEmail);
        immagineprofilo = (CircleImageView) findViewById(R.id.imageAccount);
        if(FirebaseUtilities.getIstance().isLogged()){
            nome.setText(FirebaseUtilities.getIstance().getNome());
            email.setText(FirebaseUtilities.getIstance().getEmail());
            urlfoto = FirebaseUtilities.getIstance().getFotoProfilo();
            if(urlfoto != null){
                //Foto presente
                Glide
                        .with(getApplicationContext())
                        .load(urlfoto)
                        .centerCrop()
                        .into(immagineprofilo);
            }
            else{
                //Foto non presente
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.ic_account_circle_black_24dp)
                        .centerCrop()
                        .into(immagineprofilo);
            }
        }
        logout = (Button)findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                i.putExtra("Activity","Account");
                i.putExtra("Action","Logout");
                startActivity(i);
            }
        });
        Log.d("AA",""+FirebaseAuth.getInstance().getCurrentUser().getProviderId().toString());
        //if(FirebaseAuth.getInstance().getCurrentUser().getProviderId());
        reset_psw = (Button)findViewById(R.id.buttonResetPsw);
        reset_psw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseUtilities.getIstance().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Email di reset inviata.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,MapsActivity.class);
        //i.putExtra("Activity","Account");
        startActivity(i);
    }
}
