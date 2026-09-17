package org.chousik.infosec_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_users")
public class UserAccount {

    @Id
    @Column(length = 50)
    private String login;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    protected UserAccount() {
    }

    public UserAccount(String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
