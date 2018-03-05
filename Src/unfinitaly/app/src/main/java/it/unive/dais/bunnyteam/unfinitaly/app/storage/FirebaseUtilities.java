package it.unive.dais.bunnyteam.unfinitaly.app.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unive.dais.bunnyteam.unfinitaly.app.R;

/**
 * Created by Enrico on 03/03/2018.
 */

public class FirebaseUtilities {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private static FirebaseUtilities fbutilites = new FirebaseUtilities();
    private FirebaseUtilities(){
        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseUtilities getIstance(){
        return fbutilites;
    }

    public boolean isLogged(){
        if(auth !=  null){
            user = auth.getCurrentUser();
            if(user != null)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public String getEmail(){
        if (isLogged()){
            return user.getEmail().toString();
        }
        else
            return "";
    }

    public String getNome(){
        if (isLogged()){
            return user.getDisplayName().toString();
        }
        else
            return "";
    }

    public void logOut(){
        auth.signOut();
    }

    public Uri getFotoProfilo(){
        if(isLogged())
            return user.getPhotoUrl();
        else
            return null;
    }
}
