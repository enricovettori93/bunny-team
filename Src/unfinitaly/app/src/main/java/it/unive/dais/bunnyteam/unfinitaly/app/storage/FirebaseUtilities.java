package it.unive.dais.bunnyteam.unfinitaly.app.storage;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unive.dais.bunnyteam.unfinitaly.app.InitActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.MapsActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.MarkerInfoActivity;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.ListaOpereFirebase;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;

/**
 * Created by Enrico on 03/03/2018.
 */

public class FirebaseUtilities {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private boolean ritorno = true;
    private OperaFirebase operaLetta;
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
        if (isLogged())
            if(user.getDisplayName() != null)
                return user.getDisplayName().toString();
            else
                return "";
        else
            return "";
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

    /**
     * Lettura da Firebase quando inizio l'app
     * @param act
     * @return
     */
    public boolean readFromFirebase(final InitActivity act){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if(act != null)
                        act.updateProgressBar((int)(100*dataSnapshot.getChildrenCount())/i);
                }
                long timeafter = System.nanoTime();
                Log.d("FINITO","FROM FIREBASE IN MS: " + (int) ((timeafter - timebefore) / 1000000));
                Log.d("SIZE ARRAYLIST",""+ ListaOpereFirebase.getIstance().getListaOpere().size());
                if(act != null)
                    act.resumeLoadingAfterFirebase();
                else
                    ListaOpereFirebase.getIstance().finishLetturaOpereFromFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Reading DB from Firebase");
                ritorno = false;
            }
        });
        return ritorno;
    }

    /**
     * Lettura da Firebase quando si riapre l'app chiusa da Android quando libera la memoria
     * @param act
     * @param googleMap
     * @return
     */
    public boolean readFromFirebase(final MapsActivity act, final GoogleMap googleMap){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                }
                long timeafter = System.nanoTime();
                Log.d("FINITO","FROM FIREBASE IN MS: " + (int) ((timeafter - timebefore) / 1000000));
                Log.d("SIZE ARRAYLIST",""+ ListaOpereFirebase.getIstance().getListaOpere().size());
                act.prepareMap(googleMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Reading DB from Firebase");
                ritorno = false;
            }
        });
        return ritorno;
    }

    /**
     * Lettura quando si entra nella schermata della singola opera, il listener rimane in ascolto per eventuali cambiamenti
     * @param activity
     * @param operaFirebase
     * @return
     */
    public boolean readOperaSingolaFromFirebase(final MarkerInfoActivity activity, final OperaFirebase operaFirebase){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere").child(operaFirebase.getId_firebase());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("LETTURA DB","SINGOLA OPERA");
                operaLetta = dataSnapshot.getValue(OperaFirebase.class);
                operaLetta.setId_firebase(operaFirebase.getId_firebase());
                Log.d("OPERA LETTA",operaLetta.toString());
                activity.resumeAfterLoadingFirebase(operaLetta);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                ritorno = false;
            }
        });
        return ritorno;
    }

}
