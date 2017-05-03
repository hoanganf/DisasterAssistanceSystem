package an.it.disasterassistancesystem.objects;

/**
 * Created by anit on 11/10/16.
 */

public class VehicleRate {
    private String userName;
    private float rate;
    private String comment;

    public VehicleRate(String userName, float rate, String comment) {
        this.userName = userName;
        this.rate = rate;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
