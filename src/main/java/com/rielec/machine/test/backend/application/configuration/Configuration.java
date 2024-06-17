package com.rielec.machine.test.backend.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "technical.test.backend")
public class Configuration {
    private String urlMachines = "http://localhost:8108";
    private String dockerHost = "tcp://localhost:2375"; //"unix:///var/run/docker.sock";

    public String getUrlMachines() {
        return urlMachines;
    }

    public void setUrlMachines(String urlMachines) {
        this.urlMachines = urlMachines;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
    }
}
