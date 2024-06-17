package com.rielec.machine.test.backend.domain.socket;

public class SocketResponse extends SocketRequest {
    private String response;

    public SocketResponse() {
    }

    public SocketResponse(String id, String command, String response) {
        super(id, command);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
