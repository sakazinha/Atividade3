package com.example.myapplication;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DirectionsHelper {

    private GeoApiContext geoApiContext;

    public DirectionsHelper(String apiKey) {
        geoApiContext = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public List<List<LatLng>> getDirections(LatLng origin, LatLng destination) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(geoApiContext)
                    .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                    .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                    .alternatives(true)
                    .await();

            List<List<LatLng>> routes = new ArrayList<>();

            if (result.routes != null && result.routes.length > 0) {
                for (DirectionsRoute route : result.routes) {
                    List<LatLng> path = new ArrayList<>();
                    if (route.overviewPolyline != null) {
                        List<com.google.maps.model.LatLng> decodedPath = route.overviewPolyline.decodePath();
                        for (com.google.maps.model.LatLng latLng : decodedPath) {
                            path.add(new LatLng(latLng.lat, latLng.lng));
                        }
                    }
                    routes.add(path);
                }
            }

            return routes;
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}