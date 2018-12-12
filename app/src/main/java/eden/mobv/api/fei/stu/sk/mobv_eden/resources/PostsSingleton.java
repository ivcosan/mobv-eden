package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsSingleton {
    private static PostsSingleton instance;
    private List<Post> allPosts;
    private Map<String, Post> postsByUser;

    private PostsSingleton() {
        this.allPosts = new ArrayList<>();
        this.postsByUser = new HashMap<>();
    }

    static {
        instance = new PostsSingleton();
    }

    public static PostsSingleton getInstance() {
        return instance;
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<Post> allPosts) {
        this.allPosts = allPosts;
    }

    public Map<String, Post> getLatestPosts() {
        return postsByUser;
    }

    public void setLatestPosts(Map<String, Post> latestPosts) {
        this.postsByUser = latestPosts;
    }
}
