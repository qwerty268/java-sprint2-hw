package tracker.model;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");

    protected Status status;
    protected String Name;
    protected String Description;
    protected Long taskId;
    protected Duration duration;
    protected LocalDate localDate;

    public Task(long id, String Name, String Description, Status status, Duration duration, LocalDate localDate) {
        this.taskId = id;
        this.Name = Name;
        this.Description = Description;
        this.status = status;
        this.duration = duration;
        this.localDate = localDate;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    @Override
    public String toString() {
        return String.join(",", taskId + "", Type.TASK + "", Name, status + "", Description,
                duration.getSeconds() + "", localDate.format(dateTimeFormatter));
    }
}
