package com.rielec.machine.test.backend.infrastructure.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rielec.machine.test.backend.domain.instance.InstanceStatus;
import com.rielec.machine.test.backend.domain.socket.SocketRequest;
import com.rielec.machine.test.backend.domain.socket.SocketResponse;
import com.rielec.machine.test.backend.infrastructure.status.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;

@Service
public class SocketService {
    private static final Logger logger = LoggerFactory.getLogger(SocketService.class);
    private static final Gson gson = new GsonBuilder().create();

    private HashMap<String, String> idPairings = new HashMap<>();

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private StatusService statusService;

    public void start() {

        socketIOServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                logger.info("Socket Client connected: " +socketIOClient.getSessionId());
            }
        });

        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                System.out.println("Socket IO Disconnected");
            }
        });

        socketIOServer.addEventListener("machineStatusChange", Object.class, (client, data, ackSender) -> {
            logger.info("machineStatusChanged: " + data.toString());
            SocketResponse response = gson.fromJson(data.toString(), SocketResponse.class);

            idPairings.put(response.getId(), client.getSessionId().toString());

            InstanceStatus status = InstanceStatus.valueOf(response.getResponse());

            statusService.setInstanceStatus(response.getId(), status, new Timestamp(System.currentTimeMillis()));
        });
        socketIOServer.start();
    }

    public void requestMachineShutdown(SocketRequest request) {
        String message = gson.toJson(request);

        request.setCommand("requestMachineShutdown");

        if (idPairings.get(request.getId()) != null) {
            // TODO: Envia el mensaje por socket al cliente con id "idPairings.get(request.getId())"
        } else {
            if (socketIOServer.getAllClients().size() > 0) {
                socketIOServer.getBroadcastOperations().sendEvent(request.getCommand(), message);
            }
        }

    }

    public void requestMachineStatus(SocketRequest request) {
        ObjectMapper mapper = new ObjectMapper();

        String message = null;
        try {
            message = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        request.setCommand("requestMachineStatus");

        if (idPairings.get(request.getId()) != null) {
            // TODO: Envia el mensaje por socket al cliente con id "idPairings.get(request.getId())"
        } else {
            if (socketIOServer.getAllClients().size() > 0) {
                socketIOServer.getBroadcastOperations().sendEvent(request.getCommand(), message);
            }
        }
    }
}

