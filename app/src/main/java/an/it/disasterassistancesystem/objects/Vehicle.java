package an.it.disasterassistancesystem.objects;

import java.util.ArrayList;

/**
 * Created by anit on 11/10/16.
 */

public class Vehicle {
    private long ID;
    private ArrayList<String> imageURLs;
    private ArrayList<VehicleRate> vehicleRates;
    private String userName;
    private int age;
    private int type;
    private String typeName;
    private int brandID;
    private String brandName;

    public Vehicle(long ID, ArrayList<String> imageURLs, ArrayList<VehicleRate> vehicleRates, String userName, int age, int type, String typeName, int brandID, String brandName) {
        this.ID = ID;
        this.imageURLs = imageURLs;
        this.vehicleRates = vehicleRates;
        this.userName = userName;
        this.age = age;
        this.type = type;
        this.typeName = typeName;
        this.brandID = brandID;
        this.brandName = brandName;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public ArrayList<VehicleRate> getVehicleRates() {
        return vehicleRates;
    }

    public void setVehicleRates(ArrayList<VehicleRate> vehicleRates) {
        this.vehicleRates = vehicleRates;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
