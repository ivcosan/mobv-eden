package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import eden.mobv.api.fei.stu.sk.mobv_eden.activities.MainActivity;

public class FirestoreDatabase {
    public interface FirestoreDatabaseListener {
        public void onUserDataLoaded();
        public void onUserPostsLoaded();
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
                            listener.onUserDataLoaded();
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

    public void getPostsByCurrentUser() {
        if (User.getInstance().getUsername() != null) {
            database.collection("posts")
                    .whereEqualTo("username", User.getInstance().getUsername())
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    Log.d("FirestoreDatabase", "No posts by user");
                                }
                                List<Post> userPosts = new ArrayList<>();
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    Post p = new Post();
                                    p.setDate(((Date)doc.get("date")).getTime());
                                    p.setImageUrl(doc.getString("imageUrl"));
                                    p.setVideoUrl(doc.getString("videoUrl"));
                                    p.setPostType(doc.getString("type"));
                                    userPosts.add(p);
                                }
                                User.getInstance().setPosts(userPosts);
                                if (listener != null) {
                                    listener.onUserPostsLoaded();
                                }
                            } else {
                                Crashlytics.logException(task.getException());
                            }
                        }
                    });
        }
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
//                Collections.sort(allPosts, new Comparator<Post>() {
//                    @Override
//                    public int compare(Post o1, Post o2) {
//                        return o1.getDate() > o2.getDate() ? -1 : o1.getDate() < o2.getDate() ? 1 : 0;
//                    }
//                });
//                for (int i = 0; i < allPosts.size(); i++) {
//                    System.out.println(new Date(allPosts.get(i).getDate()));
//                }

//                System.out.println("****");

//                for (Map.Entry<String, List<Post>> entry : posts.entrySet()) {
//                    Collections.sort(entry.getValue(), new Comparator<Post>() {
//                        @Override
//                        public int compare(Post o1, Post o2) {
//                            return o1.getDate() > o2.getDate() ? -1 : o1.getDate() < o2.getDate() ? 1 : 0;
//                        }
//                    });
//                }

//                for (Map.Entry<String, List<Post>> entry : posts.entrySet()) {
//                    for (int i = 0; i < entry.getValue().size(); i++) {
//                        System.out.println(new Date(entry.getValue().get(i).getDate()));
//                    }
//                    System.out.println("******");
//                }
            }
        });
    }


}
