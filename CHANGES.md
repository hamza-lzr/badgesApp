# Keycloak User Synchronization

## Overview

This update adds functionality to synchronize users created manually in Keycloak with the application's database. Previously, users created directly in the Keycloak admin console would not appear in the application database until they logged in for the first time. This new feature allows administrators to explicitly synchronize Keycloak users with the application database.

## New Components

1. **KeycloakAdminService Enhancements**
   - Added methods to fetch users from Keycloak:
     - `getUserById(String userId)`: Gets a user by their Keycloak ID
     - `getAllUsers()`: Gets all users from Keycloak
     - `getUserRoles(String userId)`: Gets the roles assigned to a user

2. **KeycloakSyncService**
   - New service that handles the synchronization between Keycloak and the application database
   - Provides methods to:
     - `syncKeycloakUser(String keycloakId)`: Synchronize a specific user
     - `syncAllKeycloakUsers()`: Synchronize all users from Keycloak

3. **KeycloakSyncController**
   - REST API endpoints to trigger synchronization:
     - `POST /api/keycloak-sync/user/{keycloakId}`: Sync a specific user
     - `POST /api/keycloak-sync/all`: Sync all users

## Usage

### Synchronizing a Specific User

If you've created a user manually in Keycloak and want to add them to the application database:

1. Log in to the application as an administrator
2. Get the Keycloak ID of the user you want to synchronize (from the Keycloak admin console)
3. Make a POST request to `/api/keycloak-sync/user/{keycloakId}` with the Keycloak ID

Example using curl:
```bash
curl -X POST http://localhost:8080/api/keycloak-sync/user/12345678-1234-1234-1234-123456789012 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Synchronizing All Users

To synchronize all users from Keycloak with the application database:

1. Log in to the application as an administrator
2. Make a POST request to `/api/keycloak-sync/all`

Example using curl:
```bash
curl -X POST http://localhost:8080/api/keycloak-sync/all \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Notes

- Only users with the ADMIN role can trigger synchronization
- For users without a matricule, a unique matricule will be generated based on their username or email
- The synchronization process will update existing users with the latest information from Keycloak
- If a user already exists in the application database, their matricule will not be changed