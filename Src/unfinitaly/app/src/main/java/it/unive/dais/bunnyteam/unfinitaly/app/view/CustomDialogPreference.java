package it.unive.dais.bunnyteam.unfinitaly.app.view;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.content.Context;
import android.util.AttributeSet;

public class CustomDialogPreference extends DialogPreference implements DialogInterface.OnClickListener
{
    /*
    Classe che estende la classe di base DialogPreference, implementa il metodo onClick per sapere quando
    l'utente ha cliccato il tasto per cancellare i dati dell'applicazione o per annullare l'operazione
     */
    private Context context;
    public CustomDialogPreference(Context oContext, AttributeSet attrs)
    {
        super(oContext, attrs);
        context = oContext;
        setPositiveButtonText("Conferma");
        setNegativeButtonText("Annulla");
    }
    @Override
    public void onClick(DialogInterface dialog, int which){
        if(which == DialogInterface.BUTTON_POSITIVE) {
            //Cancello i dati dell'applicazione
            ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();
        }
    }
}