package gps;

import ir.saa.android.datalogger.G;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {
     

          @Override

          public void onLocationChanged(Location location) {

        	  // Initialize the location fields

              	G.latitude= String.valueOf(location.getLatitude());
              	G.longitude = String.valueOf(location.getLongitude());
              
              	//MyToast.Show(G.context, String.format("latitude : %s\nlongitude : %s\n",G.latitude,G.longitude) +"Location changed!", Toast.LENGTH_SHORT);

          }

     

          @Override

          public void onStatusChanged(String provider, int status, Bundle extras) {

        	  //MyToast.Show(G.context, provider + "'s status changed to "+status +"!",Toast.LENGTH_SHORT);

          }

     

          @Override

          public void onProviderEnabled(String provider) {

              //MyToast.Show(G.context, "Provider " + provider + " enabled!",Toast.LENGTH_SHORT);

     
          }

     

          @Override

          public void onProviderDisabled(String provider) {

        	  //MyToast.Show(G.context, "Provider " + provider + " disabled!",Toast.LENGTH_SHORT);

          }

      }