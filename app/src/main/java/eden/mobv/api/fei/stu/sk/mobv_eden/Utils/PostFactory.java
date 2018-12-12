package eden.mobv.api.fei.stu.sk.mobv_eden.Utils;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ChildPost;
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ParentPost;

import java.util.ArrayList;
import java.util.List;

public class PostFactory {

    public static List<ParentPost> getRandomParents() {
        List<ParentPost> result = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            result.add(new ParentPost("Parent title: "+i, getRandomChildren()));
        }
        return result;
    }

    public static List<ChildPost> getRandomChildren() {
        List<ChildPost> result = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            result.add(new ChildPost("Child title: "+i, "https://cf5.s3.souqcdn.com/item/2017/09/06/23/99/04/98/item_L_23990498_34952250.jpg"));
        }
        return result;
    }

}
