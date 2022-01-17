package tracker.service;

public class Managers {
    public static InMemoryTasksManager getDefault() {
        return new InMemoryTasksManager();
    }
}
