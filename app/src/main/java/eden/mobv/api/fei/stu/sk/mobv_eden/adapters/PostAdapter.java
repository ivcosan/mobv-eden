package eden.mobv.api.fei.stu.sk.mobv_eden.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private List<Post> mPosts;

    public PostAdapter(List<Post> posts) {
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // involves inflating a layout from XML and returning the holder
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Involves populating data into the item through holder
        Post post = mPosts.get(position);

        // naplnenie kazdeho zaznamu
        TextView textView = viewHolder.postNameTextView;
        textView.setText(post.getData());
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView postNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postNameTextView = itemView.findViewById(R.id.post_name);
        }
    }

}
