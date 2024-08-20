package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class LocationManagerHelper extends Thread {

    private static String TAG = "LocationManagerHelper";

    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location previousLocation = null;
    private long previousTime = 0;

    public LocationManagerHelper(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Looper.prepare();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                handleLocationChanged(location);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,  // Intervalo de atualização em milissegundos
                    10,    // Distância mínima de atualização em metros
                    locationListener
            );
        }

        Looper.loop();
    }

    private void handleLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        long currentTime = System.currentTimeMillis();

        Log.d(TAG, "Nova localização recebida - Latitude: " + latitude + ", Longitude: " + longitude);

        if (previousLocation != null) {
            float distance = location.distanceTo(previousLocation);
            long timeDelta = currentTime - previousTime;
            float speed = (distance / timeDelta) * 1000;

            // Converte para km/h
            float speedKmh = (speed * 3600) / 1000;
            Log.d(TAG, "Velocidade calculada: " + speedKmh + " km/h");

            // Atualiza a velocidade no MapsActivity
            ((MapsActivity) context).updateSpeed(speedKmh);
        }

        previousLocation = location;
        previousTime = currentTime;

        ((MapsActivity) context).setLatitude(latitude);
        ((MapsActivity) context).setLongitude(longitude);
        ((MapsActivity) context).atualizarMapa();
    }

    public void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
