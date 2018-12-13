package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.Utils.RealPathUtil;
import eden.mobv.api.fei.stu.sk.mobv_eden.adapters.ParentAdapter;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.*;
import com.firebase.ui.auth.AuthUI;

import java.io.File;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    FirestoreDatabase fd = null;
    private static final int MEDIA_PICKER_SELECT = 1;
    private static final int fileSizeAllowed = 8192; // 8 MB
    private static int dbOperationsFinished = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fd = new FirestoreDatabase();
        fd.getAllProfiles();
        fd.getPostsByAllUsers();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Log.i("MainAcitivyTRUE", "NOVY USER");
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
        }
        fd.setFirestoreDatabaseListener( new FirestoreDatabase.FirestoreDatabaseListener() {
//            @Override
//            public void onUserPostsLoaded() {
//                System.out.println("posts natiahnute");
//            }

            @Override
            public void onProfilesLoaded() {
                dbOperationsFinished++;
                System.out.println("user profiles natiahnute");
                if(dbOperationsFinished == 2){
                        initRecycler();
                }
            }
        });
    }

    private void initRecycler() {
        dbOperationsFinished = 0;
        RecyclerView.ItemDecoration verticalDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        SnapHelper snapHelper = new LinearSnapHelper();
        recyclerView = findViewById(R.id.rvParent);
        recyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(verticalDecoration);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(new ParentAdapter(PostFactory.getRandomParents()));
        recyclerView.setAdapter(new ParentAdapter(PostsSingleton.getInstance().getAllPosts(), getApplication()));
        snapHelper.attachToRecyclerView(recyclerView);

        // BUTTON
        ConstraintLayout cl = findViewById(R.id.main_content);
        UploadMediaButton uploadButton = new UploadMediaButton(this, cl, MEDIA_PICKER_SELECT);
        uploadButton.setEverything();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
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

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });
    }


}
