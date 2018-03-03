package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;

public class AccountActivity extends BaseActivity {
    Toolbar toolbar;
    TextView nome, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        buildDrawer(toolbar);
        toolbar.setTitle(R.string.account_toolbar);
        nome = (TextView)findViewById(R.id.textViewNome);
        email = (TextView)findViewById(R.id.textViewEmail);
        nome.setText(User.getIstance().getName().toString());
        email.setText(User.getIstance().getEmail().toString());
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,MapsActivity.class);
        startActivity(i);
    }
}
