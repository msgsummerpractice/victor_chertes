package com.example.demo.repository;
import java.util.List;


public interface Repository<T, ID> {
    T save(T entity);
    T findById(ID id);
    List<T> findAll();
    
}
