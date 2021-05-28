import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ToDo {
    private UUID id;
    private LocalDateTime dateCreated;
    private boolean done;
    private String task;

    public ToDo(UUID id, LocalDateTime date_created, boolean done, String task) {
        this.id = id;
        this.dateCreated = date_created;
        this.done = done;
        this.task = task;
    }

    public ToDo(boolean done, String task) {
        this(UUID.randomUUID(),LocalDateTime.now(),done,task);
    }

    public Object getId() {
        return this.id;
    }

    public LocalDateTime getDateCreated() {
        return this.dateCreated;
    }

    public boolean isDone() {
        return done;
    }

    public String getTask() {
        return task;
    }
}
