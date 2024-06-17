package com.rielec.machine.test.backend.application.beans;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SocketIOBean {
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        return new SocketIOServer(config);
    }
}
