package com.v2solutions.user_service.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String mobile;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    protected User() {}

    public User(UUID id, String name, String email, String mobile, String address) {
        this.id = id; this.name = name; this.email = email; this.mobile = mobile; this.address = address;
        this.status = UserStatus.ACTIVE;
    }

    // getters/setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
}
