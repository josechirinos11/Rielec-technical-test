package com.rielec.machine.test.backend.infrastructure.status.service;

import com.rielec.machine.test.backend.domain.instance.InstanceStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;

@Service
public class StatusService {

    private HashMap<String, InstanceStatus> statusMap = new HashMap<>();
    private HashMap<String, Timestamp> timestampMap = new HashMap<>();

    public InstanceStatus getInstanceStatus(String id) {
        return statusMap.containsKey(id) ? statusMap.get(id) : InstanceStatus.NONE;
    }

    public Timestamp getInstanceStatusTimestamp(String id) {
        return timestampMap.containsKey(id) ? timestampMap.get(id) : null;
    }

    public void setInstanceStatus(String id, InstanceStatus status, Timestamp timestamp) {
        statusMap.put(id, status);
        timestampMap.put(id, timestamp);
    }

}
