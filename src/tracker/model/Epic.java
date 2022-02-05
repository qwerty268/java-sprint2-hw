package tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id, String Name, String Description, Status status, ArrayList<SubTask> subTasks) {
        super(id, Name, Description, status);
        this.subTasks = subTasks;
    }

    public void clearSubTasks(){
        subTasks.clear();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}
