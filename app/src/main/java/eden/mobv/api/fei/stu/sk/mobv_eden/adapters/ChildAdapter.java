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
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ChildPost;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<ChildPost> children;
    private Context context;
    private Context mainContext;

    public ChildAdapter(List<ChildPost> children, Context mainContext) {
        this.children = children;
        this.mainContext = mainContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.child_recycler, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChildPost child = children.get(position);
        //Loading image from below url into imageView
        Glide.with(mainContext)
                .load(child.image)
                .into(holder.imageView);
        //holder.imageView.setImageResource(child.image);
        holder.textView.setText(child.title);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView2;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPostImage);
            textView = itemView.findViewById(R.id.textViewUserName);
            textView2 = itemView.findViewById(R.id.textViewPublicationDate);
        }
    }
}
