package it.unive.dais.bunnyteam.unfinitaly.app.testing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import it.unive.dais.bunnyteam.unfinitaly.app.R;
import it.unive.dais.bunnyteam.unfinitaly.app.opere.OperaFirebase;

@Deprecated
public class TestFirebase extends AppCompatActivity {
    ArrayList<OperaFirebase> listaOpere = new ArrayList<>();
    OperaFirebase opera;
    TextView printData;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_firebase);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            Log.d("EXIT","ESCO AAAAAAAAAa");
            /*moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);*/
            finishAffinity();
            System.exit(0);
        }
        /*printData = (TextView)findViewById(R.id.printData);
        FirebaseUtilities.getIstance().readFromFirebase();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("opere");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("NUMERO DATI LETTI",""+dataSnapshot.getChildrenCount());
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    listaOpere.add(data.getValue(OperaFirebase.class));
                }
                Log.d("FINITO","FROM FIREBASE");
                Log.d("SIZE ARRAYLIST",""+listaOpere.size());
                for(int i = 0;i < listaOpere.size();i++){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("OPERA" + i,listaOpere.get(i).toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Reading DB from Firebase");
            }
        });*/
    }
}
