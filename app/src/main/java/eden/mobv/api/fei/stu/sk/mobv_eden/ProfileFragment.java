package eden.mobv.api.fei.stu.sk.mobv_eden;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private String userName;
    private String registrationDate;
    private Integer numberOfPosts;

    private TextView textViewUserName;
    private TextView textViewRegistrationDate;
    private TextView textViewPostsNumber;

    public ProfileFragment newInstance(String userName, String registrationDate, Integer numberOfPosts) {
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putString("registrationDate", registrationDate);
        bundle.putInt("numberOfPosts", numberOfPosts);

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            userName = bundle.getString("userName");
            registrationDate = bundle.getString("registrationDate");
            numberOfPosts = bundle.getInt("numberOfPosts");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUserName = (TextView) view.findViewById(R.id.textViewUserName);
        textViewRegistrationDate = (TextView) view.findViewById(R.id.textViewRegistrationDate);
        textViewPostsNumber = (TextView) view.findViewById(R.id.textViewPostsNumber);

        readBundle(getArguments());

        textViewUserName.setText(userName);
        textViewRegistrationDate.setText(registrationDate);
        textViewPostsNumber.setText(numberOfPosts);

        return view;
    }
}
