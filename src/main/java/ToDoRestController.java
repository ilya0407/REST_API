import com.fasterxml.jackson.databind.ser.std.RawSerializer;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/todo")
public final class ToDoRestController {
    private final JdbcOperations jdbcOperations = null;

    @GetMapping
    public ResponseEntity<List<ToDo>> getToDoList(){
        return ResponseEntity.ok(this.jdbcOperations.query("select * from todo",
                (resultSet, i) -> new ToDo(UUID.fromString(resultSet.getString("id")),
                        resultSet.getTimestamp("date_created").toLocalDateTime(),
                        resultSet.getBoolean("done"),
                        resultSet.getString("task"))));
    }

    public ResponseEntity<ToDo> getToDo(@PathVariable UUID todoId){
        try{
           return ResponseEntity.ok(Objects.requireNonNull(this.jdbcOperations.queryForObject("select * from todo where id = ? limit 1",
                   (resultSet, i) -> new ToDo(UUID.fromString(resultSet.getString("id")),
                           resultSet.getTimestamp("date_created").toLocalDateTime(),
                           resultSet.getBoolean("done"),
                           resultSet.getString("task")))));
        }
        catch (IncorrectResultSizeDataAccessException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.example.todo_payload+json"})
    public ResponseEntity<ToDo> createToDo(@RequestBody ToDoPayload payload){
        var todo = new ToDo(payload.isDone(), payload.getTask());
        jdbcOperations.update("insert into todo (id, date_created, done, task) values (?, ?, ?, ?)",
                todo.getId().toString(), Timestamp.valueOf(todo.getDateCreated()),
                todo.isDone(), todo.getTask());
        return ResponseEntity.ok(todo);}

    @PutMapping(path = "{toDoId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, "application/vnd.example.todo_payload+json"}
    )
    public ResponseEntity<Void> modifyToDo(@PathVariable UUID toDoId, @RequestBody ToDoPayload payload){
        if(this.jdbcOperations.update("update todo set done = ?, task = ? where id = ?",
                payload.isDone(), payload.getTask()) == 1){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{toDoId}")
    public ResponseEntity<Void> deleteToDo(@PathVariable UUID toDoId){
        if(this.jdbcOperations.update("delete from todo where is = ?", toDoId.toString()) == 1){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
