package tracker.model;

public class SubTask extends Task {
    long epicId;

    public SubTask(long id, String Name, String Description, Status status) {
        super(id, Name, Description, status);
    }

    public Long getEpicId() {
        return epicId;
    }
}
