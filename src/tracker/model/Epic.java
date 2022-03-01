package tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id, String Name, String Description, Status status, ArrayList<SubTask> subTasks) {
        super(id, Name, Description, status);
        this.subTasks = subTasks;
    }

    public void clearSubTasks() {
        subTasks.clear();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public String toString() {
        String numOfSubs = "";

        if (subTasks != null) {
            for (Task task : subTasks) {
                numOfSubs += "," + task.getTaskId();
            }
        }
        if (numOfSubs == "") {
            numOfSubs = ",null";
        }
        return String.join(",", taskId + "", Type.EPIC + "", Name, status + "", Description) +
                numOfSubs;
    }

}
