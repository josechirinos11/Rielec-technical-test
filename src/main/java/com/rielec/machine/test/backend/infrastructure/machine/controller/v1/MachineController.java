package com.rielec.machine.test.backend.infrastructure.machine.controller.v1;

import com.rielec.machine.test.backend.application.rest.CRUDController;
import com.rielec.machine.test.backend.domain.machine.Machine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rielec.machine.test.backend.infrastructure.machine.service.MachineService;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1/machines")
public class MachineController implements CRUDController<Machine> {

    @Autowired
    private MachineService service;

    @GetMapping
    @Override
    public ResponseEntity<?> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "sortBy", required = false) String sortBy,
                                     @RequestParam(value = "search", required = false) String search) {
        // TODO: Implementa la lógica para obtener todas las máquinas
        List<?> items = new ArrayList<>();

        return ResponseEntity.ok(items);

    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> find(@PathVariable("id") String id) {
        Machine item = null;

        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(item);
    }

    @PostMapping
    @Override
    public ResponseEntity<?> create(@RequestBody Machine data) {
        try {
            // FUNCIONALIDAD NO REQUERIDA.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Not implemented yet");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    @PutMapping
    @Override
    public ResponseEntity<?> save(@RequestBody Machine item) {
        try {
            Machine savedItem = service.findById(item.getUuid());
            service.complete(savedItem, item);
            service.save(savedItem);
            return ResponseEntity.ok(savedItem.getUuid());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("It cannot be deleted because it is associated");
        }
    }
}
