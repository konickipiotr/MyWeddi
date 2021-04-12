package com.myweddi.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class UserAuth implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    public UserAuth() {
    }

    public UserAuth(String username, String password, String role, UserStatus status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public UserAuth(RegistrationForm rf) {
        this.username = rf.getUsername();
        this.password = rf.getPassword();
        this.role = rf.getUsertype();
        this.status = UserStatus.FIRSTLOGIN;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+ this.role);
        if(status == UserStatus.ACTIVE) {
            authorities.add(new SimpleGrantedAuthority("ACCESS_" + "ACTIVE"));
        }else if(status == UserStatus.FIRSTLOGIN) {
            authorities.add(new SimpleGrantedAuthority("ACCESS_" + "FIRSTLOGIN"));
        }
        authorities.add(authority);
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
