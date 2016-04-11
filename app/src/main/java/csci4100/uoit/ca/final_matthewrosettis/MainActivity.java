package csci4100.uoit.ca.final_matthewrosettis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity implements DataDownloadListener{
    private final String URL = "http://www.bikesharetoronto.com/data/stations/bikeStations.xml";
    private ArrayList<Bike> Bikes = null;
    private int BikeIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFeed();
    }

    public void getFeed(){
        DownloadDataTask task = new DownloadDataTask(this);
        task.execute(URL);
    }

    @Override
    public void dataDownloaded(final List<Bike> bikes) {
        final int count = bikes.size();
        BikeDBHelper dbHelper = new BikeDBHelper(this);
        List<String> bikeArray = new ArrayList<>();
        // delete any products from a previous execution
        dbHelper.deleteAllBikes();
        Log.d("array size", Integer.toString(bikes.size()));
        for(int i = 0; i < count; i++){
            bikeArray.add(bikes.get(i).getAddress());
            dbHelper.createBike(bikes.get(i).getBikeShareId(), bikes.get(i).getLatitude(),
                    bikes.get(i).getLongitude(), bikes.get(i).getName(),
                    bikes.get(i).getNumBikes(), bikes.get(i).getAddress());
        }
        this.Bikes = dbHelper.getAllBikes();
        this.BikeIndex = 0;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, bikeArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner);
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String items = sItems.getSelectedItem().toString();
                Log.i("Selected item : ", items);
                for(int i = 0; i < count; i++){
                    if(bikes.get(i).getAddress().equals(items)){
                        showBike(bikes.get(i));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        sItems.setAdapter(adapter);
        showBike(Bikes.get(0));
    }

    public void showMap(View view){
        Intent mapIntent = new Intent (this, MapsActivity.class);
        Double[][] position = new Double[][] {};
        for(int i = 0; i < Bikes.size(); i++){
            position[i][0] = Bikes.get(i).getLatitude();
            position[i][1] = Bikes.get(i).getLongitude();
        }
        mapIntent.putExtra("bike positions",position);
        startActivity(mapIntent);
    }

    public void Save(View view){
        BikeDBHelper dbHelper = new BikeDBHelper(this);
        EditText bikeShareIdField = (EditText)findViewById(R.id.bikeShareId);
        EditText nameIdField = (EditText)findViewById(R.id.nameID);
        EditText addressIdField = (EditText)findViewById(R.id.addressID);
        EditText latitudeIdField = (EditText)findViewById(R.id.latitudeID);
        EditText longitudeIdField = (EditText)findViewById(R.id.longitudeID);
        EditText numBikesIdField = (EditText)findViewById(R.id.numBikesID);

        Bike temp = new Bike(Integer.parseInt(bikeShareIdField.getText().toString()),
                Double.parseDouble(latitudeIdField.getText().toString()),
                Double.parseDouble(longitudeIdField.getText().toString()),
                nameIdField.getText().toString(),
                Integer.parseInt(numBikesIdField.getText().toString()),
                addressIdField.getText().toString());

        dbHelper.updateBike(temp, findViewById(R.id.spinner).getId());
    }

    private void showBike(Bike bike) {
        EditText bikeShareIdField = (EditText)findViewById(R.id.bikeShareId);
        EditText nameIdField = (EditText)findViewById(R.id.nameID);
        EditText addressIdField = (EditText)findViewById(R.id.addressID);
//        EditText latitudeIdField = (EditText)findViewById(R.id.latitudeID);
//        EditText longitudeIdField = (EditText)findViewById(R.id.longitudeID);
        EditText numBikesIdField = (EditText)findViewById(R.id.numBikesID);

        //Setting EditText Fields to Bike Values
        bikeShareIdField.setText(Integer.toString(bike.getBikeShareId()));
        addressIdField.setText(bike.getAddress());
        nameIdField.setText(bike.getName());
        numBikesIdField.setText(Integer.toString(bike.getNumBikes()));
    }

}
