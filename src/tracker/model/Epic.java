package tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<SubTask> subTasks;

    public Epic(long id) {
        super(id);
    }

    public void clearSubTasks(){
        subTasks.clear();
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }
}
