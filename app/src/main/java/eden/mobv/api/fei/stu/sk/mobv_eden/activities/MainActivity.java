package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.Utils.PostFactory;
import eden.mobv.api.fei.stu.sk.mobv_eden.Utils.RealPathUtil;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.ParentAdapter;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.FirestoreDatabase;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.UploadMediaButton;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.UploadMediaTask;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.User;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    private static final int MEDIA_PICKER_SELECT = 1;
    private static final int fileSizeAllowed = 8192; // 8 MB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycler();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.i("MainAcitivyTRUE", "NOVY USER");
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        } else {
            Log.i("MainAcitivyFALSE", "nie je nacitany user");
            final FirestoreDatabase fd = new FirestoreDatabase();
            fd.getPostsByAllUsers();
            fd.getDataFromUserDocument();
            fd.setFirestoreDatabaseListener(new FirestoreDatabase.FirestoreDatabaseListener() {
                @Override
                public void onUserDataLoaded() {
                    Toast.makeText(getBaseContext(), User.getInstance().getUsername(), Toast.LENGTH_LONG).show();
                    fd.getPostsByCurrentUser();
                }

                @Override
                public void onUserPostsLoaded(QuerySnapshot document) {
                    String s = "";
                    ConstraintLayout cl = findViewById(R.id.main_content);
                    for (QueryDocumentSnapshot d : document) {
                        Log.d("FirestoreDatabaseMacko", d.getId() + " => " + d.getData());
                        s = s + d.getData().toString() + "\n";
                    }
                    TextView tv = new TextView(MainActivity.this);
                    cl.addView(tv);
                    tv.setText(s);
                }
            });
        }
    }

    private void initRecycler() {
        RecyclerView.ItemDecoration verticalDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView = findViewById(R.id.rvParent);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ParentAdapter(PostFactory.getRandomParents()));
        snapHelper.attachToRecyclerView(recyclerView);

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

                try {
                    Uri selectedMediaUri = data.getData();
                    String realPathToFile = RealPathUtil.getRealPath(getApplicationContext(), selectedMediaUri);//selectedMediaUri.getPath();
                    File selectedFile = new File(realPathToFile);//create path from uri
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
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toast.makeText(getBaseContext(), "Chyba pri ziskavani cesty k suboru.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
