package com.example.cardservice.repository;

import com.example.cardservice.model.Operation;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class OperationRepository {
    private final Map<String, Operation> operations = new ConcurrentHashMap<>();

    public void save(Operation operation) {
        operations.put(operation.getOperationId(), operation);
    }

    public Optional<Operation> findById(String operationId) {
        return Optional.ofNullable(operations.get(operationId));
    }
}