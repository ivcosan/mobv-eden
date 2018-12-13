package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import java.util.Date;

public class UserProfile {
    private String username;
    private Date registrationDate;
    private int numberOfPosts;

    public UserProfile() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }


    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }
}
