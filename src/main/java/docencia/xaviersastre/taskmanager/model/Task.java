package docencia.xaviersastre.taskmanager.model;
import java.io.Serializable;

public class Task implements Serializable {
    private int id;
    private String title, description, priority;
    private boolean completed;

    public Task(int id, String title, String description, String priority) {
        this.id = id; this.title = title;
        this.description = description; this.priority = priority;
        this.completed = false;
    }
    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}