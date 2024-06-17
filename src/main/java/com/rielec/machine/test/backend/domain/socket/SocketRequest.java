package com.rielec.machine.test.backend.domain.socket;

public class SocketRequest {
    protected String id;
    protected String command;
    public SocketRequest() {
    }

    public SocketRequest(String id) {
        this.id = id;
    }

    public SocketRequest(String id, String command) {
        this.id = id;
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public String getCommand() {
        return command;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
