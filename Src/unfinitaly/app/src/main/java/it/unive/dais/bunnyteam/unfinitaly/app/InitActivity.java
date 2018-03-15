package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.testing.TestFirebase;

public class InitActivity extends AppCompatActivity {
    AVLoadingIndicatorView loading;
    TextView error;
    boolean status = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi2);
        error = (TextView)findViewById(R.id.textError);
        if(!isOnline()){
            Log.d("ONLINE","FALSE");
            status = false;
            resumeLoadingAfterFirebase();
        }
        else{
            error.setText("Caricamento dati in corso..");
            status = FirebaseUtilities.getIstance().readFromFirebase(InitActivity.this);
        }
    }

    public void resumeLoadingAfterFirebase(){
        if(status){
            Intent i = new Intent(this,LoadingActivity.class);
            startActivity(i);
        }
        else{
            loading.hide();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
