package ci.k2jts.recensement.ui.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ci.k2jts.recensement.R;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class Util {

    public static void checkPermissionAndroidApplication(Activity activity){


        //VALIDER LISTE DES PERMISSIONS
        if(ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE)
//                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.VIBRATE)
//                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BLUETOOTH)
//                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.BLUETOOTH_ADMIN)
                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_NETWORK_STATE)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.VIBRATE)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH)
//                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.BLUETOOTH_ADMIN)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ){
                //Cela signifie que la permission à déjà était
                //demandé et l'utilisateur l'a refusé
                //Vous pouvez aussi expliquer à l'utilisateur pourquoi
                //cette permission est nécessaire et la redemander


            } else {
                //Sinon demander la permission
                int REQUEST_PERMISSIONS = 50;
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }, REQUEST_PERMISSIONS);
            }
        }


    }


    public static void dialogWithAction(Context context, String title, String message, String titleButton, int icon, final Runnable function) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setIcon(icon);
        adb.setCancelable(false);
        adb.setPositiveButton(titleButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (function != null) {
                    function.run();
                }
            }
        });
        adb.show();
    }

    public static String getDatesNow() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static int getValueDate(String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String currentDateandTime = sdf.format(new Date());
        return Integer.parseInt(currentDateandTime);
    }


    @SuppressLint("ValidFragment")
    public static class DatePickerFra extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private EditText text;

        int var_day, var_year, var_month;

        public DatePickerFra(EditText text, int day, int year, int month) {
            this.text = text;
            this. var_day = day;
            this. var_year = year;
            this. var_month = month;
        }

        @Override
        public Dialog onCreateDialog(Bundle saveInstanceState){
            final Calendar c = Calendar.getInstance();
            int d = c.get(Calendar.DAY_OF_MONTH);
            int m = c.get(Calendar.MONTH);
            int y = c.get(Calendar.YEAR);

            return new DatePickerDialog(getActivity(), this,y,m,d);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            var_day = dayOfMonth;
            var_month = month+1;
            var_year=year;
            this.text.setText(dizaine(""+dayOfMonth)+"/"+dizaine(""+var_month)+"/"+dizaine(""+year));
        }

    }

    public static String dizaine(String val){
        if(val.length()==1){
            return "0"+val;
        }
        return val;
    }


    public static Bitmap resizeImage(Bitmap bp){
        float scaleWidth = ((float) 400) / bp.getWidth();
        float scaleHeight = ((float) 500) / bp.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // return Bitmap.createBitmap(bp, 0,0, bp.getWidth(), bp.getHeight(), matrix, false);
        return Bitmap.createScaledBitmap(bp, 500, 400, true);
    }

    public static long availableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
            availableBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = stat.getBlockSize();
            availableBlocks = stat.getAvailableBlocks();
        }
        return availableBlocks * blockSize;
    }

    static class MemoryTest implements ComponentCallbacks2 {
        Activity activity;
        MemoryTest(Activity act){
            this.activity = act;
        }
        boolean isMemoryLow() {
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            return memoryInfo.lowMemory;
        }
        @Override
        public void onTrimMemory(int level) {

            switch (level) {

                case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                    break;

                case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:

                    break;

                case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                    showMessage(null);
                    break;
                case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                    showMessage(null);
                    break;
                case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                    showMessage( new Runnable() {
                        @Override
                        public void run() {
                            activity.finish();
                        }
                    });

                    break;

                default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                    break;
            }
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            showMessage( new Runnable() {
                @Override
                public void run() {
                    activity.finish();
                }
            });

        }

        private void showMessage(Runnable fun){
            dialogWithAction(activity, "INFORMATION",
                    "Vous n'avez pas d'espace mémoire! Veuillez supprimer des données!",
                    "FERMER",
                    R.mipmap.ic_launcher,
                    fun);
        }
    }

}
