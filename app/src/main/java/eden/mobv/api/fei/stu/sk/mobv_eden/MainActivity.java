package eden.mobv.api.fei.stu.sk.mobv_eden;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String mail = "adamko@mobv.sk";
        String heslo = "heslo123";

        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(mail, heslo);
    }
}
