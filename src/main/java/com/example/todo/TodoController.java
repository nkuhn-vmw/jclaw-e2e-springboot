package com.example.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final Map<Long, Todo> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping
    public List<Todo> getAll() {
        return new ArrayList<>(store.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        Todo todo = store.get(id);
        if (todo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        long id = idGenerator.getAndIncrement();
        todo.setId(id);
        store.put(id, todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        if (!store.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        todo.setId(id);
        store.put(id, todo);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Todo removed = store.remove(id);
        if (removed == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}
