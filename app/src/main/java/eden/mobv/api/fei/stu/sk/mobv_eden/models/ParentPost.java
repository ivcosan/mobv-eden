package eden.mobv.api.fei.stu.sk.mobv_eden.models;

import java.util.List;

public class ParentPost {

    public String title;
    public List<ChildPost> children;

    public ParentPost(String title) {
        this.title = title;
    }

    public ParentPost(String title, List<ChildPost> children) {
        this.title = title;
        this.children = children;
    }
}
