package an.it.disasterassistancesystem.objects;

import java.util.ArrayList;

/**
 * Created by hoang on 10/24/2016.
 */
public class Employer extends User {
    private ArrayList<Recruitment> recruitments;

    public Employer(String userName, int userType, String avatarURL, String fullName, String birthdate, String address, String email, String mobilePhone) {
        super(userName, userType, avatarURL, fullName,  birthdate, address, email, mobilePhone);
        this.recruitments = recruitments;
    }

    public ArrayList<Recruitment> getRecruitments() {
        return recruitments;
    }

    public void setRecruitments(ArrayList<Recruitment> recruitments) {
        this.recruitments = recruitments;
    }
}
