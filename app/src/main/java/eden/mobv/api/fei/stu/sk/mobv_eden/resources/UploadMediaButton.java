package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextThemeWrapper;
import android.view.View;

import eden.mobv.api.fei.stu.sk.mobv_eden.R;

public class UploadMediaButton {

    private Activity activity;
    private ConstraintLayout constraintLayout;
    private FloatingActionButton floatingActionButton;
    private int MEDIA_PICKER_SELECT;

    // input: Activity where is button placed, ConstraintLayout of activity, variable to determine if file was picked
    public UploadMediaButton(Activity _activity, ConstraintLayout _constraintLayout, int _MEDIA_PICKER_SELECT) {
        activity = _activity;
        constraintLayout = _constraintLayout;
        MEDIA_PICKER_SELECT = _MEDIA_PICKER_SELECT;

        // create button
        ContextThemeWrapper ctw = new ContextThemeWrapper(activity, R.style.AppTheme);
        floatingActionButton = new FloatingActionButton(ctw);
    }

    // set xml layout style for button in current activity
    //@SuppressLint("ResourceType")
    private void setDefaultStyle() {
        floatingActionButton.setImageResource(R.drawable.ic_plus_button);
        floatingActionButton.setId(View.generateViewId());
        constraintLayout.addView(floatingActionButton); // add button to layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(floatingActionButton.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM,36);
        constraintSet.connect(floatingActionButton.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END,40);
        constraintSet.constrainHeight(floatingActionButton.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(floatingActionButton.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.applyTo(constraintLayout);
    }

    // set on click listener to pick video/image from gallery
    private void setMediaPickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the click.
                Intent pickMediaIntent = new Intent();
                pickMediaIntent.setType("*/*");
                pickMediaIntent.setAction(Intent.ACTION_GET_CONTENT);
                pickMediaIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "video/mp4"});
                activity.startActivityForResult(pickMediaIntent, MEDIA_PICKER_SELECT);
            }
        });
    }

    public void setEverything() {
        setDefaultStyle();
        setMediaPickListener();
    }
}
