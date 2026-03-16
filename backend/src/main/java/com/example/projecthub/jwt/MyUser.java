package com.example.projecthub.jwt;

import com.example.projecthub.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom UserDetails implementation for Spring Security
 * Wraps the User entity and provides role-based authority information
 */
public class MyUser implements UserDetails {

    private final User user;

    /**
     * Create MyUser from User entity
     * 
     * @param user the User entity
     */
    public MyUser(User user) {
        this.user = user;
    }

    /**
     * Returns the authorities granted to the user
     * Maps user roles to GrantedAuthority for Spring Security
     * 
     * @return collection of granted authorities based on user roles
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the password used to authenticate the user
     * 
     * @return encoded password
     */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user
     * 
     * @return username
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }


    public long getId(){
        return user.getId();
    }

    /**
     * Indicates whether the user's account has expired
     * 
     * @return true if user is valid, false if expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked
     * 
     * @return true if user is not locked, false if locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials have expired
     * 
     * @return true if credentials are valid, false if expired
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled
     * 
     * @return true if user is enabled, false if disabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
