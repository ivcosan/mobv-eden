package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import java.util.ArrayList;

public class Post {
    private boolean isImage;
    private String data;

    public Post(String data) {
        this.isImage = true;
        this.data = data;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static ArrayList<Post> createPostsList(int length) {
        ArrayList<Post> result = new ArrayList<>();
        for(int i = 0; i < length; i++){
            result.add(new Post("Post " + i));
        }
        return result;
    }
}
