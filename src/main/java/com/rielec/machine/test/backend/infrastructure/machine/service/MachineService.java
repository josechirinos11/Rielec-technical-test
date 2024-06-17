package com.rielec.machine.test.backend.infrastructure.machine.service;

import com.querydsl.core.types.Predicate;
import com.rielec.machine.test.backend.application.exceptions.CRUDException;
import com.rielec.machine.test.backend.application.services.IService;
import com.rielec.machine.test.backend.domain.machine.Machine;
import com.rielec.machine.test.backend.infrastructure.machine.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MachineService implements IService<Machine> {
    @Autowired
    private MachineRepository repository;

    @Override
    public Machine findById(String id) {
        Optional<Machine> optional = repository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public void delete(String id) throws CRUDException {
        try {
            Optional<Machine> optional = repository.findById(id);
            if (!optional.isPresent()) {
                throw new CRUDException("Machine not found");
            }

            repository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Machine item) {
        repository.save(item);
    }

    @Override
    public void save(Iterable<Machine> items) {
        repository.saveAll(items);
    }

    @Override
    public void complete(Machine originalItem, Machine newItem) {
        originalItem.setName(newItem.getName());
        originalItem.setImage(newItem.getImage());
        originalItem.setDescription(newItem.getDescription());
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public List<Machine> findAllByIds(List<String> ids) {
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
    public List<Machine> findAll() {
        List<Machine> items = repository.findAll();
        return items;
    }

    @Override
    public List<Machine> findAll(Sort sort) {
        List<Machine> items = repository.findAll(sort);
        return items;
    }

    @Override
    public Page<Machine> findAll(Pageable request) {
        Page<Machine> items = repository.findAll(request);
        return items;

    }

    @Override
    public Page<Machine> findAll(Predicate predicate, Pageable request) {
        Page<Machine> items = repository.findAll(predicate, request);
        return items;
    }

    @Override
    public List<Machine> findAll(Predicate predicate) {
        Iterable<Machine> foundList = repository.findAll(predicate);
        ArrayList<Machine> list = new ArrayList<>();

        Iterator<Machine> iterator = foundList.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }


}
