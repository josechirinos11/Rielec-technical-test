package com.rielec.machine.test.backend.infrastructure.instance.service;

import com.querydsl.core.types.Predicate;
import com.rielec.machine.test.backend.application.exceptions.CRUDException;
import com.rielec.machine.test.backend.application.services.IService;
import com.rielec.machine.test.backend.domain.instance.Instance;
import com.rielec.machine.test.backend.infrastructure.instance.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class InstanceService implements IService<Instance> {
    @Autowired
    private InstanceRepository repository;

    @Override
    public Instance findById(String id) {
        Optional<Instance> optional = repository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public void delete(String id) throws CRUDException {
        try {
            Optional<Instance> optional = repository.findById(id);
            if (!optional.isPresent()) {
                throw new CRUDException("Machine not found");
            }

            repository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Instance item) {
        repository.save(item);
    }

    @Override
    public void save(Iterable<Instance> items) {
        repository.saveAll(items);
    }

    @Override
    public void complete(Instance originalItem, Instance newItem) {
        originalItem.setName(newItem.getName());
        originalItem.setStatus(newItem.getStatus());
        originalItem.setLastModifiedDate(newItem.getLastModifiedDate());
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<Instance> findAllByIds(List<String> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Predicate find(String search) {
        return null;
    }

    /* -------------------------------------------------------------------------- */
    /*                              FIND ALL METHODS                              */
    /* -------------------------------------------------------------------------- */

    @Override
    public List<Instance> findAll() {
        List<Instance> items = repository.findAll();
        return items;
    }

    @Override
    public List<Instance> findAll(Sort sort) {
        List<Instance> items = repository.findAll(sort);
        return items;
    }

    @Override
    public Page<Instance> findAll(Pageable request) {
        Page<Instance> items = repository.findAll(request);
        return items;

    }

    @Override
    public Page<Instance> findAll(Predicate predicate, Pageable request) {
        Page<Instance> items = repository.findAll(predicate, request);
        return items;
    }

    @Override
    public List<Instance> findAll(Predicate predicate) {
        Iterable<Instance> foundList = repository.findAll(predicate);
        ArrayList<Instance> list = new ArrayList<>();

        Iterator<Instance> iterator = foundList.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }


}
