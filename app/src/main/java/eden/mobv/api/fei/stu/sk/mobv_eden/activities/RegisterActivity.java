package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseAuth;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private static final String TAG = "SignupActivity";

//    EditText _userName;
    EditText _email;
    EditText _password;
    Button _signUpButton;
    Button _signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        this._userName = findViewById(R.id.input_username);
        this._email = findViewById(R.id.input_email);
        this._password = findViewById(R.id.input_password);
        this._signUpButton = findViewById(R.id.link_signup);
        this._signInButton = findViewById(R.id.link_signin);

        _signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signUpButton.setEnabled(false);
        onSignupSuccess();

        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

//        String name = _userName.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();

        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signUpButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Login sucess", Toast.LENGTH_LONG).show();
        _signUpButton.setEnabled(true);
//        setResult(RESULT_OK, null);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

//        String name = _userName.getText().toString();
        String email = _email.getText().toString();
        String password = _password.getText().toString();

//        if (name.isEmpty() || name.length() < 3) {
//            _userName.setError("Používateľské meno musí mať aspoň 3 znaky");
//            valid = false;
//        } else {
//            _userName.setError(null);
//        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _email.setError("Zadajte platnú emailovú adresu");
            valid = false;
        } else {
            _email.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            _password.setError("Heslo sa musí mať aspoň znakov");
            valid = false;
        } else {
            _password.setError(null);
        }

        return valid;
    }
}
