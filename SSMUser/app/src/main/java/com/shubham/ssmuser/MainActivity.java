package com.shubham.ssmuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;

    DatabaseReference dRefrence;

    ArrayList<String> arrayList;

    Button btn;

    String lat,lon;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner=findViewById(R.id.spinner);
        dRefrence= FirebaseDatabase.getInstance().getReference("BusLocation");
      arrayList=new ArrayList<String>();
      arrayList.add("Select Bus");
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        spinner.setAdapter(adapter);
btn=findViewById(R.id.button);


      dRefrence.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

              for (DataSnapshot snapshot : dataSnapshot.getChildren())
              {

                  BusDetails details= snapshot.getValue(BusDetails.class);

                  arrayList.add(""+details.getBusNumber());

                  //Toast.makeText(MainActivity.this, "" +details.getBusNumber(), Toast.LENGTH_SHORT).show();
              }
              adapter.notifyDataSetChanged();


          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v)
          {

            final String busNumber=spinner.getSelectedItem().toString();


            dRefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {

                        BusDetails details= snapshot.getValue(BusDetails.class);

                        if (details.getBusNumber().equals(busNumber))
                        {

                      lat=details.getLatitude();
                      lon=details.getLongitude();
                        }
                    }


                    Intent i=new Intent(MainActivity.this,MapsActivity.class);
                    i.putExtra("lat",Double.parseDouble(lat));
                    i.putExtra("lon",Double.parseDouble(lon));
                    startActivity(i);

                    Toast.makeText(MainActivity.this, "" +lat+"\n"+lon, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




          }
      });




    }
}
