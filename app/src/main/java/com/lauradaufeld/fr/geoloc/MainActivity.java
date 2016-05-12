package com.lauradaufeld.fr.geoloc;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String serviceString = LOCATION_SERVICE;
        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(serviceString);


        String provider = LocationManager.NETWORK_PROVIDER;
        try {
            locationManager.requestLocationUpdates(provider,5000,5,myLocationListener);
        } catch (SecurityException se) {
            Toast toast = Toast.makeText(getApplicationContext(), "Erreur d'accès au GPS", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    final LocationListener myLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            TextView coordTextView = (TextView) findViewById(R.id.coordTextView);

            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            coordTextView.setText(lat + " / " + lon);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        class MyClientTask extends AsyncTask<Void, Void, Void> {

            String dstAddress = "192.168.2.138";
            int dstPort = 8888;
            String response = "";

            @Override
            protected Void doInBackground(Void... arg0) {

                Socket socket = null;

                try {
                    socket = new Socket(dstAddress, dstPort);

                    ByteArrayOutputStream byteArrayOutputStream =
                            new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];

                    int bytesRead;
                    InputStream inputStream = socket.getInputStream();

                    /*
                     * notice:
                     * inputStream.read() will block if no data return
                     */
                    while ((bytesRead = inputStream.read(buffer)) != -1){
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                        response += byteArrayOutputStream.toString("UTF-8");
                    }

                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();

                }finally{
                    if(socket != null){
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }
        }
    };
}
