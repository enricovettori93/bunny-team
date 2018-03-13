package it.unive.dais.bunnyteam.unfinitaly.app.storage;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import it.unive.dais.bunnyteam.unfinitaly.app.LoadingActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.OperaFirebase;

/**
 * Created by Enrico on 03/03/2018.
 */

public class FirebaseUtilities {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
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
        User.getIstance().userLogOut();
    }

    public Uri getFotoProfilo(){
        if(isLogged())
            return user.getPhotoUrl();
        else
            return null;
    }

    public void readFromFirebase(final LoadingActivity act){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("NUMERO DATI LETTI",""+dataSnapshot.getChildrenCount());
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    ListaOpereFirebase.getIstance().getListaOpere().add(data.getValue(OperaFirebase.class));
                }
                Log.d("FINITO","FROM FIREBASE");
                Log.d("SIZE ARRAYLIST",""+ ListaOpereFirebase.getIstance().getListaOpere().size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Reading DB from Firebase");
            }
        });
        act.resumeLoadingAfterFirebase();
    }
}
