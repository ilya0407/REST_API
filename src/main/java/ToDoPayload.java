import lombok.Data;

@Data
public final class ToDoPayload {

    private boolean done;

    private String task;

    public boolean isDone() {
        return done;
    }

    public String getTask() {
        return task;
    }
}
