package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostsSingleton {
    private static PostsSingleton instance;

    private List<Post> allPosts;
    private Map<String, List<Post>> postsByUser;

    public Map<String, UserProfile> getProfileByUser() {
        return profileByUser;
    }

    public UserProfile getProfileByUsername(String username){
        return profileByUser.get(username);
    }

    public void setProfileByUser(Map<String, UserProfile> profileByUser) {
        this.profileByUser = profileByUser;
    }

    private Map<String, UserProfile> profileByUser;

    private PostsSingleton() {
        this.allPosts = new ArrayList<>();
        this.postsByUser = new HashMap<>();
        this.profileByUser = new HashMap<>();
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

    public int getCurrentIndex(String username, long millis){
        if(postsByUser.containsKey(username)){
            for(int i = 0; i < postsByUser.get(username).size(); i++){
                if(postsByUser.get(username).get(i).getDate() == millis){
                    return i;
                }
            }
        }
        return 0;
    }
}
