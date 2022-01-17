package tracker.model;

public class Task {
    protected Status status;
    protected String Name;
    protected String Description;
    protected Long taskId;

    public Task(long id) {
        this.taskId = id;
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
}
