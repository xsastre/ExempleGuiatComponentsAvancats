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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import docencia.xaviersastre.taskmanager.DetailActivity;
import docencia.xaviersastre.taskmanager.R;
import docencia.xaviersastre.taskmanager.adapter.TaskAdapter;
import docencia.xaviersastre.taskmanager.database.DatabaseHelper;
import docencia.xaviersastre.taskmanager.model.Task;

public class TaskFragment extends Fragment implements TaskAdapter.OnTaskClickListener {
    private TaskAdapter adapter;
    private List<Task> taskList = new ArrayList<>();
    private DatabaseHelper dbHelper;

    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    int pos = data.getIntExtra("position", -1);
                    boolean deleted = data.getBooleanExtra("deleted", false);

                    if (deleted && pos >= 0 && pos < taskList.size()) {
                        Task taskToDelete = taskList.get(pos);
                        dbHelper.deleteTask(taskToDelete.getId());
                        adapter.removeTask(pos);
                    } else {
                        Task updated = (Task) data.getSerializableExtra("updated_task");
                        if (updated != null) {
                            if (pos == -1) {
                                long id = dbHelper.addTask(updated);
                                updated = new Task((int) id, updated.getTitle(),
                                        updated.getDescription(), updated.getPriority());
                                adapter.addTask(updated);
                            } else {
                                dbHelper.updateTask(updated);
                                adapter.updateTask(pos, updated);
                            }
                        }
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_task, container, false);

        if (getContext() != null) {
            dbHelper = new DatabaseHelper(getContext());
            taskList = dbHelper.getAllTasks();
        }

        if (taskList.isEmpty()) {
            Task t1 = new Task(0, "Comprar llet", "2L", "HIGH");
            Task t2 = new Task(0, "Estudiar Android", "Capítol 5", "MED");
            Task t3 = new Task(0, "Fer esport", "Correr 30min", "LOW");
            t1 = new Task((int) dbHelper.addTask(t1), t1.getTitle(), t1.getDescription(), t1.getPriority());
            t2 = new Task((int) dbHelper.addTask(t2), t2.getTitle(), t2.getDescription(), t2.getPriority());
            t3 = new Task((int) dbHelper.addTask(t3), t3.getTitle(), t3.getDescription(), t3.getPriority());
            taskList.add(t1);
            taskList.add(t2);
            taskList.add(t3);
        }

        adapter = new TaskAdapter(taskList, this);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = v.findViewById(R.id.fabAddTask);
        fab.setOnClickListener(btn -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("position", -1);
            detailLauncher.launch(intent);
        });

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
        dbHelper.updateTask(task);
        adapter.updateTask(pos, task);
        Intent br = new Intent("com.example.TASK_COMPLETED");
        br.putExtra("task_title", task.getTitle());
        if (getContext() != null) {
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(br);
        }
    }
}
