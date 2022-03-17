package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    public SubTask(long id, String Name, String Description, Status status, Duration duration, LocalDateTime localDate) {
        super(id, Name, Description, status, duration, localDate);
    }

    public String toString() {
        return String.join(",", taskId + "", Type.SUBTASK + "", Name, status + "", Description,
                duration.getSeconds() + "", localDate.format(dateTimeFormatter));
    }

}
