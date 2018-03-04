package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.fastadapter.utils.DefaultIdDistributor;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;
import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;

public class AccountActivity extends BaseActivity {
    Toolbar toolbar;
    private TextView nome, email;
    private Button logout;
    private Uri urlfoto;
    private ImageView immagineprofilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        buildDrawer(toolbar);
        toolbar.setTitle(R.string.account_toolbar);
        nome = (TextView)findViewById(R.id.textViewNome);
        email = (TextView)findViewById(R.id.textViewEmail);
        immagineprofilo = (ImageView)findViewById(R.id.imageAccount);
        if(FirebaseUtilities.getIstance().isLogged()){
            nome.setText(FirebaseUtilities.getIstance().getNome());
            email.setText(FirebaseUtilities.getIstance().getEmail());
            urlfoto = FirebaseUtilities.getIstance().getFotoProfilo();
            immagineprofilo.setImageURI(urlfoto);
        }
        logout = (Button)findViewById(R.id.buttonLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtilities.getIstance().logOut();
                User.getIstance().userLogOut();
                Toast.makeText(getApplicationContext(),"Sei uscito con successo",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                i.putExtra("Activity","Account");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,MapsActivity.class);
        i.putExtra("Activity","Account");
        startActivity(i);
    }
}
