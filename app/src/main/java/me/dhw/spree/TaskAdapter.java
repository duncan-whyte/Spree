package me.dhw.spree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<String> tasks;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;


    public TaskAdapter(Context context, List<String> tasks) {
        this.inflater = LayoutInflater.from(context);
        this.tasks = tasks;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.taskitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String task = tasks.get(position);
        holder.textView.setText(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        Button doneButton;
        Button cancelButton;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.taskName);
            doneButton = itemView.findViewById(R.id.doneButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            //itemView.setOnClickListener(this);
            doneButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, this, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, ViewHolder viewHolder, int position);
    }

    String getItem(int id) {
        return tasks.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
