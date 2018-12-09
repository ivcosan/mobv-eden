package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.Utils.PostFactory;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.ParentAdapter;

public class MainActivity extends AppCompatActivity {

//    private ArrayList<ChildPost> posts;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();

//        RecyclerView rvPosts = findViewById(R.id.rvPosts);
//        posts = ChildPost.createPostsList(50);
//        PostAdapter adapter = new PostAdapter(posts);
//        rvPosts.setAdapter(adapter);
//        rvPosts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//
//        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
//        rvPosts.addItemDecoration(decoration);
//
//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(rvPosts);
    }

    private void initRecycler() {
        recyclerView = findViewById(R.id.rvParent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ParentAdapter(PostFactory.getRandomParents()));
    }
}
