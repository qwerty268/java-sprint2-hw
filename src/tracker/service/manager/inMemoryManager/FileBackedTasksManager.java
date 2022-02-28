package tracker.service.manager.inMemoryManager;

import tracker.exception.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private final File file = new File("info");

    public FileBackedTasksManager(InMemoryHistoryManager historyManager) {
        super(historyManager);
    }

    @Override
    public void addAnyTypeOfTask(Task task) {
        super.addAnyTypeOfTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void deleteByIndex(long index) {
        super.deleteByIndex(index);
        save();
    }

    @Override
    public Task getTask(long taskId) {
        historyManager.add(tasks.get(taskId));
        save();
        return tasks.get(taskId);
    }

    @Override
    public SubTask getSubTask(long subTaskId) {
        historyManager.add(subtasks.get(subTaskId));
        save();
        return subtasks.get(subTaskId);
    }

    @Override
    public Epic getEpic(long epicId) {
        historyManager.add(epics.get(epicId));
        save();
        return epics.get(epicId);
    }

    /* private boolean isFileEmpty(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file));) {
            return br.readLine() == null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    public void save() {
        try (Writer fileWriter = new FileWriter(file, false)) {


            fileWriter.write("id,type,name,status,description,epic\n");

            if (!tasks.isEmpty()) {
                for (Task task : tasks.values()) {
                    fileWriter.write(task.toString() + "\n");
                }
            }

            if (!epics.isEmpty()) {
                for (Task epic : epics.values()) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }

            if (!subtasks.isEmpty()) {
                for (Task epic : subtasks.values()) {
                    fileWriter.write(epic.toString() + "\n");
                }
            }

            fileWriter.write("\n");
            fileWriter.write(historyToString());

        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public void read() {
        try (Reader fileReader = new FileReader(file); BufferedReader br = new BufferedReader(fileReader)) {

            if (br.ready()) {
                br.readLine();
            }

            List<String[]> epicInfo = new ArrayList<>();

            while (br.ready()) {
                String rd = br.readLine();

                String[] line;

                if (rd.equals("")) {
                    break;
                }

                line = rd.split(",");

                if (Type.valueOf(line[1]) == Type.EPIC) {
                    epicInfo.add(line);
                } else {
                    fromString(line);
                }
            }

            for (String[] valueOfEpic : epicInfo) {
                Epic epic = makeEpicWithString(valueOfEpic);
                epics.put(epic.getTaskId(), epic);
            }

            if (br.ready()) {
                String rd = br.readLine();
                String[] line = rd.split(",");

                readHistory(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHistory(String[] value) {
        for (int i = value.length - 1; i > -1; i--) {

            if (tasks.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(tasks.get(Long.parseLong(value[i])));
            }
            if (epics.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(epics.get(Long.parseLong(value[i])));
            }
            if (subtasks.containsKey(Long.parseLong(value[i]))) {
                historyManager.add(subtasks.get(Long.parseLong(value[i])));
            }

        }

    }

    private String historyToString() {
        List<Task> history;
        StringBuilder historyInNum = new StringBuilder("null");

        if (historyManager != null) {
            history = new ArrayList<>(historyManager.getHistory());

            if (!history.isEmpty()) {
                historyInNum = new StringBuilder();

                for (int i = 0; i < history.size() - 1; i++) {
                    historyInNum.append(history.get(i).getTaskId()).append(",");
                }

                historyInNum.append(history.get(history.size() - 1).getTaskId());
            }

        }
        return historyInNum.toString();
    }

    private void fromString(String[] value) {

        switch (Type.valueOf(value[1])) {
            case TASK:
                Task task = new Task(Long.valueOf(value[0]), value[2], value[4], Status.valueOf(value[3]));
                tasks.put(task.getTaskId(), task);
                break;
            case SUBTASK:
                SubTask subTask = new SubTask(Long.valueOf(value[0]), value[2], value[4], Status.valueOf(value[3]));
                subtasks.put(subTask.getTaskId(), subTask);
        }

    }

    private Epic makeEpicWithString(String[] value) {
        ArrayList<SubTask> subTasks = null;

        if (!value[5].equals("null")) {
            subTasks = new ArrayList<>();
            for (int i = 5; i < value.length; i++) {
                subTasks.add(subtasks.get(Long.valueOf(value[i])));
            }
        }

        return  new Epic(Long.valueOf(value[0]), value[2], value[4], Status.valueOf(value[3]), subTasks);
    }
}
