package tracker.model;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id, String Name, String Description, Status status, ArrayList<SubTask> subTasks) {
        super(id, Name, Description, status);

        Duration newDuration = Duration.ofSeconds(0);
        LocalDate newLocalDate = LocalDate.of(1, 1, 1);

        if (subTasks != null) {
            newLocalDate = subTasks.get(0).getLocalDate();
            for (SubTask sub : subTasks) {
                newDuration = newDuration.plus(sub.getDuration());
                if (newLocalDate.isAfter(sub.getLocalDate())) {
                    newLocalDate = sub.getLocalDate();
                }
            }
        }


        super.duration = newDuration;
        super.localDate = newLocalDate;

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
