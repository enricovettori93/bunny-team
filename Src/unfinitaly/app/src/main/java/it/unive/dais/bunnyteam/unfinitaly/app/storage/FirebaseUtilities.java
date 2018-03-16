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

import it.unive.dais.bunnyteam.unfinitaly.app.InitActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.LoadingActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.entities.User;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.marker.OperaFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.testing.TestFirebase;

/**
 * Created by Enrico on 03/03/2018.
 */

public class FirebaseUtilities {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private boolean ritorno = true;
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
        User.getIstance().userLogOut();
    }

    public Uri getFotoProfilo(){
        if(isLogged())
            return user.getPhotoUrl();
        else
            return null;
    }

    public String getIdUtente(){
        if(isLogged())
            return user.getUid();
        else
            return null;
    }

    public boolean readFromFirebase(final InitActivity act){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long timebefore = System.nanoTime();
                Log.d("NUMERO DATI LETTI",""+dataSnapshot.getChildrenCount());
                int i = 0;
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    OperaFirebase app = data.getValue(OperaFirebase.class);
                    app.setId_firebase(Integer.toString(i));
                    ListaOpereFirebase.getIstance().getListaOpere().add(app);
                    i++;
                    act.updateProgressBar((int)(100*dataSnapshot.getChildrenCount())/i);
                }
                Log.d("OPERA 1",ListaOpereFirebase.getIstance().getListaOpere().get(0).toString());
                long timeafter = System.nanoTime();
                Log.d("FINITO","FROM FIREBASE IN MS: " + (int) ((timeafter - timebefore) / 1000000));
                Log.d("SIZE ARRAYLIST",""+ ListaOpereFirebase.getIstance().getListaOpere().size());
                act.resumeLoadingAfterFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Reading DB from Firebase");
                ritorno = false;
            }
        });
        return ritorno;
    }
}
