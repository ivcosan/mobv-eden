package eden.mobv.api.fei.stu.sk.mobv_eden.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText _email;
    EditText _password;
    Button _signUpButton;
    Button _signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        this._email = findViewById(R.id.input_email);
        this._password = findViewById(R.id.input_password);
        this._signUpButton = findViewById(R.id.link_signup);
        this._signInButton = findViewById(R.id.btn_login);

        _signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void signup() {
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        auth = FirebaseAuth.getInstance();
        try {
            this.auth.signInWithEmailAndPassword(email, password);
            Toast.makeText(getBaseContext(), "Login success", Toast.LENGTH_LONG).show();
        }
        catch(Exception e) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        }
    }
}
