package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    private AVLoadingIndicatorView loading;
    private TextView message;
    private ProgressBar progress_loading;
    private boolean status = true;
    private BroadcastReceiver networkStateReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        loading = (AVLoadingIndicatorView)findViewById(R.id.avi2);
        message = (TextView)findViewById(R.id.textMessageLoading);
        progress_loading = (ProgressBar)findViewById(R.id.progressLoading);
        progress_loading.setMax(100);
        progress_loading.setProgressTintList(ColorStateList.valueOf(Color.WHITE));
        if(!isOnline()){
            message.setText(R.string.loading_bad);
            status = false;
            loading.hide();
            progress_loading.setVisibility(View.INVISIBLE);
            networkStateReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo ni = manager.getActiveNetworkInfo();
                    if(ni != null) {
                        Log.d("NETWORK", String.format("%s", ni.getState()));
                        if(ni.getState() == NetworkInfo.State.CONNECTED)
                            beginLoadingFromFirebase();
                    }
                }
            };
        }
        else{
            beginLoadingFromFirebase();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        if(networkStateReceiver != null)
            unregisterReceiver(networkStateReceiver);
        super.onPause();
    }

    public void updateProgressBar(int percentage){
        progress_loading.setProgress(percentage);
    }

    private void beginLoadingFromFirebase(){
        loading.show();
        progress_loading.setVisibility(View.VISIBLE);
        message.setText(R.string.loading_ok);
        status = FirebaseUtilities.getIstance().readFromFirebase(InitActivity.this);
    }

    public void resumeLoadingAfterFirebase(){
        if(status){
            Intent i = new Intent(this,LoadingActivity.class);
            startActivity(i);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
