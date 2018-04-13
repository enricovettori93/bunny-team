package it.unive.dais.bunnyteam.unfinitaly.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.coreutils.BuildConfig;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;


/**
 * Activity per la schermata di crediti e about.
 *
 * @author BunnyTeam, Università Ca' Foscari
 */
public class InfoActivity extends BaseActivity {
    private WebView webview;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ImageView imV;
    private TextView mail, site, credits;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        webview = (WebView)findViewById(R.id.infowebview);
        webview.loadUrl("http://unfinitaly.altervista.org/");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        mail = (TextView)findViewById(R.id.textViewmail);
        site = (TextView)findViewById(R.id.textViewsite);
        credits = (TextView)findViewById(R.id.textView9);
        try {
            credits.setText("Per visualizzare gli altri sviluppatori prima della versione 2, consultare il sito web.\n"+getResources().getString(R.string.creditsdatadroid)+getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            credits.setText(getResources().getString(R.string.creditsdatadroiderror));
            Log.e("Version","Error injecting versione");
        };
        mail.setText(R.string.email);
        site.setText(R.string.website);
        imV = (ImageView) findViewById(R.id.bunnyLogo);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebView();
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        buildDrawer(toolbar);
        toolbar.setTitle("Informazioni");

        drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer.setOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
            @Override
            public boolean onNavigationClickListener(View clickedView) {
                onBackPressed();
                return true;
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:unfinitaly.app@gmail.com"));
                i.putExtra(android.content.Intent.EXTRA_SUBJECT, "[UNFINITALY] info");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(i, "Manda una Email"));
            }
        });
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWebView();
            }
        });
    }

    private void showWebView() {
        webview.setVisibility(View.VISIBLE);
        imV.setVisibility(View.INVISIBLE);
        mail.setVisibility(View.INVISIBLE);
        site.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);

    }

    private void unshowWebView() {
        webview.setVisibility(View.INVISIBLE);
        imV.setVisibility(View.VISIBLE);
        mail.setVisibility(View.VISIBLE);
        site.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (webview.getVisibility() == View.VISIBLE)
            unshowWebView();
        else
            super.onBackPressed();
    }
}
