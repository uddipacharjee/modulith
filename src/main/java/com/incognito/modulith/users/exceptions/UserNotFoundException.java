package com.incognito.modulith.users.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public static UserNotFoundException forId(Long id) {
        return new UserNotFoundException("User with ID " + id + " not found");
    }

    public static UserNotFoundException forUsername(String username) {
        return new UserNotFoundException("User with username " + username + " not found");
    }

    public static UserNotFoundException forEmail(String email) {
        return new UserNotFoundException("User with email " + email + " not found");
    }
}