package eden.mobv.api.fei.stu.sk.mobv_eden;

import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PostFragment extends Fragment {
    private String postImage;
    private String userName;
    private String publicationDate;

    private ImageView imageViewPostImage;
    private TextView textViewUserName;
    private TextView textViewPublicationDate;

    public PostFragment newInstance(String postImage, String userName, String publicationDate) {
        Bundle bundle = new Bundle();
        bundle.putString("postImage", postImage);
        bundle.putString("userName", userName);
        bundle.putString("publicationDate", publicationDate);

        PostFragment fragment = new PostFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            postImage = bundle.getString("postImage");
            userName = bundle.getString("userName");
            publicationDate = bundle.getString("publicationDate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        imageViewPostImage = (ImageView) view.findViewById(R.id.imageViewPostImage);
        textViewUserName = (TextView) view.findViewById(R.id.textViewUserName);
        textViewPublicationDate = (TextView) view.findViewById(R.id.textViewPublicationDate);

        readBundle(getArguments());

        //Loading image from below url into imageView
        Glide.with(this)
                .load(postImage)
                .into(imageViewPostImage);

        textViewUserName.setText(userName);
        textViewPublicationDate.setText(publicationDate);

        return view;
    }
}
