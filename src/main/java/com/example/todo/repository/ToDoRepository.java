package com.example.todo.repository;

import com.example.todo.domain.ToDo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ToDoRepository implements CommonRepository<ToDo> {
    private final Map<String, ToDo> toDos = new HashMap<>();

    @Override
    public ToDo save(ToDo toDo) {
        ToDo result = toDos.get(toDo.getId());
        if (result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(toDo.getDescription());
            result.setCompleted(toDo.isCompleted());
            toDo = result;
        }
        toDos.put(toDo.getId(), toDo);
        return toDos.get(toDo.getId());
    }

    @Override
    public Iterable<ToDo> save(Collection<ToDo> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(ToDo domain) {
        toDos.remove(domain.getId());
    }

    @Override
    public ToDo findById(String id) {
        return toDos.get(id);
    }

    @Override
    public Iterable<ToDo> findAll() {
        return toDos
                .entrySet()
                .stream()
                .sorted(entryComparator)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private final Comparator<Map.Entry<String, ToDo>> entryComparator =
            Comparator.comparing((Map.Entry<String, ToDo> o) -> o.getValue().getCreated());
}
