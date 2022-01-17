package tracker.service;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTasksManager implements TaskManager {
    long newId = 0;

    public long getNewId() {
        return ++newId;
    }

    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Epic> epics = new HashMap<>();
    private HashMap<Long, SubTask> subtasks = new HashMap<>();
    private ArrayList<Task> historyList = new ArrayList<>();

    public ArrayList<Task> getTasks() {
        ArrayList<Task> arrayOfTasks = new ArrayList<>();

        if (!tasks.isEmpty()) {
            for (Task i : tasks.values()) {
                arrayOfTasks.add(i);
            }
            return arrayOfTasks;
        }

        System.out.println("Задач нет");

        return arrayOfTasks;
    }

    public ArrayList<Task> getEpics() {
        ArrayList<Task> arrayOfEpics = new ArrayList<>();

        if (!epics.isEmpty()) {
            for (Task i : epics.values()) {
                arrayOfEpics.add(i);
            }
            return arrayOfEpics;
        }

        System.out.println("Нет епиков");
        return arrayOfEpics;
    }

    public ArrayList<SubTask> getSubTasksOfEpic(Long epicId) {
        if (epics.get(epicId) == null) {
            return null;
        }

        return epics.get(epicId).getSubTasks();
    }

    @Override
    public void addAnyTypeOfTask(Task task) {
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

    @Override
    public void updateTask(Task task) {
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
                task.setStatus(Status.DONE);
            } else if (numOfDone == 0) {
                task.setStatus(Status.NEW);
            } else {
                task.setStatus(Status.IN_PROGRESS);
            }
        }

        if (task.getClass() == SubTask.class) {
            subtasks.put(task.getTaskId(), (SubTask) task);
        }
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
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

    public SubTask getSubTask(long subTaskId) {
        if (subtasks.containsKey(subTaskId)) {
            historyList.add(0, subtasks.get(subTaskId));
            if (historyList.size() == 11) {
                historyList.remove(10);
            }
        }
        return subtasks.get(subTaskId);
    }

    public Epic getEpic(long epicId) {
        if (epics.containsKey(epicId)) {
            historyList.add(0, epics.get(epicId));
            if (historyList.size() == 11) {
                historyList.remove(10);
            }
        }
        return epics.get(epicId);
    }

    @Override
    public ArrayList<Task> history() {
        return historyList;
    }
}
