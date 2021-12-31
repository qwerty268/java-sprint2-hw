package tracker.model;

public class SubTask extends Task {
    long epicId;

    public SubTask(long id) {
        super(id);
    }

    public Long getEpicId() {
        return epicId;
    }
}
