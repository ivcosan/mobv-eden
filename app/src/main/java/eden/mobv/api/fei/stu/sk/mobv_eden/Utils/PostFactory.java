package eden.mobv.api.fei.stu.sk.mobv_eden.Utils;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ChildPost;
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ParentPost;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PostFactory {

    public static List<ParentPost> getRandomParents() {
        return IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new ParentPost("Parent title: "+i, getRandomChildren()))
                .collect(Collectors.toList());
    }

    public static List<ChildPost> getRandomChildren() {
        return IntStream.rangeClosed(0, 9)
                .mapToObj(i -> new ChildPost("Child title: "+i, R.mipmap.letter_i))
                .collect(Collectors.toList());
    }

}
