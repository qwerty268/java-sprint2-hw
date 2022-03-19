package tracker.service.manager.inMemoryManager;

import tracker.exception.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
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

    public static FileBackedTasksManager loadFromFile(File newFile) {
        FileBackedTasksManager manager = new FileBackedTasksManager(newFile);

        manager.read();

        return manager;
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file, false)) {


            fileWriter.write("id,type,name,status,description,epic,duration,LocalDate\n");

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
                makeEpicWithString(valueOfEpic);
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
                addAnyTypeOfTask(new Task(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]),
                        Duration.ofSeconds(Long.parseLong(value[5])), LocalDateTime.parse(value[6], Task.dateTimeFormatter)));                                                                  //!!!!!
                break;
            case SUBTASK:
                addAnyTypeOfTask(new SubTask(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]),
                        Duration.ofSeconds(Long.parseLong(value[5])),
                        LocalDateTime.parse(value[6], Task.dateTimeFormatter)));
        }

    }

    private void makeEpicWithString(String[] value) {
        ArrayList<SubTask> subTasks = null;

        if (!value[5].equals("null")) {
            subTasks = new ArrayList<>();
            for (int i = 5; i < value.length; i++) {
                subTasks.add(subtasks.get(Long.valueOf(value[i])));
            }
        }

        addAnyTypeOfTask(new Epic(Long.parseLong(value[0]), value[2], value[4], Status.valueOf(value[3]), subTasks));                                                                          //!!!!!!!!
    }
}
