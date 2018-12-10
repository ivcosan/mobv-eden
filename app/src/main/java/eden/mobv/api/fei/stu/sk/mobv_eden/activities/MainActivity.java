package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.PostAdapter;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.FirestoreDatabase;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.i("MainAcitivyTRUE", "NOVY USER");
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        } else {
            Log.i("MainAcitivyFALSE", "nie je nacitany user");
            final FirestoreDatabase fd = new FirestoreDatabase();
            fd.getDataFromUserDocument();
            fd.setFirestoreDatabaseListener(new FirestoreDatabase.FirestoreDatabaseListener() {
                @Override
                public void onDataLoaded() {
                    Toast.makeText(getBaseContext(), User.getInstance().getUsername(), Toast.LENGTH_LONG).show();
                }
            });
        }

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
