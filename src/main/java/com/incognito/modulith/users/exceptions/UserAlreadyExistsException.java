package com.incognito.modulith.users.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException withUsername(String username) {
        return new UserAlreadyExistsException("User with username " + username + " already exists");
    }

    public static UserAlreadyExistsException withEmail(String email) {
        return new UserAlreadyExistsException("User with email " + email + " already exists");
    }
}