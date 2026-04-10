package com.shoppinglist.backend.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "devices")
public class Device extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String deviceId;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public boolean active;

    @Column(nullable = false)
    public Instant registeredAt;

    @Column(nullable = false)
    public Instant lastSeenAt;

    public Device() {
    }

    public Device(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
        this.active = true;
        this.registeredAt = Instant.now();
        this.lastSeenAt = Instant.now();
    }
}
