package an.it.disasterassistancesystem.objects;

/**
 * Created by anit on 11/10/16.
 */

public class DriverPrice {
    private int priceType;
    private String priceTypeName;
    private double price;
    private String note;

    public DriverPrice(int priceType, String priceTypeName, double price, String note) {
        this.priceType = priceType;
        this.priceTypeName = priceTypeName;
        this.price = price;
        this.note = note;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public String getPriceTypeName() {
        return priceTypeName;
    }

    public void setPriceTypeName(String priceTypeName) {
        this.priceTypeName = priceTypeName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
