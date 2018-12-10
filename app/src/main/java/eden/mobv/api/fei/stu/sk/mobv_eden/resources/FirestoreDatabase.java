package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eden.mobv.api.fei.stu.sk.mobv_eden.activities.MainActivity;

public class FirestoreDatabase {
    public interface FirestoreDatabaseListener {
        public void onDataLoaded();
    }

    private FirebaseFirestore database;
    private FirestoreDatabaseListener listener;

    public FirestoreDatabase() {
        this.listener = null;
        database = FirebaseFirestore.getInstance();
    }

    public void setFirestoreDatabaseListener(FirestoreDatabaseListener listener) {
        this.listener = listener;
    }

    public void addPost(String mediaType, String mediaUrl, String userId, String username) {
        Map< String, Object > newPost = new HashMap< >();

        DocumentReference usersRef = database.collection("users").document(userId);

        newPost.put("type", mediaType);
        if (mediaType.equals("image")) {
            newPost.put("videoUrl", "");
            newPost.put("imageUrl", mediaUrl);
        }
        else {
            newPost.put("videoUrl", mediaUrl);
            newPost.put("imageUrl", "");
        }
        newPost.put("username", username);
        newPost.put("date", new Date());

        newPost.put("userId", usersRef);

        database.collection("posts")
                .add(newPost)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        updateUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                    }
                });

    }

    public void addUser(Map<String, Object> user) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        this.database.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getBaseContext(), "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        Crashlytics.logException(e);
                    }
                });
    }


    // TODO: treba overit funkcnost
    public void updateUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        User user = User.getInstance();
        int newNumberOfPosts = user.getNumberOfPosts() + 1;
        Log.i("macko", User.getInstance().getUsername());

        Map<String, Object> updatePosts = new HashMap<>();
        updatePosts.put("numberOfPosts", newNumberOfPosts);
        this.database.collection("users")
                .document(firebaseUser.getUid())
                .update(updatePosts);
    }

    public void getDataFromUserDocument() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final DocumentReference docRef = this.database.collection("users").document(auth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        User.getInstance().setUsername(data.get("username").toString());
                        User.getInstance().setNumberOfPosts(((Long)data.get("numberOfPosts")).intValue());
                        if (listener != null) {
                            listener.onDataLoaded();
                        }
                    } else {
                        Crashlytics.log("User document -> no such document");
                    }
                } else {
                    Crashlytics.logException(task.getException());
                }
            }
        });
    }
}
