package eden.mobv.api.fei.stu.sk.mobv_eden.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.UserProfile;

import java.util.Date;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<Post> children;
    private Context mainContenxt;
    private UserProfile profile;

    ChildAdapter(List<Post> children, Context mainContenxt, UserProfile profile) {
        this.children = children;
        this.mainContenxt = mainContenxt;
        this.profile = profile;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.child_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("Position: "+position);
        if(position == 0){
            // vykreslit profil
            holder.relativeLayout.setVisibility(View.GONE);
            holder.imageInclude.setVisibility(View.GONE);
            holder.profileInclude.setVisibility(View.VISIBLE);

            holder.profileDate.setText(profile.getRegistrationDate().toString());
            holder.profileName.setText(profile.getUsername());
            holder.profileNumPosts.setText(String.valueOf(profile.getNumberOfPosts()));

        } else {
            // vykreslit image/video
            holder.relativeLayout.setVisibility(View.VISIBLE);
            Post child = children.get(position);
            holder.imageInclude.setVisibility(View.VISIBLE);
            holder.profileInclude.setVisibility(View.GONE);
            Glide.with(mainContenxt)
                    .load(child.getImageUrl())
                    .into(holder.imageView);
            holder.imageMeno.setText(child.getUsername());
            holder.imageDatum.setText(String.valueOf(new Date(child.getDate())));

        }

    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        View imageInclude;

        View profileInclude;
        TextView profileDate;
        TextView profileNumPosts;
        TextView profileName;

        View relativeLayout;

        View videoInclude;

        TextView imageMeno;
        TextView imageDatum;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.child_imageView);
            imageInclude = itemView.findViewById(R.id.image_post);

            profileInclude = itemView.findViewById(R.id.profile);
            profileDate = itemView.findViewById(R.id.profile_date);
            profileNumPosts = itemView.findViewById(R.id.profile_num_posts);
            profileName = itemView.findViewById(R.id.profile_name);

            relativeLayout = itemView.findViewById(R.id.relative_layout_id);

            imageMeno = itemView.findViewById(R.id.image_post_meno);
            imageDatum = itemView.findViewById(R.id.image_post_datum);

        }
    }
}
