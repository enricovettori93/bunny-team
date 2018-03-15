package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import it.unive.dais.bunnyteam.unfinitaly.app.storage.FirebaseUtilities;
import it.unive.dais.bunnyteam.unfinitaly.app.testing.TestFirebase;

public class InitActivity extends AppCompatActivity {
    AVLoadingIndicatorView loading;
    TextView error;
    ProgressBar progress_loading;
    boolean status = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi2);
        error = (TextView)findViewById(R.id.textError);
        progress_loading = (ProgressBar)findViewById(R.id.progressLoading);
        progress_loading.setMax(100);
        progress_loading.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
        if(!isOnline()){
            status = false;
            resumeLoadingAfterFirebase();
        }
        else{
            error.setText("Caricamento dati in corso...");
            status = FirebaseUtilities.getIstance().readFromFirebase(InitActivity.this);
        }
    }

    public void updateProgressBar(int percentage){
        progress_loading.setProgress(percentage);
    }

    public void resumeLoadingAfterFirebase(){
        if(status){
            Intent i = new Intent(this,LoadingActivity.class);
            startActivity(i);
        }
        else{
            loading.hide();
            progress_loading.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
