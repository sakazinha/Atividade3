package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;
import android.util.Log;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationManagerHelper locationManagerHelper;
    private Double latitude;
    private Double longitude;

    private TextView speedTextView;

    // Coordenadas das rotas
    private static final LatLng[] ROUTE1 = {
            new LatLng(-21.2343, -45.0049),  // Av. Evaristo Gomes Guerra, 536 - loja 03/04 - Jardim Gloria, Lavras - MG, 37209-214, Brazil
            new LatLng(-21.2338, -44.9996),  // R. Cleto Fantazzini, 73A - Vila Sao Francisco, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2363, -44.9988),  // R. Bernadino Macieira, 249 - Jardim Klintiana, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2399, -44.9983),  // R. Benedito Valadares, 62 - Dos Ipês, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2383, -44.9939),  // Av. Dr. Silvio Menicuci, 648 - Vila Ester, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2439, -44.9918)   // Av. Dr. Silvio Menicuci, 1479 - A, - Pres. Kennedy, Lavras - MG, 37200-000, Brazil
    };

    private static final LatLng[] ROUTE2 = {
            new LatLng(-21.2343, -45.0049),  // Av. Evaristo Gomes Guerra, 536 - loja 03/04 - Jardim Gloria, Lavras - MG, 37209-214, Brazil
            new LatLng(-21.2338, -44.9996),  // R. Cleto Fantazzini, 73A - Vila Sao Francisco, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2362, -44.9979),  // MG-335, 275 - Dos Ipês, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2379, -44.9966),  // R. Saturno de Pádua, 199a - Dos Ipês, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2382, -44.9940),  // Av. Dr. Silvio Menicuci, 648 - Vila Ester, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2439, -44.9918)   // Av. Dr. Silvio Menicuci, 1479 - A, - Pres. Kennedy, Lavras - MG, 37200-000, Brazil
    };

    private static final LatLng[] ROUTE3 = {
            new LatLng(-21.2343, -45.0049),  // Av. Evaristo Gomes Guerra, 536 - loja 03/04 - Jardim Gloria, Lavras - MG, 37209-214, Brazil
            new LatLng(-21.2338, -44.9996),  // R. Cleto Fantazzini, 73A - Vila Sao Francisco, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2329, -44.9973),  // Praça Dr. Jorge, 297 - Dos Ipês, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2340, -44.9948),  // Av. Dr. Silvio Menicuci, 150a - Inácio Valentim, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2383, -44.9939),  // Av. Dr. Silvio Menicuci, 648 - Vila Ester, Lavras - MG, 37200-000, Brazil
            new LatLng(-21.2439, -44.9918)   // Av. Dr. Silvio Menicuci, 1479 - A, - Pres. Kennedy, Lavras - MG, 37200-000, Brazil
    };

    private LatLng[] currentRoute;  // Armazena a rota selecionada
    private long startTime = -1;  // Tempo de início da rota (-1 indica que ainda não começou)
    private int currentPointIndex;  // Índice do ponto atual na rota
    private ArrayList<Long> timeStamps;  // Lista de timestamps para cada ponto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        speedTextView = findViewById(R.id.speedTextView);

        Permissoes.validarPermissoes(permissoes, this, 1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        locationManagerHelper = new LocationManagerHelper(this);
        locationManagerHelper.start();
    }

    public void updateSpeed(final float speed) {
        runOnUiThread(() -> {
            String speedText = String.format(Locale.getDefault(), "%.2f km/h", speed);
            speedTextView.setText(speedText);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManagerHelper.stopLocationUpdates();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraIdleListener(() -> {
            LatLng newPosition = mMap.getCameraPosition().target;
            String formattedLatitude = String.format(Locale.getDefault(), "%.6f", newPosition.latitude);
            String formattedLongitude = String.format(Locale.getDefault(), "%.6f", newPosition.longitude);

            TextView latitudeTextView = findViewById(R.id.latitudeTextView);
            TextView longitudeTextView = findViewById(R.id.longitudeTextView);
            latitudeTextView.setText(formattedLatitude);
            longitudeTextView.setText(formattedLongitude);
        });

        mMap.setOnMapClickListener(latLng -> {
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            Toast.makeText(MapsActivity.this, "Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_SHORT).show();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Local selecionado").snippet("Descrição").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                locationManagerHelper = new LocationManagerHelper(this);
                locationManagerHelper.start();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", (dialog, which) -> finish());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void atualizarMapa() {
        runOnUiThread(() -> {
            mMap.clear();
            LatLng localUsuario = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(localUsuario).title("Meu local"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUsuario, 15));

            checkProximityToNextPoint(localUsuario);  // Verifica se o usuário está próximo do próximo ponto na rota

        });
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void selectRoute1(View view) {
        displayRoute(ROUTE1);
        startRoute(ROUTE1);
    }

    public void selectRoute2(View view) {
        displayRoute(ROUTE2);
        startRoute(ROUTE2);
    }

    public void selectRoute3(View view) {
        displayRoute(ROUTE3);
        startRoute(ROUTE3);
    }

    private void startRoute(LatLng[] route) {
        currentRoute = route;
        startTime = -1;  // Inicializar como -1 para indicar que o tempo ainda não começou
        currentPointIndex = 0;
        timeStamps = new ArrayList<>();

        displayRoute(route);
    }

    private void displayRoute(LatLng[] route) {
        mMap.clear();  // Limpa o mapa de quaisquer marcadores ou polilinhas anteriores

        for (int i = 0; i < route.length; i++) {
            LatLng point = route[i];
            MarkerOptions markerOptions = new MarkerOptions().position(point);

            if (i == 0) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));  // Primeiro marcador - azul
            } else if (i == route.length - 1) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));  // Último marcador - verde
            } else {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));  // Outros marcadores - laranja
            }

            mMap.addMarker(markerOptions);  // Adiciona o marcador no mapa
        }

        // Removendo o código que desenha a polilinha
        // PolylineOptions polylineOptions = new PolylineOptions().add(route).color(ContextCompat.getColor(this, R.color.black)).width(5);
        // mMap.addPolyline(polylineOptions);

        // Mover a câmera para o ponto inicial da rota
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route[0], 14));
    }

    private void checkProximityToNextPoint(LatLng currentLocation) {
        if (currentRoute == null || currentPointIndex >= currentRoute.length) {
            Log.d("MapsActivity", "Rota não iniciada ou todos os pontos já foram alcançados.");
            return;
        }

        LatLng nextPoint = currentRoute[currentPointIndex];
        float[] results = new float[1];
        android.location.Location.distanceBetween(
                currentLocation.latitude, currentLocation.longitude,
                nextPoint.latitude, nextPoint.longitude,
                results);

        //Log.d("MapsActivity", "Distância até o próximo ponto: " + results[0] + " metros.");

        if (results[0] < 50) {  // Verifica se o usuário está a 50 metros do próximo ponto
            if (startTime == -1) {
                startTime = System.currentTimeMillis();  // Inicializa o tempo de início na primeira movimentação
                Log.d("MapsActivity", "Tempo de início registrado: " + startTime + " ms");
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            timeStamps.add(elapsedTime);

            // Log para informar o tempo ao passar pelo ponto
            Log.d("MapsActivity", "Ponto " + (currentPointIndex + 1) + " alcançado. Tempo: " + elapsedTime + " ms");

            currentPointIndex++;

            if (currentPointIndex >= currentRoute.length) {
                Log.d("MapsActivity", "Rota concluída! Tempos em cada ponto: " + timeStamps.toString());
                Toast.makeText(this, "Rota concluída! Confira os tempos no Logcat.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
