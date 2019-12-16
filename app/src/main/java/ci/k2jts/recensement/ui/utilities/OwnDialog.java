package ci.k2jts.recensement.ui.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import ci.k2jts.recensement.R;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class OwnDialog {
    public static class WithTwoButtons{
        public WithTwoButtons(final Context ctx,
                              final Runnable fungallerie,
                              final Runnable funphoto){


            View alertLayout = LayoutInflater.from(ctx).inflate(R.layout.photo, null);
            final LinearLayout photo = alertLayout.findViewById(R.id.photo);
            final LinearLayout gallerie = alertLayout.findViewById(R.id.gallerie);
            AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            final AlertDialog dialog = alert.create();
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(funphoto!=null){
                        funphoto.run();
                    }
                    dialog.dismiss();

                }
            });
            gallerie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fungallerie!=null){
                        fungallerie.run();
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public static class dialogWithTwoActions{

        public dialogWithTwoActions(final Context context,final String message,final int icon,final String titleBuilder,
                                    final String titleFirstButton, final Runnable funcFirstButton, final String titleSecondButton,
                                    final Runnable funcSecondButton) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setIcon(icon);
            alertDialogBuilder.setTitle(titleBuilder);
            alertDialogBuilder.setPositiveButton(titleFirstButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    if(funcFirstButton!=null)
                        funcFirstButton.run();
                }
            });
            alertDialogBuilder.setNegativeButton(titleSecondButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(funcSecondButton!=null)
                        funcSecondButton.run();
                }
            });
            alertDialogBuilder.show();
        }

    }

}
