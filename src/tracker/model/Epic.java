package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id, String Name, String Description, Status status, ArrayList<SubTask> subTasks) {
        super(id, Name, Description, status);

        Duration newDuration = Duration.ofSeconds(0);
        LocalDateTime newLocalDate = LocalDateTime.of(1, 1, 1, 1, 1);

        if (subTasks != null) {
            newLocalDate = subTasks.get(0).getLocalDate();
            for (SubTask sub : subTasks) {
                newDuration = newDuration.plus(sub.getDuration());
                if (newLocalDate.isAfter(sub.getLocalDate())) {
                    newLocalDate = sub.getLocalDate();
                }
            }

        }
        this.subTasks = subTasks;

        calculateStatus();

        super.duration = newDuration;
        super.localDate = newLocalDate;

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

    public void calculateStatus() {

        if (subTasks == null) {
            this.status = Status.NEW;
            return;
        }
        boolean isDone = true;
        int numOfDone = 0;
        for (SubTask i : subTasks) {
            if (i.getStatus() == Status.valueOf("NEW")) {
                isDone = false;
            } else {
                numOfDone += 1;
            }
        }
        if (isDone) {
            this.setStatus(Status.DONE);
        } else if (numOfDone == 0) {
            this.setStatus(Status.NEW);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }

}
