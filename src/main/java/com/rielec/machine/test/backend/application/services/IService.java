package com.rielec.machine.test.backend.application.services;

import com.querydsl.core.types.Predicate;
import com.rielec.machine.test.backend.application.exceptions.CRUDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;

public interface IService<T> {
    T findById(String id) throws CRUDException;
    void delete(String id) throws CRUDException;
    void save(T item);
    void save(Iterable<T> items);
    void complete(T originalItem, T newItem);
    boolean existsById(String id);
    List<T> findAllByIds(List<String> ids);

    Predicate find(String search);
    List<T> findAll();
    List<T> findAll(Sort sort);
    Page<T> findAll(Pageable request);
    Page<T> findAll(Predicate predicate, Pageable request);
    List<T> findAll(Predicate predicate);
}