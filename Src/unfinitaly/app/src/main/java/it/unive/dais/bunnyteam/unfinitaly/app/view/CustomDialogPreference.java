package it.unive.dais.bunnyteam.unfinitaly.app.view;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.content.Context;
import android.util.AttributeSet;

public class CustomDialogPreference extends DialogPreference implements DialogInterface.OnClickListener
{
    Context context;
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