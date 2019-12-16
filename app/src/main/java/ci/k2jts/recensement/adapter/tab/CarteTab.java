package ci.k2jts.recensement.adapter.tab;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ci.k2jts.recensement.R;
import ci.k2jts.recensement.ui.utilities.CameraOption;

import static ci.k2jts.recensement.MainActivity.latitude;
import static ci.k2jts.recensement.MainActivity.longitude;

/**
 * Created by ADOU JOHN on 14,décembre,2019
 * K2JTS Ltd
 * adoujean1996@gmail.com
 */
public class CarteTab extends Fragment implements OnMapReadyCallback {
    MapView mMapView;
    GoogleMap mGoogleMap;
    View mView;

   public CarteTab(){

    }

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab2_carte, container,
                false);
        return mView;
    }

 @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

       super.onViewCreated(view,savedInstanceState);

       mMapView=mView.findViewById(R.id.mapView);
       if (mMapView!=null){
//           mMapView.onResume();
           mMapView.getMapAsync(this);
       }

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
       MapsInitializer.initialize(getContext());
       mGoogleMap=googleMap;
       googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng sydney = new LatLng(latitude, longitude);
//        LatLng sydney1 = new LatLng(5.384664, -3.987846);
//        LatLng sydney2 = new LatLng(5.385446, -3.973960);
//        LatLng sydney3 = new LatLng(5.391452, -3.988445);

        googleMap.addMarker(new MarkerOptions().position(sydney).title("Ma position"));
        CameraPosition libre =CameraPosition.builder().target( sydney).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(libre));

    }



}
