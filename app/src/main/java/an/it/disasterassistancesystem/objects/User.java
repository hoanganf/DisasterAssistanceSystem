package an.it.disasterassistancesystem.objects;

/**
 * Created by hoang on 10/22/2016.
 */
public class User {
    protected String userName;
    protected int userType;
    protected String avatarURL;
    protected String fullName;
    protected String birthdate;
    protected String address;
    protected String email;
    protected String mobilePhone;

    public User(String userName, int userType, String avatarURL, String fullName, String birthdate, String address, String email, String mobilePhone) {
        this.userName = userName;
        this.userType = userType;
        this.avatarURL = avatarURL;
        this.fullName = fullName;
        this.birthdate = birthdate;
        this.address = address;
        this.email = email;
        this.mobilePhone = mobilePhone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
