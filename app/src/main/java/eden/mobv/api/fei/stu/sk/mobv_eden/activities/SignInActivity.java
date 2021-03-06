package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.FirestoreDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.EmailBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
//                startActivity(SignInActivity.createIntent(this, response));
                if (response.isNewUser()) {
                    addNewUserWithPosts();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
//                  Toast.makeText(getBaseContext(), "Signing in was interrupted by user", Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    showSnackbar(R.string.no_internet_connection);
                    Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_LONG).show();
                    return;
                }
                Crashlytics.logException(response.getError());
//                showSnackbar(R.string.unknown_error);
            }
        }
    }

    protected void addNewUserWithPosts() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        eden.mobv.api.fei.stu.sk.mobv_eden.resources.User currentUser = eden.mobv.api.fei.stu.sk.mobv_eden.resources.User.getInstance();
        Map<String, Object> user = new HashMap<>();
        if (firebaseUser.getDisplayName() != null) {
            user.put("username", firebaseUser.getDisplayName());
        } else {
            user.put("username", firebaseUser.getEmail());
        }

        user.put("date", new Timestamp(new java.util.Date()));
        user.put("numberOfPosts", 0);

        currentUser.setUsername(user.get("username").toString());
        currentUser.setDate((Timestamp) user.get("date"));
        currentUser.setNumberOfPosts(0);

        FirestoreDatabase fd = new FirestoreDatabase();
        fd.addUser(user);

    }



}
