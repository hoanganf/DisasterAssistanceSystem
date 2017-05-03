package an.it.disasterassistancesystem.objects;

import java.util.ArrayList;

/**
 * Created by hoang on 10/24/2016.
 */
public class Driver extends User {
    private String identityCardImageURL;
    private String identityCardNumber;
    private ArrayList<License> licenses;
    private ArrayList<DriverRate> rates;
    private ArrayList<DriverPrice> prices;
    private float longtitude;
    private float latitude;
    private int driverStatus;
    private long expireDate;
    private String contactDetail;
    private String contactImgeURL;

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public String getContactImgeURL() {
        return contactImgeURL;
    }

    public void setContactImgeURL(String contactImgeURL) {
        this.contactImgeURL = contactImgeURL;
    }

    public Driver(String userName, int userType, String avatarURL, String fullName, String identityCardImageURL, String identityCardNumber, String email, String mobilePhone, float longtitude, float latitude, int driverStatus, String birthdate, String address) {
        super(userName, userType, avatarURL, fullName,birthdate,address,email,mobilePhone);
        this.identityCardImageURL = identityCardImageURL;
        this.identityCardNumber = identityCardNumber;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.driverStatus = driverStatus;
        this.birthdate = birthdate;
        this.address = address;
    }



    public String getIdentityCardImageURL() {
        return identityCardImageURL;
    }

    public void setIdentityCardImageURL(String identityCardImageURL) {
        this.identityCardImageURL = identityCardImageURL;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public ArrayList<License> getLicenses() {
        return licenses;
    }

    public void setContacts(ArrayList<License> licenses) {
        this.licenses = licenses;
    }

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }





    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public void setLicenses(ArrayList<License> licenses) {
        this.licenses = licenses;
    }

    public ArrayList<DriverRate> getRates() {
        return rates;
    }

    public void setRates(ArrayList<DriverRate> rates) {
        this.rates = rates;
    }

    public ArrayList<DriverPrice> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<DriverPrice> prices) {
        this.prices = prices;
    }
}

