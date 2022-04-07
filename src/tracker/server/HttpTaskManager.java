package tracker.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.model.Task;
import tracker.service.manager.inMemoryManager.FileBackedTasksManager;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private int id;

    public HttpTaskManager(String url, int id) {
        super(null);
        client = new KVTaskClient(url);

        this.id = id;
        loadFromServer();
    }


    @Override
    public void addAnyTypeOfTask(Task task) {
        super.addAnyTypeOfTask(task);
    }

    @Override
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        client.put(id + "", gson.toJson(this));
    }




    public void loadFromServer() {
        HttpTaskManager clone = client.load(id + "");

        //копирует состояние менеджера
        if (clone != null) {
            this.subtasks = clone.subtasks;
            this.tasks = clone.tasks;
            this.epics = clone.epics;
            this.historyManager = clone.historyManager;
            this.sortedTasks = clone.sortedTasks;
        }
    }
}
