package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.PostAdapter;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvPosts = findViewById(R.id.rvPosts);
        posts = Post.createPostsList(50);
        PostAdapter adapter = new PostAdapter(posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        rvPosts.addItemDecoration(decoration);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvPosts);
    }
}