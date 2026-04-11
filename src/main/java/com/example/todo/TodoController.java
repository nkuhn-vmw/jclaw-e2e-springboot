package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final ConcurrentHashMap<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todos.get(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Long id = idGenerator.getAndIncrement();
        todo.setId(id);
        todos.put(id, todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        if (!todos.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        todos.put(id, todo);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todos.remove(id);
        return ResponseEntity.noContent().build();
    }
}
