package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
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
    }

    private void initRecycler() {
        RecyclerView.ItemDecoration verticalDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView = findViewById(R.id.rvParent);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ParentAdapter(PostFactory.getRandomParents()));
    }
}
