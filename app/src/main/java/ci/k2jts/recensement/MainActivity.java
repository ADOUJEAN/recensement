package ci.k2jts.recensement;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Driver;

import ci.k2jts.recensement.ui.location;
import ci.k2jts.recensement.ui.utilities.Constants;
import ci.k2jts.recensement.ui.utilities.Util;


public class MainActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private AppBarConfiguration mAppBarConfiguration;


    public static double longitude, latitude, precision, altitude;
    private LocationManager locationManager;

    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_new_rec, R.id.nav_list_rec,
                R.id.nav_statistiqur)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        navigationView.setNavigationItemSelectedListener(this);

        progress = findViewById(R.id.progress);


        Constants.PROGRESS =progress;
        Util.checkPermissionAndroidApplication(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deconnecter) {
            fermer();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void fermer(){
        new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }.run();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if ( Constants.cameraOption!=null && Constants.imgViewCapturePrise!=null)
                Constants.cameraOption.onActivityResult(requestCode, resultCode, data, Constants.imgViewCapturePrise);
//            Toast.makeText(this, "Oops==>", Toast.LENGTH_LONG).show();

//            imgViewCapturePrise.setImageBitmap(setImageFromBase64(Constants.IMAGEPRISE));

            //afficheImage();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Oops==>"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPosition();
    }

    public void getPosition() {

        locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    10000,
                    0,
                    this);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10000,
                0,
                this);

    }


    @Override
    public void onLocationChanged(Location location) {
        altitude = location.getAltitude();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        precision =location.getAccuracy();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "HORS SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORELLEMENT INDISPONIBLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "DISPONIBLE";
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format("LA LOCALISATION %s MAINTENANT ACTIVEE", provider.toUpperCase());

        Toast.makeText(this, msg.toUpperCase(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format("LOCALISATION %s DESACTIVIEE", provider.toUpperCase());
        Toast.makeText(this, msg.toUpperCase(), Toast.LENGTH_SHORT).show();

        Util.dialogWithAction(this,
                "INFORMATION",
                "VEUILLEZ ACTIVER LA LOCALISATION!","FERMER", R.mipmap.ic_launcher, null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int idItem = menuItem.getItemId();
        if (idItem == R.id.nav_home) {
            // Handle the camera action


        }if (idItem == R.id.nav_new_rec) {
            // Handle the camera action


        }if (idItem == R.id.nav_list_rec) {
            // Handle the camera action

        }if (idItem == R.id.nav_statistiqur) {
            // Handle the camera action

        }
        return false;
    }
}
