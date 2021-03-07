package com.myweddi.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class UserAuth implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String role;
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status;

    public UserAuth(String username, String password, String role, UserStatus status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public UserAuth(RegistrationForm rf) {
        this.username = rf.getUsername();
        this.password = rf.getPassword();
        this.role = "HOST";
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
}
