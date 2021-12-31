package tracker.model;

public class Task {
    protected String Status;
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

    public String getStatus(){
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
