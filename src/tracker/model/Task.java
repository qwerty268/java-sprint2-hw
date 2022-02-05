package tracker.model;

public class Task {
    protected Status status;
    protected String Name;
    protected String Description;
    protected Long taskId;

    public Task(long id, String Name, String Description, Status status) {
        this.taskId = id;
        this.Name = Name;
        this.Description = Description;
        this.status = status;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "status=" + status +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}
