package tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id, String Name, String Description, Status status) {
        super(id, Name, Description, status);
    }

    public void clearSubTasks(){
        subTasks.clear();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}
