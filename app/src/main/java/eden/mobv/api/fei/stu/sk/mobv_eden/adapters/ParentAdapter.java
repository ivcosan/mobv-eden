package eden.mobv.api.fei.stu.sk.mobv_eden.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import eden.mobv.api.fei.stu.sk.mobv_eden.R;
import eden.mobv.api.fei.stu.sk.mobv_eden.models.ParentPost;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ViewHolder> {

    private RecyclerView.RecycledViewPool viewPool =  new RecyclerView.RecycledViewPool();
    private List<ParentPost> parents;

    public ParentAdapter(List<ParentPost> parents) {
        this.parents = parents;
    }

    @NonNull
    @Override
    public ParentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.parent_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentAdapter.ViewHolder holder, int position) {
        ParentPost parent = parents.get(position);
        holder.textView.setText(parent.title);
        RecyclerView.LayoutManager childLayoutManager = new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        ((LinearLayoutManager) childLayoutManager).setInitialPrefetchItemCount(4);
        holder.recyclerView.setLayoutManager(childLayoutManager);
        holder.recyclerView.setAdapter(new ChildAdapter(parent.children));
        holder.recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_child);
            RecyclerView.ItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            recyclerView.addItemDecoration(horizontalDecoration);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
