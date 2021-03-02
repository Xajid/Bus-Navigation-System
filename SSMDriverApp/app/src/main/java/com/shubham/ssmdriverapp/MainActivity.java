package com.shubham.ssmdriverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btn, submit_button;
    EditText e1, e2, e3;

    String bn, dn, dp, lat, lon;

    ProgressDialog pd;

    LocationManager locationManager;

   DatabaseReference dRefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);
        dRefrence=FirebaseDatabase.getInstance().getReference("BusLocation");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                //runtime-permission

                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                            {Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                         lat=""+location.getLatitude();
                         lon=""+location.getLongitude();
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
                });

                pd=new ProgressDialog(MainActivity.this);
                pd.setMessage("Please Wait!");
                pd.setCanceledOnTouchOutside(false);
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.register_layout);
                dialog.setCanceledOnTouchOutside(false);
                e1=dialog.findViewById(R.id.editText);
                e2=dialog.findViewById(R.id.editText2);
                e3=dialog.findViewById(R.id.editText3);
                submit_button=dialog.findViewById(R.id.button_start);
                submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
   pd.show();

   bn=e1.getText().toString().trim();
   dn=e2.getText().toString();
   dp=e3.getText().toString();


   BusDetails details=new BusDetails();
   details.setBusNumber(bn);
   details.setDriverName(dn);
   details.setDriverPhone(dp);
   details.setLatitude(lat);
   details.setLongitude(lon);

   dRefrence.child(bn).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
       @Override
       public void onSuccess(Void aVoid)
       {
       pd.dismiss();
           Toast.makeText(MainActivity.this, "Let's  Start", Toast.LENGTH_SHORT).show();
       }
   }).addOnFailureListener(new OnFailureListener() {
       @Override
       public void onFailure(@NonNull Exception e) {
           pd.dismiss();
           Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
       }
   });


   Toast.makeText(MainActivity.this, bn+"\n"+dn+"\n"+dp+"\n"+lat+"\n"+lon, Toast.LENGTH_SHORT).show();



                    }
                });
                dialog.show();
            }
        });

    }
}
