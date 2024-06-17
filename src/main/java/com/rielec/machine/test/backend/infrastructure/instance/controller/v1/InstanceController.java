package com.rielec.machine.test.backend.infrastructure.instance.controller.v1;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.RestartPolicy;
import com.querydsl.core.types.Predicate;
import com.rielec.machine.test.backend.application.exceptions.CRUDException;
import com.rielec.machine.test.backend.application.rest.CRUDController;
import com.rielec.machine.test.backend.application.utils.PageUtil;
import com.rielec.machine.test.backend.domain.instance.Instance;
import com.rielec.machine.test.backend.domain.instance.InstanceStatus;
import com.rielec.machine.test.backend.domain.machine.Machine;
import com.rielec.machine.test.backend.domain.socket.SocketRequest;
import com.rielec.machine.test.backend.infrastructure.instance.service.InstanceService;
import com.rielec.machine.test.backend.infrastructure.machine.service.MachineService;
import com.rielec.machine.test.backend.infrastructure.status.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.rielec.machine.test.backend.infrastructure.socket.SocketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/instances")
public class InstanceController implements CRUDController<Instance> {
    private static final Logger logger = LoggerFactory.getLogger(InstanceController.class);
    @Autowired
    private InstanceService service;

    @Autowired
    private MachineService machineService;

    @Autowired
    private SocketService socketService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private DockerClient dockerClient;

    @Override
    public ResponseEntity<?> findAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", required = false) Integer size,
                                     @RequestParam(value = "sortBy", required = false) String sortBy,
                                     @RequestParam(value = "search", required = false) String search) {
        boolean hasSearch = search != null && !search.isEmpty();
        boolean hasSort = sortBy != null && !sortBy.isEmpty();
        boolean hasSize = size != null;

        if (hasSize || hasSearch) {
            if (search == null) search = "";
            if (size == null) size = 20;

            // EXAMPLE: ?sortBy=name:ASC,lastName:DESC
            HashMap<String, PageUtil.Order> sort = PageUtil.getSorting(sortBy);
            Pageable pageable = PageUtil.makeRequest(page, size, sort);
            Sort _sort = PageUtil.parseSort(sortBy);

            Predicate predicate = service.find(search);
            Page<Instance> items = service.findAll(predicate, pageable);

            for (Instance item : items.getContent()) {
                item.setStatus(statusService.getInstanceStatus(item.getUuid()));
                if (item.getStatus() == null) {
                    item.setStatus(InstanceStatus.NONE);
                }
            }

            return ResponseEntity.ok(items);
        } else {
            List<Instance> items = null;
            if (hasSort) {
                items = service.findAll(PageUtil.parseSort(sortBy));
            } else {
                items = service.findAll();
            }

            for (Instance item : items) {
                item.setStatus(statusService.getInstanceStatus(item.getUuid()));
                if (item.getStatus() == null) {
                    item.setStatus(InstanceStatus.NONE);
                }
            }

            return ResponseEntity.ok(items);
        }
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<?> find(@PathVariable("id") String id) {
        Instance item = service.findById(id);

        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        item.setStatus(statusService.getInstanceStatus(item.getUuid()));
        if (item.getStatus() == null) {
            item.setStatus(InstanceStatus.NONE);
        }

        return ResponseEntity.ok(item);
    }

    @PostMapping
    @Override
    public ResponseEntity<?> create(@RequestBody Instance data) {
        try {
            Machine machine = data.getMachine();
            if (machine.getUuid() != null) {
                machine = machineService.findById(machine.getUuid());
                data.setMachine(machine);
            } else {
                data.setMachine(null);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(data.getUuid());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    @PutMapping
    @Override
    public ResponseEntity<?> save(@RequestBody Instance item) {
        try {
            Instance savedItem = service.findById(item.getUuid());
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
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("It cannot be deleted because it is associated");
        } catch (CRUDException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }


    @PostMapping("/{id}/start")
    public ResponseEntity<?> start(@PathVariable("id") String id) {
        try {
            Instance item = service.findById(id);

            String image = "technical-test-service:0.0.1-SNAPSHOT";
            String environment = String.format("JAVA_OPTS=-Dtechnical.test.service.id=%s", id);
            String containerName = String.format("technical-test-service-%s", id);
            long memoryLimit = 200;

            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withRestartPolicy(RestartPolicy.unlessStoppedRestart())
                    .withMemory(memoryLimit * 1024L * 1024L);

            // TODO: Find the container
            String containerId = null;
            List<Container> containers = new ArrayList<>();
            if (containers.isEmpty()) {
                CreateContainerResponse response = dockerClient.createContainerCmd(image)
                        .withHostConfig(hostConfig)
                        .withName(containerName)
                        .withEnv(environment)
                        .exec();

                containerId = response.getId();
            } else {
                Container container = containers.get(0);
                containerId = container.getId();
            }

            if (containerId == null || containerId.isEmpty()) {
                throw new Exception("Container ID is empty");
            }

            dockerClient.startContainerCmd(containerId).exec();
            logger.info(String.format("Container %s started", containerId));

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<?> stop(@PathVariable("id") String id) {
        try {
            // TODO: Realiza una llamada que pare el servicio
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable("id") String id) {
        try {
            socketService.requestMachineStatus(new SocketRequest(id));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }
}
