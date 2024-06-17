package com.rielec.machine.test.backend.domain.machine;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Machine {
    @Id
    @Indexed
    private String uuid;
    @Indexed
    private String name;
    private String image;
    @Indexed(unique = true)
    private String reference;
    @Indexed
    private String description;

    public Machine() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
