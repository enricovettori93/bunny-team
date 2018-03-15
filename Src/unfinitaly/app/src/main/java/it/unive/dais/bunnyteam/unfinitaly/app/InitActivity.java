package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.testing.TestFirebase;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        FirebaseUtilities.getIstance().readFromFirebase(InitActivity.this);
    }

    public void resumeLoadingAfterFirebase(){
        Intent i = new Intent(this,LoadingActivity.class);
        startActivity(i);
    }
}
