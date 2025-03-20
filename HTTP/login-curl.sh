#!/bin/bash

# First, create a user if you haven't already
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test.user@example.com",
    "firstName": "Test",
    "lastName": "User",
    "phoneNumber": "555-123-4567",
    "userType": "CUSTOMER",
    "roles": ["ROLE_USER"],
    "active": true
  }'

echo -e "\n\nNow trying to login with the created user:\n"

# Then, login with the created user
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'