package com.rielec.machine.test.backend;

import com.rielec.machine.test.backend.application.configuration.Configuration;
import com.rielec.machine.test.backend.domain.machine.Machine;
import com.rielec.machine.test.backend.infrastructure.machine.service.MachineService;
import com.rielec.machine.test.backend.infrastructure.socket.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class BackendApplicationListener implements ApplicationRunner {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BackendApplicationListener.class);
    @Autowired
    private SocketService socketService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private Configuration configuration;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        socketService.start();

        // TODO: Consumir de la API /machines e importar los datos a nuestro sistema.
        Machine[] result = {};

        machineService.save(new ArrayList<>(Arrays.asList(result)));
        logger.info(String.format("%d Machines loaded successfully!", result.length));
    }
}
