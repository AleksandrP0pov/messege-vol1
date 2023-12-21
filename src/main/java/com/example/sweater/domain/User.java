package com.example.sweater.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

import static org.springframework.boot.devtools.restart.AgentReloader.isActive;

@Data
@Entity
@Table(name = "usr")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private boolean acrive;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    // позволяет не создавать отдельную таблицу
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//   создается таблица для ролей и соединяемся мы с ней по user_id
    @Enumerated(EnumType.STRING) // храним enum в виде строки
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
