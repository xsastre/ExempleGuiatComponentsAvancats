package docencia.xaviersastre.taskmanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

import docencia.xaviersastre.taskmanager.DetailActivity;
import docencia.xaviersastre.taskmanager.R;
import docencia.xaviersastre.taskmanager.adapter.TaskAdapter;
import docencia.xaviersastre.taskmanager.model.Task;

public class TaskFragment extends Fragment implements TaskAdapter.OnTaskClickListener {
    private TaskAdapter adapter;
    private List<Task> taskList = new ArrayList<>();
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Task updated = (Task) result.getData().getSerializableExtra("updated_task");
                    int pos = result.getData().getIntExtra("position", -1);
                    if (pos != -1 && updated != null) {
                        adapter.updateTask(pos, updated);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        if (taskList.isEmpty()) {
            taskList.add(new Task(1, "Comprar llet", "2L", "HIGH"));
            taskList.add(new Task(2, "Estudiar Android", "Capítol 5", "MED"));
            taskList.add(new Task(3, "Fer esport", "Correr 30min", "LOW"));
        }

        adapter = new TaskAdapter(taskList, this);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onTaskClick(Task task) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("task", task);
        intent.putExtra("position", taskList.indexOf(task));
        detailLauncher.launch(intent);
    }

    @Override
    public void onTaskLongClick(Task task, int pos) {
        task.setCompleted(!task.isCompleted());
        adapter.updateTask(pos, task);
        Intent br = new Intent("com.example.TASK_COMPLETED");
        br.putExtra("task_title", task.getTitle());
        if (getContext() != null) {
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(br);
        }
    }
}
