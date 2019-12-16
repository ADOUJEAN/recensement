package ci.k2jts.recensement.ui.newRecensement;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.InscriptionActivity;
import ci.k2jts.recensement.ui.utilities.CameraOption;
import ci.k2jts.recensement.ui.utilities.Constants;
import ci.k2jts.recensement.ui.utilities.OwnDialog;
import ci.k2jts.recensement.ui.utilities.Util;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;


public class NewRecFragment extends Fragment implements LocationListener {

    public static double longitude, latitude, precision, altitude;
    private LocationManager locationManager;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    String Nom,Prenom,Contact,DateNais;
    EditText edNom,edPrenom,edContact,edDateNais;
    Button btEnregistrer;

    int dayNais, monthNais, yearNais;

    ImageButton imBtCapture;
    ImageView imgViewCapturePrise;

    StorageReference storageReferenc;

    private NewRecViewModel newRecViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newRecViewModel =
                ViewModelProviders.of(this).get(NewRecViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_recensement, container, false);

        storageReference= FirebaseStorage.getInstance().getReference("images");
        databaseReference= FirebaseDatabase.getInstance().getReference("images");

        edNom= root.findViewById(R.id.edt_nom);
        edPrenom= root.findViewById(R.id.edt_prenom);
        edContact = root.findViewById(R.id.edt_contact);
        edDateNais = root.findViewById(R.id.edt_date_nais);

        imBtCapture = root.findViewById(R.id.imBt_capture);
        imgViewCapturePrise = root.findViewById(R.id.imgview_capture_prise);
        Constants.imgViewCapturePrise=imgViewCapturePrise;
        btEnregistrer = root.findViewById(R.id.btSaveRec);

        Constants.cameraOption = new CameraOption(getActivity());

        firebaseFirestore = FirebaseFirestore.getInstance();

        edDateNais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment frag = new Util.DatePickerFra(edDateNais,
                        dayNais, yearNais, monthNais);
                frag.show(getFragmentManager(), "DatePicker");
            }
        });


        btEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Constants.PROGRESS.setVisibility(View.VISIBLE);
                Nom = edNom.getText().toString();
                Prenom = edPrenom.getText().toString();
                DateNais = edDateNais.getText().toString();
                Contact = edContact.getText().toString();

                if (Nom.equalsIgnoreCase("") || Prenom.equalsIgnoreCase("")
                        || DateNais.equalsIgnoreCase("") || Contact.equalsIgnoreCase("")) {
                    Constants.PROGRESS.setVisibility(View.GONE);

                    Util.dialogWithAction(getActivity(),
                            "Recensement",
                            "Veuillez saisir tous les champs !).",
                            "OK", R.mipmap.ic_launcher,
                            null);

                    return;
                }

                if (longitude ==0 && altitude==0){
                    Constants.PROGRESS.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "Patientez jusqu'à la récupération des cordonnées GPS !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Constants.USER_ID == null || Constants.USER_ID.equalsIgnoreCase("") ){
                    Constants.PROGRESS.setVisibility(View.GONE);
                    return;
                }

                if (Constants.IMAGE_URI!=null){

                     storageReferenc= storageReference.child(System.currentTimeMillis()+"."+fileExention(Constants.IMAGE_URI));

                    Task<Uri> urlTask = storageReferenc.putFile(Constants.IMAGE_URI).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Constants.PROGRESS.setVisibility(View.GONE);
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReferenc.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String UriDownload = downloadUri.toString();
                                Map<String,String> newPop =new HashMap<>();
                                newPop.put("nom",Nom);
                                newPop.put("prenom",Prenom);
                                newPop.put("contact",Contact);
                                newPop.put("daterecensement",Util.getDatesNow());
                                newPop.put("identifiantagentconnecte",Constants.USER_ID);
                                newPop.put("photo",UriDownload);
                                newPop.put("datenaissance",DateNais);
                                newPop.put("latitude", String.valueOf(latitude));
                                newPop.put("longitude ",String.valueOf(longitude));

                                firebaseFirestore.collection("population").add(newPop).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Constants.PROGRESS.setVisibility(View.GONE);
                                        Util.dialogWithAction(getActivity(),
                                                "Recensement",
                                                "Recensement effectuée!.",
                                                "OK", R.mipmap.ic_launcher,
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        viderChamps();
                                                    }
                                                });
                                        Toast.makeText(getActivity(), "Enregistrement effectué!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Constants.PROGRESS.setVisibility(View.GONE);
                                        String error =e.getMessage();
                                        Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });

                }

            }
        });



        imBtCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new OwnDialog.WithTwoButtons(getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        Constants.cameraOption.choosePhotoFromGallary();

                    }
                }, new Runnable() {
                    @Override
                    public void run() {

                        Constants.cameraOption.takePhotoFromCamera();

                    }
                });
            }
        });


        newRecViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });


        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(LOCATION_SERVICE);


        return root;
    }




    public static Bitmap setImageFromBase64(String base64String){
        byte[] piece =  Base64.decode(base64String.getBytes(), Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(
                piece, 0, piece.length);
    }

    public String fileExention(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }


    private void viderChamps(){

        edContact.setText("");
        edNom.setText("");
        edPrenom.setText("");
        edDateNais.setText("");
        Constants.imgViewCapturePrise.setImageDrawable(null);
    }

    @Override
    public void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPosition();
    }

    public void getPosition() {

        locationManager = (LocationManager)
                getActivity().getSystemService(LOCATION_SERVICE);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        Toast.makeText(getActivity(), msg.toUpperCase(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format("LOCALISATION %s DESACTIVIEE", provider.toUpperCase());
        Toast.makeText(getActivity(), msg.toUpperCase(), Toast.LENGTH_SHORT).show();

        Util.dialogWithAction(getActivity(),
                "INFORMATION",
                "VEUILLEZ ACTIVER LA LOCALISATION!","FERMER", R.mipmap.ic_launcher, null);

    }
}

