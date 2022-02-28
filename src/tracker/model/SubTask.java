package tracker.model;

public class SubTask extends Task {

    public SubTask(long id, String Name, String Description, Status status) {
        super(id, Name, Description, status);
    }

    public String toString() {
        return String.join(",", taskId + "", Type.SUBTASK + "", Name, status + "", Description);
    }

}
