package tracker.server;

import tracker.model.Task;
import tracker.service.manager.inMemoryManager.FileBackedTasksManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    public HttpTaskManager(File file) {
        super(file);
    }

}
