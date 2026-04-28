package docencia.xaviersastre.taskmanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import docencia.xaviersastre.taskmanager.R;
import docencia.xaviersastre.taskmanager.model.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<docencia.xaviersastre.taskmanager.adapter.TaskAdapter.ViewHolder> {
    private List<Task> tasks;
    private docencia.xaviersastre.taskmanager.adapter.TaskAdapter.OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
        void onTaskLongClick(Task task, int pos);
    }

    public TaskAdapter(List<Task> tasks, docencia.xaviersastre.taskmanager.adapter.TaskAdapter.OnTaskClickListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvPriority;
        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvPriority = v.findViewById(R.id.tvPriority);
        }
    }

    @NonNull
    @Override
    public docencia.xaviersastre.taskmanager.adapter.TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new docencia.xaviersastre.taskmanager.adapter.TaskAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull docencia.xaviersastre.taskmanager.adapter.TaskAdapter.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.tvPriority.setText("Prioritat: " + task.getPriority());

        // Clic simple -> obrir detall
        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task));

        // Clic llarg -> marcar completada
        holder.itemView.setOnLongClickListener(v -> {
            listener.onTaskLongClick(task, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTask(int pos, Task t) {
        tasks.set(pos, t);
        notifyItemChanged(pos);
    }

    public void addTask(Task t) {
        tasks.add(t);
        notifyItemInserted(tasks.size() - 1);
    }

    public void removeTask(int pos) {
        tasks.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, tasks.size());
    }
}
