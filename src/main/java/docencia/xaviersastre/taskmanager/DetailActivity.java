package docencia.xaviersastre.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import docencia.xaviersastre.taskmanager.model.Task;

public class DetailActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Spinner spinnerPriority;
    private Task task;
    private int position;

    private static final String[] PRIORITIES = {"HIGH", "MED", "LOW"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);

        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, PRIORITIES);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(priorityAdapter);

        task = (Task) getIntent().getSerializableExtra("task");
        position = getIntent().getIntExtra("position", -1);

        if (task != null) {
            etTitle.setText(task.getTitle());
            etDescription.setText(task.getDescription());
            for (int i = 0; i < PRIORITIES.length; i++) {
                if (PRIORITIES[i].equals(task.getPriority())) {
                    spinnerPriority.setSelection(i);
                    break;
                }
            }
            setTitle("Editar tasca");
        } else {
            setTitle("Nova tasca");
            btnDelete.setEnabled(false);
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String priority = (String) spinnerPriority.getSelectedItem();

            if (title.isEmpty()) {
                etTitle.setError("El títol és obligatori");
                return;
            }

            Task result;
            if (task != null) {
                task.setTitle(title);
                task.setDescription(desc);
                task.setPriority(priority);
                result = task;
            } else {
                result = new Task(0, title, desc, priority);
            }

            Intent data = new Intent();
            data.putExtra("updated_task", result);
            data.putExtra("position", position);
            setResult(RESULT_OK, data);
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra("position", position);
            data.putExtra("deleted", true);
            setResult(RESULT_OK, data);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
