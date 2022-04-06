package tracker.service.manager;

import tracker.server.HttpTaskManager;

public class Managers {

    public static HttpTaskManager getDefault(String url, int id) {
        return new HttpTaskManager(url, id);
    }
}
