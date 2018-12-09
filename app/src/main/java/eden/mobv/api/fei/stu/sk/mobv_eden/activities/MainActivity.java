package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.PostAdapter;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.UploadMediaButton;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.UploadMediaTask;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.User;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Post> posts;
    private static final int MEDIA_PICKER_SELECT = 1;
    private static final int fileSizeAllowed = 8192; // 8 MB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
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

        // BUTTON
        ConstraintLayout cl = findViewById(R.id.main_content);
        UploadMediaButton uploadButton = new UploadMediaButton(this, cl, MEDIA_PICKER_SELECT);
        uploadButton.setEverything();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MEDIA_PICKER_SELECT) {
            if (resultCode == RESULT_OK) {

                Uri selectedMediaUri = data.getData();
                String realPathToFile = getRealPath(selectedMediaUri);
                File selectedFile = new File(realPathToFile);
                int fileSize = Integer.parseInt(String.valueOf(selectedFile.length()/1024)); // file size in KB
                // if less than fileSizeAllowed (8 MB)
                if (fileSize <= fileSizeAllowed) {
                    new UploadMediaTask().execute(realPathToFile);
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Súbor je príliš veľký. Maximálna povolená veľkost je 8MB."; // TODO: add to strings
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }

    private String getRealPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

}
