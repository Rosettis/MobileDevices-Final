package csci4100.uoit.ca.final_matthewrosettis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Final_MatthewRosettis
 * Created by 100490515 on 12/8/2015.
 */

public class BikeDBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "Bike.db";
    public static final String TABLE_NAME = "Bikes";

    // don't forget to use the column name '_id' for your primary key
    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
            "  _id integer primary key autoincrement, " +
            "  bikeShareId int not null," +
            "  latitude real not null," +
            "  longitude real not null," +
            "  name text not null," +
            "  numBikes int not null," +
            "  address text null" +
            ")";
    public static final String DROP_STATEMENT = "DROP TABLE " + TABLE_NAME;

    public BikeDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // the implementation below is adequate for the first version
        // however, if we change our table at all, we'd need to execute code to move the data
        // to the new table structure, then delete the old tables (renaming the new ones)

        // the current version destroys all existing data
        db.execSQL(DROP_STATEMENT);
        db.execSQL(CREATE_STATEMENT);
    }

    public Bike createBike(int bikeShareId, double latitude, double longitude, String name,
                           int numBikes, String address) {
        // create the object
        Bike Bike = new Bike(bikeShareId, latitude, longitude, name, numBikes, address);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("bikeShareId", Bike.getBikeShareId());
        values.put("latitude", Bike.getLatitude());
        values.put("longitude", Bike.getLongitude());
        values.put("name", Bike.getName());
        values.put("numBikes", Bike.getNumBikes());
        values.put("address", Bike.getAddress());
        long id = database.insert(TABLE_NAME, null, values);

        // assign the Id of the new database row as the Id of the object
        Bike.setId(id);

        return Bike;
    }

    /*public Bike getBike(long id) {
        Bike bike = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the Bike from the database
        String[] columns = new String[] { "_id", "bikeShareId", "latitude", "longitude", "name", "numBikes" };
        Cursor cursor = database.query(TABLE_NAME, columns, "_id = ?", new String[] { "" + id }, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            int bikeShareId = cursor.getInt(0);
            Double latitude = cursor.getDouble(1);
            Double longitude = cursor.getDouble(2);
            String name = cursor.getString(3);
            int numBikes = cursor.getInt(4);
            String address = cursor.getString(5);
            bike = new Bike(bikeShareId, latitude, longitude, name, numBikes, address);
            bike.setId(id);
        }

        Log.i("DatabaseAccess", "getBike(" + id + "):  Bike: " + bike);
        cursor.close();
        return bike;
    }*/

    public ArrayList<Bike> getAllBikes() {
        ArrayList<Bike> Bikes = new ArrayList<>();

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the Bike from the database
        String[] columns = new String[] { "_id", "bikeShareId", "latitude", "longitude", "name", "numBikes" };
        Cursor cursor = database.query(TABLE_NAME, columns, "", new String[]{}, "", "", "");
        cursor.moveToFirst();
        do {
            // collect the Bike data, and place it into a Bike object
            long id = Long.parseLong(cursor.getString(0));
            int bikeShareId = cursor.getInt(0);
            Double latitude = cursor.getDouble(1);
            Double longitude = cursor.getDouble(2);
            String name = cursor.getString(3);
            int numBikes = cursor.getInt(4);
            String address = cursor.getString(5);
            Bike bike = new Bike(bikeShareId, latitude, longitude, name, numBikes, address);
            bike.setId(id);

            // add the current Bike to the list
            Bikes.add(bike);

            // advance to the next row in the results
            cursor.moveToNext();
        } while (!cursor.isAfterLast());

        Log.i("DatabaseAccess", "getAllBikes():  num: " + Bikes.size());
        cursor.close();
        return Bikes;
    }
    public boolean updateBike(Bike bike, long id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("bikeShareId", bike.getBikeShareId());
        values.put("latitude", bike.getLatitude());
        values.put("longitude", bike.getLongitude());
        values.put("name", bike.getName());
        values.put("numBikes", bike.getNumBikes());
        values.put("address", bike.getAddress());
        int numRowsAffected = database.update(TABLE_NAME, values, "_id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "updateBike(" + bike + "):  numRowsAffected: " + numRowsAffected);

        // verify that the Bike was updated successfully
        return (numRowsAffected == 1);
    }

    /*public boolean deleteBike(long id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the Bike
        int numRowsAffected = database.delete(TABLE_NAME, "_id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteBike(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the Bike was deleted successfully
        return (numRowsAffected == 1);
    }*/

    public void deleteAllBikes() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the Bikes
        int numRowsAffected = database.delete(TABLE_NAME, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAllBikes():  numRowsAffected: " + numRowsAffected);
    }
}
