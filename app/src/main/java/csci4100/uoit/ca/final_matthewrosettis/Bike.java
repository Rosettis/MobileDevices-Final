package csci4100.uoit.ca.final_matthewrosettis;

/**
 * Final_MatthewRosettis
 * Created by 100490515 on 12/8/2015.
 */

public class Bike {
    private long id = -1;
    private int bikeShareId = -1;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String name = null;
    private int numBikes = 0;
    private String address = null;


    public Bike(int bikeShareId, double latitude, double longitude, String name, int numBikes, String address) {

        setBikeShareId(bikeShareId);
        setLatitude(latitude);
        setLongitude(longitude);
        setName(name);
        setNumBikes(numBikes);
        setAddress(address);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBikeShareId() {
        return bikeShareId;
    }

    public void setBikeShareId(int bikeShareId) {
        this.bikeShareId = bikeShareId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumBikes() {
        return numBikes;
    }

    public void setNumBikes(int numBikes) {
        this.numBikes = numBikes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString(){
        return name + " (" + address + ")";
    }
}
