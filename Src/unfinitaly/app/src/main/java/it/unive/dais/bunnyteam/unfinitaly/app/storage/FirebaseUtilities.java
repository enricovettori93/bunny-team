package it.unive.dais.bunnyteam.unfinitaly.app.storage;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Enrico on 03/03/2018.
 */

public class FirebaseUtilities {
    private static FirebaseAuth auth;
    private static FirebaseUser user;
    private static FirebaseUtilities fbutilites = new FirebaseUtilities();
    private FirebaseUtilities(){
        auth = FirebaseAuth.getInstance();
    }

    public static FirebaseUtilities getIstance(){
        return fbutilites;
    }

    private static boolean isLogged(){
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

    public static String getEmail(){
        if (isLogged()){
            return user.getEmail().toString();
        }
        else
            return "";
    }

    public static String getNome(){
        if (isLogged()){
            return user.getDisplayName().toString();
        }
        else
            return "";
    }
}
