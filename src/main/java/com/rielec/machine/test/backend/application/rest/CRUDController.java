package com.rielec.machine.test.backend.application.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface CRUDController<T> {
    ResponseEntity<?> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              @RequestParam(value = "size", required = false) Integer size,
                              @RequestParam(value = "sortBy", required = false) String sortBy,
                              @RequestParam(value = "search", required = false) String search);
    ResponseEntity<?> find(@PathVariable("id") String id);
    ResponseEntity<?> save(@RequestBody T data);
    ResponseEntity<?> create(@RequestBody T data);
    ResponseEntity<?> delete(@PathVariable("id") String id);
}
