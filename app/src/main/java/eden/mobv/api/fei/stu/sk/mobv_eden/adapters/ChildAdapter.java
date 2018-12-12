package eden.mobv.api.fei.stu.sk.mobv_eden.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.resources.Post;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<Post> children;
    private Context mainContenxt;

    ChildAdapter(List<Post> children, Context mainContenxt) {
        this.children = children;
        this.mainContenxt = mainContenxt;
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
        Post child = children.get(position);
//        holder.imageView.setImageResource();
        Glide.with(mainContenxt)
                .load(child.getImageUrl())
                .into(holder.imageView);
//        holder.textView.setText(child.title);
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        TextView textView;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.child_textView);
            imageView = itemView.findViewById(R.id.child_imageView);
        }
    }
}
