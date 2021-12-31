package tracker.service;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    long newId = 0;

    public long getNewId() {
        return ++newId;
    }

    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    private HashMap<Long, SubTask> subtasks = new HashMap<>();

    public ArrayList<Task> getTasks() {
        ArrayList<Task> arrayOfTasks = new ArrayList<>();

        if (!tasks.isEmpty()) {
            for (Task i : tasks.values()) {
                arrayOfTasks.add(i);
            }
            return arrayOfTasks;
        }

        return null;
    }

    public ArrayList<Task> getEpics() {
        ArrayList<Task> arrayOfEpics = new ArrayList<>();

        if (!epics.isEmpty()) {
            for (Task i : epics.values()) {
                arrayOfEpics.add(i);
            }
            return arrayOfEpics;
        }

        return null;
    }

    public ArrayList<SubTask> getSubTasksOfEpic(Long epicId) {
        if (epics.get(epicId) == null) {
            return null;
        }

        return epics.get(epicId).getSubTasks();
    }

    public void add(Task task) {
        if (task == null) {
            return;
        }
        task.setTaskId(getNewId());
        if (task.getClass() == Task.class) {
            tasks.put(task.getTaskId(), task);
        }

        if (task.getClass() == Epic.class) {
            epics.put(task.getTaskId(), (Epic) task);
        }

        if (task.getClass() == SubTask.class) {
            subtasks.put(task.getTaskId(), (SubTask) task);
        }

    }

    public void update(Task task) {
        if (task.getClass() == Task.class) {
            tasks.put(task.getTaskId(), task);
        }

        if (task.getClass() == Epic.class) {
            epics.put(task.getTaskId(), (Epic) task);
            ArrayList<SubTask> subT = ((Epic) task).getSubTasks();
            boolean isDone = true;
            int numOfDone = 0;
            for (SubTask i : subT) {
                if (i.getStatus().equals("NEW")) {
                    isDone = false;
                } else {
                    numOfDone += 1;
                }
            }
            if (isDone) {
                task.setStatus("DONE");
            } else if(numOfDone == 0){
                task.setStatus("NEW");
            } else {
                task.setStatus("IN_PROGRESS");
            }
        }

        if (task.getClass() == SubTask.class) {
            subtasks.put(task.getTaskId(), (SubTask) task);
        }
    }

    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public void deleteByIndex(long index) {
        if (tasks.get(index) != null) {
            tasks.remove(index);
        }

        if (epics.get(index) != null) {
            epics.get(index).clearSubTasks();
            epics.remove(index);
        }

        if (subtasks.get(index) != null) {
            subtasks.remove(index);
        }

    }

}
