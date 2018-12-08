package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirestoreDatabase {

    private FirebaseFirestore database;

    public FirestoreDatabase() {
        database = FirebaseFirestore.getInstance();
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
    private void updateUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        User user = User.getInstance();
        int newNumberOfPosts = user.getNumberOfPosts() + 1;

        Map<String, Object> updatePosts = new HashMap<>();
        updatePosts.put("numberOfPosts", newNumberOfPosts);
        this.database.collection("users")
                .document(firebaseUser.getUid())
                .update(updatePosts);
    }
}
