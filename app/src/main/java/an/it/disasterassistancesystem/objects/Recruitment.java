package an.it.disasterassistancesystem.objects;

/**
 * Created by hoang on 10/24/2016.
 */
public class Recruitment {
    private long id;
    private int licenseType;
    private int numberDriver;
    private String licenseName;
    private double price;
    private int priceType;
    private String priceName;
    private String note;

    public Recruitment(long id,int licenseType,String licenseName, int numberDriver,int priceType,String priceName, double price, String note) {
        this.id = id;
        this.licenseType = licenseType;
        this.licenseName=licenseName;
        this.numberDriver = numberDriver;
        this.priceType=priceType;
        this.priceName=priceName;
        this.price=price;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLicenseName() {
        return licenseName;
    }

    public void setLicenseName(String licenseName) {
        this.licenseName = licenseName;
    }

    public int getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(int licenseType) {
        this.licenseType = licenseType;
    }

    public int getNumberDriver() {
        return numberDriver;
    }

    public void setNumberDriver(int numberDriver) {
        this.numberDriver = numberDriver;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
