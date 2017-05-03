package an.it.disasterassistancesystem.objects;

/**
 * Created by hoang on 10/24/2016.
 */
public class License {
    private int id;
    private String name;
    private String imageURL;
    private String note;
    public License(int id, String imageURL,String name, String note) {
        this.id = id;
        this.imageURL = imageURL;
        this.note = note;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
