package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsSingleton {
    private static PostsSingleton instance;

    private List<Post> allPosts;
    private Map<String, List<Post>> postsByUser;

    private PostsSingleton() {
        this.allPosts = new ArrayList<>();
        this.postsByUser = new HashMap<>();
    }

    public static PostsSingleton getInstance() {
        return instance;
    }

    static {
        instance = new PostsSingleton();
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    public void setAllPosts(List<Post> allPosts) {
        this.allPosts = allPosts;
    }

    public Map<String, List<Post>> getPostsByUser() {
        return postsByUser;
    }

    public void setPostsByUser(Map<String, List<Post>> latestPosts) {
        this.postsByUser = latestPosts;
    }

    public List<Post> getPostsByUsername(String username){
        return postsByUser.get(username);
    }
}
