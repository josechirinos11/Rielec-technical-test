package com.rielec.machine.test.backend.application.beans;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.okhttp.OkDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.rielec.machine.test.backend.application.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class DockerBeans {
    @Autowired
    private Configuration configuration;

    @Bean
    public DockerClient getDockerClient() {
        if (configuration.getDockerHost() == null || configuration.getDockerHost().isEmpty()) {
            return DockerClientBuilder.getInstance().build();
        } else {
            DefaultDockerClientConfig defaultConfig = DefaultDockerClientConfig
                    .createDefaultConfigBuilder()
                    .withDockerHost(configuration.getDockerHost())
                    .build();

            DockerHttpClient httpClient = new OkDockerHttpClient.Builder()
                    .dockerHost(defaultConfig.getDockerHost())
                    .sslConfig(defaultConfig.getSSLConfig())
                    .build();

            return DockerClientBuilder
                    .getInstance(defaultConfig)
                    .withDockerHttpClient(httpClient)
                    .build();
        }
    }



}
