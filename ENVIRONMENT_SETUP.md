# PayGuard - Environment Setup Guide

## Overview
PayGuard uses environment variables for sensitive configuration to prevent credentials from being committed to version control.

## Setup Instructions

### 1. Local Development Setup

#### Option A: Using Environment Variables (Recommended)
Set environment variables in your system or IDE:

**Windows (PowerShell)**:
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/payguard"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_password_here"
```

**Linux/Mac (Bash)**:
```bash
export DB_URL="jdbc:postgresql://localhost:5432/payguard"
export DB_USERNAME="postgres"
export DB_PASSWORD="your_password_here"
```

**IntelliJ IDEA**:
1. Run → Edit Configurations
2. Set environment variables in the "Environment variables" field:
   ```
   DB_URL=jdbc:postgresql://localhost:5432/payguard;DB_USERNAME=postgres;DB_PASSWORD=your_password
   ```

#### Option B: Using Profile-specific Properties File
1. Copy `application-example.properties` to `application-dev.properties`:
   ```bash
   cp src/main/resources/application-example.properties src/main/resources/application-dev.properties
   ```

2. Edit `application-dev.properties` with your local database credentials

3. Run with the dev profile:
   ```bash
   java -Dspring.profiles.active=dev -jar payguard.jar
   ```

### 2. Production Environment Setup

For production, use your deployment platform's secrets management:

**AWS**:
- Store secrets in AWS Secrets Manager
- Use IAM roles to provide access

**Docker**:
```bash
docker run -e DB_URL=... -e DB_USERNAME=... -e DB_PASSWORD=... payguard:latest
```

**Kubernetes**:
```yaml
env:
  - name: DB_URL
    valueFrom:
      secretKeyRef:
        name: payguard-secrets
        key: db-url
  - name: DB_USERNAME
    valueFrom:
      secretKeyRef:
        name: payguard-secrets
        key: db-username
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: payguard-secrets
        key: db-password
```

### 3. Environment Variables Reference

| Variable | Description | Example | Required |
|----------|-------------|---------|----------|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/payguard` | Yes |
| `DB_USERNAME` | Database username | `postgres` | Yes |
| `DB_PASSWORD` | Database password | `secure_password` | Yes |
| `server.port` | Server port (optional) | `8080` | No |

### 4. Important Notes

⚠️ **SECURITY**:
- Never commit `application.properties` or `application-*.properties` files with real credentials to version control
- `.gitignore` is configured to prevent this
- Only `application-example.properties` should be committed

✅ **Best Practices**:
- Use strong passwords for database credentials
- Rotate credentials regularly
- Use environment-specific configurations
- Never share credentials in code reviews or documentation
- Store credentials in secure vaults (HashiCorp Vault, AWS Secrets Manager, etc.)

### 5. Verification

To verify your setup is correct, run:
```bash
./mvnw.cmd clean compile
```

The application should compile successfully without exposing any credentials in logs.

## Files Reference

- `application.properties` - ❌ Do NOT commit (contains sensitive data)
- `application-example.properties` - ✅ Commit this (template only)
- `.gitignore` - ✅ Configured to block credentials
- `ENVIRONMENT_SETUP.md` - ✅ This file

## Troubleshooting

**Issue**: "Unable to connect to database"
- Ensure environment variables are set
- Verify database is running
- Check credentials are correct

**Issue**: "Property not found" warnings
- Default values are provided in `application.properties`
- Override with environment variables when needed

**Issue**: Changes to application.properties keep getting overwritten
- This file is ignored by Git
- Your local changes won't be committed
- This is intentional for security

For more help, refer to [Spring Boot Configuration Documentation](https://spring.io/guides/gs/managing-secrets/).
