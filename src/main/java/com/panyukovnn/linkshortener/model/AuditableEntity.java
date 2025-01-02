package com.panyukovnn.linkshortener.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.panyukovnn.linkshortener.util.Constants.DB_DEFAULT_USER;

@Getter
@Setter
@MappedSuperclass
public class AuditableEntity {

    private LocalDateTime createTime;
    private String  createUser;
    private LocalDateTime lastUpdateTime;
    private String  lastUpdateUser;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.lastUpdateTime = now;
        this.createUser = DB_DEFAULT_USER;
        this.lastUpdateUser = DB_DEFAULT_USER;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdateTime = LocalDateTime.now();
        this.lastUpdateUser = DB_DEFAULT_USER;
    }
}
