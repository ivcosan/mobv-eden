package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.support.annotation.NonNull;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;

import javax.annotation.Nullable;
import java.util.*;

public class FirestoreDatabase {
    public interface FirestoreDatabaseListener {
//        public void onUserPostsLoaded();
        public void onProfilesLoaded();
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

    public void setUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        User.getInstance().setUsername(firebaseUser.getDisplayName());
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

        Map<String, Object> updatePosts = new HashMap<>();
        updatePosts.put("numberOfPosts", newNumberOfPosts);
        this.database.collection("users")
                .document(firebaseUser.getUid())
                .update(updatePosts);
    }

    public void getPostsByAllUsers() {
        database.collection("posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("firestoredatabase", "Listen failed.", e);
                    return;
                }

                Map<String, List<Post>> posts = new HashMap<>();
                List<Post> allPosts = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("username") != null) {
                        Post p = new Post();
                        p.setDate(((Date)doc.get("date")).getTime());
                        p.setImageUrl(doc.getString("imageUrl"));
                        p.setVideoUrl(doc.getString("videoUrl"));
                        p.setPostType(doc.getString("type"));
                        p.setUsername(doc.getString("username"));
                        // TODO add video support
                        if(p.getPostType().equals("video")){
                            continue;
                        }
                        allPosts.add(p);
                        if (posts.containsKey(doc.getString("username"))) {
                            posts.get(doc.getString("username")).add(p);
                        } else {
                            List<Post> tmpList = new ArrayList<>();
                            tmpList.add(p);
                            posts.put(doc.getString("username"), tmpList);
                        }
                    }
                }
                Log.d("firestoredatabase", "Data from database: " + posts);
//                User.getInstance().setPosts(posts.get(User.getInstance().getUsername()));
                PostsSingleton.getInstance().setAllPosts(allPosts);
                PostsSingleton.getInstance().setPostsByUser(posts);
                if (listener != null) {
                    listener.onProfilesLoaded();
                }
            }
        });
    }

    public void getAllProfiles() {
        this.database.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, UserProfile> userProfiles = new HashMap<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                UserProfile up = new UserProfile();
                                up.setNumberOfPosts(((Long)doc.get("numberOfPosts")).intValue());
                                up.setUsername(doc.get("username").toString());
                                up.setRegistrationDate(new Date(((Date)doc.get("date")).getTime()));
                                Log.d("userProfiles", doc.getId() + " => " + doc.getData());
                                userProfiles.put(doc.get("username").toString(), up);
                            }
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            if (auth != null && userProfiles != null) {
                                User.getInstance().setNumberOfPosts(userProfiles.get(auth.getCurrentUser().getDisplayName()).getNumberOfPosts());
                            }
                            PostsSingleton.getInstance().setProfileByUser(userProfiles);
                            if(listener != null) {
                                listener.onProfilesLoaded();
                            }
                        } else {
                            Crashlytics.logException(task.getException());
                        }
                    }
                });
    }

}
