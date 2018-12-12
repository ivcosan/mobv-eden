package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static User instance;
    private String username;
    private Timestamp date;
    private int numberOfPosts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    private List<Post> posts;

    private User() {
        this.posts = new ArrayList<>();
    }

    static {
        instance = new User();
    }

    public static User getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }
}
