# PayGuard Project - Comprehensive Code Review
**Date**: 2025 | **Status**: Development Phase | **Target**: Production-Ready

---

## Executive Summary

PayGuard is a **well-structured Spring Boot payment processing system** with proper layered architecture and modern Java practices. However, it requires significant hardening in security, validation, error handling, and API consistency before production deployment. This review identifies critical gaps and provides actionable recommendations.

---

## ✅ WHAT YOU DID RIGHT

### 1. **Architecture & Design Patterns**
- ✅ Clean **layered architecture** (Controller → Service → Repository → Entity)
- ✅ Proper **separation of concerns** - business logic isolated in services
- ✅ Good use of **DTOs** for API contracts (request/response separation)
- ✅ **MapStruct** integration for type-safe entity-DTO mapping
- ✅ Proper **Dependency Injection** via constructor with `@RequiredArgsConstructor`

### 2. **Spring Boot Best Practices**
- ✅ Modern Spring Boot **4.1.0** with latest features
- ✅ **Spring Data JPA** for data access abstraction
- ✅ **BCryptPasswordEncoder** for secure password hashing
- ✅ Proper use of **Lombok** to reduce boilerplate
- ✅ **@Transactional** annotations used in some services

### 3. **Data Modeling**
- ✅ Proper **JPA entity relationships** (ManyToOne with FetchType.LAZY)
- ✅ Good use of **database constraints** (unique, nullable, updatable flags)
- ✅ Automatic **timestamp management** with `@PrePersist/@PreUpdate`
- ✅ **Enum types** for PaymentStatus, TransactionStatus, UserStatus, Role
- ✅ Proper use of **BigDecimal** for monetary values (not float/double)
- ✅ User hierarchy with role-based types (Admin, Customer, Merchant)

### 4. **Security Foundation**
- ✅ **Spring Security** integrated
- ✅ **Password encoding** configured
- ✅ Password hashed before storage

---

## ❌ CRITICAL ISSUES (Must Fix for Production)

### 1. **SECURITY - Most Critical** 🔴

#### Issue 1a: No Authorization/Access Control
```java
// ❌ SECURITY ISSUE - In SecurityConfig.java
http.authorizeHttpRequests(auth -> auth
    .anyRequest().permitAll()  // ⚠️ ALL endpoints publicly accessible!
);
```

**Impact**: Any unauthenticated user can access admin endpoints, view/modify payments, steal customer data.

**Fix Required**:
```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Only disable if using stateless JWT auth
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(POST, "/api/customers/**").permitAll()  // Allow registration
                .requestMatchers(POST, "/api/merchant/**").permitAll()
                .requestMatchers(GET, "/api/auth/**").permitAll()
                .requestMatchers("/app/v1/admin/**").hasRole("ADMIN")  // ✅ Admin only
                .requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER")
                .requestMatchers("/api/merchant/**").hasRole("MERCHANT")
                .anyRequest().authenticated()
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
```

#### Issue 1b: No Authentication Mechanism
- No login endpoint
- No JWT/OAuth support
- No session management
- Users can't be authenticated

**Fix Required**: Implement one of:
1. **JWT Authentication** (recommended for REST APIs)
   - Add Spring Security JWT dependencies
   - Implement authentication controller with login endpoint
   - Create JWT token provider and filter

2. **Spring Security with UserDetailsService**
   ```java
   @Service
   public class CustomUserDetailsService implements UserDetailsService {
       @Override
       public UserDetails loadUserByUsername(String email) {
           User user = userRepository.findByEmail(email)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
           return org.springframework.security.core.userdetails.User.builder()
               .username(user.getEmail())
               .password(user.getPasswordHash())
               .roles(user.getRole().name())
               .build();
       }
   }
   ```

#### Issue 1c: Credentials in Configuration
```properties
# ❌ SECURITY ISSUE - application.properties
spring.datasource.username=postgres
spring.datasource.password=tanaka  # Exposed in version control!
```

**Fix Required**:
- Use **environment variables** or **.env files** (not in git)
- Use **Spring Cloud Config** for centralized secrets
- Use **Spring Vault** for secrets management in production

#### Issue 1d: Hardcoded Sensitive Data
```java
// ❌ If any API keys are hardcoded, move to properties
```

**Fix Required**: Move all configuration to environment-specific properties files.

### 2. **Input Validation - Critical** 🔴

#### Issue 2a: No Validation Annotations
```java
// ❌ NO VALIDATION
public record CustomerDto(
    String firstName,          // Could be empty, null, or 10000 chars
    String lastName,           // Same issue
    String email,              // No format validation
    String password            // No strength requirements
) { }

// ❌ NO VALIDATION
public record PaymentRequestDto(
    Long customerId,           // Not validated
    Long merchantId,           // Not validated
    BigDecimal amount,         // Could be negative!
    String currency            // Could be invalid
) { }
```

**Fix Required**:
```java
import jakarta.validation.constraints.*;

public record CustomerDto(
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50)
    String lastName,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=]).{8,}$",
             message = "Password must contain uppercase, number, and special character")
    String password
) { }

public record PaymentRequestDto(
    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    Long customerId,
    
    @NotNull(message = "Merchant ID is required")
    @Positive(message = "Merchant ID must be positive")
    Long merchantId,
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Amount exceeds maximum")
    BigDecimal amount,
    
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be 3-letter code")
    String currency
) { }
```

#### Issue 2b: Controllers Not Using @Valid
```java
// ❌ No validation triggered
@PostMapping
public CustomerResponseDto createCustomer(@RequestBody CustomerDto customerDto) {
    return customerService.createCustomer(customerDto);
}

// ✅ CORRECT
@PostMapping
public CustomerResponseDto createCustomer(@Valid @RequestBody CustomerDto customerDto) {
    return customerService.createCustomer(customerDto);
}
```

### 3. **Error Handling - Missing** 🔴

#### Issue 3a: No Global Exception Handler
```java
// ❌ PROBLEM - Unhandled exceptions return generic Spring errors
@Override
public PaymentResponseDto getPaymentById(Long id) {
    Payment payment = paymentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Payment not found"));  // ❌ Generic exception
    return paymentMapper.toResponseDto(payment);
}

// ❌ Return to client:
{
    "timestamp": "2025-01-06T10:30:00Z",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Payment not found",
    "path": "/api/payment/999"
}
```

**Fix Required**: Create centralized exception handler:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "USER_NOT_FOUND",
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "USER_ALREADY_EXISTS",
            ex.getMessage(),
            null
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "VALIDATION_ERROR",
            "Validation failed",
            errors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Unexpected error", ex);
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "INTERNAL_ERROR",
            "An unexpected error occurred",
            null
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

Define error response DTO:
```java
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String code,
    String message,
    Object errors
) { }
```

#### Issue 3b: Inconsistent Exception Types
```java
// ❌ Mix of custom and generic exceptions
new UserNotFoundException("Customer not found")  // Custom
new RuntimeException("Payment not found")        // Generic - BAD!
```

**Fix**: Use custom exceptions consistently.

### 4. **API Inconsistency** 🔴

#### Issue 4a: Inconsistent Path Conventions
```
❌ Mixed naming:
/app/v1/admin/...          (app prefix, v1)
/api/customers/...          (api prefix, no version)
/api/merchant/...           (singular, not plural)
/api/payment/...            (singular)
/api/transaction/...        (singular)
```

**Fix Required**: Standardize API paths
```
✅ CONSISTENT:
/api/v1/admin/...
/api/v1/customers/...
/api/v1/merchants/...
/api/v1/payments/...
/api/v1/transactions/...
```

#### Issue 4b: Duplicate/Conflicting Mappings
```java
// ❌ AdminController - TWO methods with same @GetMapping
@GetMapping("/recentPayments")
public List<PaymentResponseDto> getRecentPayments() { ... }

@GetMapping("/recentPayments")
public List<PaymentResponseDto> getRecentPaymentsByStatus(@RequestParam PaymentStatus status) { ... }
// Only one can be invoked!
```

**Fix Required**: Use request param in single method:
```java
@GetMapping("/recentPayments")
public List<PaymentResponseDto> getRecentPayments(
    @RequestParam(required = false) PaymentStatus status) {
    if (status != null) {
        return adminService.getPaymentsByStatus(status);
    }
    return adminService.getRecentPayments();
}
```

### 5. **Missing @Transactional** 🔴

#### Issue 5a: Incomplete Transactional Coverage
```java
// ❌ AdminServiceImpl - createAdmin has no @Transactional
@Override
public AdminResponseDto createAdmin(AdminDto adminDto) {
    // If this fails halfway, data can be inconsistent
    if(adminRepo.existsByEmail(adminDto.email())){
        throw new UserAlreadyExistsException("User already exists");
    }
    User user = new User();
    user.setEmail(adminDto.email());
    user.setPasswordHash(passwordEncoder.encode(adminDto.password()));
    user.setStatus(UserStatus.ACTIVE);
    user.setRole(Role.ADMIN);
    userRepo.save(user);

    Admin admin = adminMapper.toEntity(adminDto);
    admin.setUser(user);
    Admin savedAdmin = adminRepo.save(admin);  // If fails here, User already saved
    return adminMapper.toResponseDto(savedAdmin);
}
```

**Fix Required**: Add @Transactional to ensure atomicity:
```java
@Override
@Transactional
public AdminResponseDto createAdmin(AdminDto adminDto) {
    // Now if any step fails, everything rolls back
    ...
}
```

---

## ⚠️ MAJOR ISSUES (Should Fix Before Production)

### 6. **Incomplete Code & Commented Methods**

#### Issue 6a: Commented Endpoints
```java
// ❌ Multiple commented-out endpoints in CustomerController
// @GetMapping
// public CustomerResponseDto getCustomerByEmail(@RequestParam String email) { ... }

// @GetMapping("/all")
// public Iterable<CustomerResponseDto> getAllCustomers() { ... }

// @DeleteMapping("/{id}")
// public void deleteCustomer(@PathVariable Long id) { ... }
```

**Fix Required**:
- Either implement them or remove them
- Don't commit commented code - use version control
- Document why features are incomplete

#### Issue 6b: AdminServiceImpl Incomplete
```java
@Override
public void deleteAdmin(Long adminId) {
    // ❌ Method ends abruptly, implementation cut off
```

**Fix Required**: Complete all interface method implementations.

### 7. **Missing Logging** ⚠️

```java
// ❌ No logging anywhere in the codebase
@Override
@Transactional
public CustomerResponseDto createCustomer(CustomerDto customerDto) {
    // No audit trail, no debugging capability
    if (userRepo.existsByEmail(customerDto.email())) {
        throw new UserAlreadyExistsException("A customer with this email already exists");
    }
    // ... creates user without logging
}
```

**Fix Required**: Add comprehensive logging:
```java
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerDto customerDto) {
        log.info("Creating customer with email: {}", customerDto.email());
        
        if (userRepo.existsByEmail(customerDto.email())) {
            log.warn("Customer creation failed - email already exists: {}", customerDto.email());
            throw new UserAlreadyExistsException("A customer with this email already exists");
        }
        
        User user = new User();
        // ... setup ...
        User savedUser = userRepo.save(user);
        log.debug("User saved with ID: {} and role: CUSTOMER", savedUser.getId());
        
        Customer customer = customerMapper.toEntity(customerDto);
        customer.setUser(savedUser);
        Customer savedCustomer = customerRepo.save(customer);
        
        log.info("Customer created successfully - ID: {}, Email: {}", 
                 savedCustomer.getId(), savedCustomer.getUser().getEmail());
        
        return customerMapper.toResponseDto(savedCustomer);
    }
}
```

### 8. **Missing Business Logic Validations** ⚠️

```java
// ❌ No business rules enforced
@Override
public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    // No checks for:
    // - If customer/merchant accounts are ACTIVE
    // - If payment amount is reasonable
    // - If merchant is authorized to accept payments
    // - Duplicate payment prevention
    
    Customer customer = customerRepo.findById(paymentRequestDto.customerId())
        .orElseThrow(() -> new UserNotFoundException("Customer not found"));
    
    Merchant merchant = merchantRepo.findById(paymentRequestDto.merchantId())
        .orElseThrow(() -> new UserNotFoundException("Merchant not found"));

    Payment payment = paymentMapper.toEntity(paymentRequestDto);
    // ... save without validation
}
```

**Fix Required**: Add business validations:
```java
@Override
@Transactional
public PaymentResponseDto createPayment(PaymentRequestDto paymentRequestDto) {
    log.info("Creating payment from customer {} to merchant {}, amount: {}",
             paymentRequestDto.customerId(), 
             paymentRequestDto.merchantId(),
             paymentRequestDto.amount());
    
    // Validate customer
    Customer customer = customerRepo.findById(paymentRequestDto.customerId())
        .orElseThrow(() -> new UserNotFoundException("Customer not found"));
    
    if (customer.getUser().getStatus() != UserStatus.ACTIVE) {
        log.warn("Payment blocked - customer inactive: {}", customer.getId());
        throw new InvalidOperationException("Customer account is inactive");
    }
    
    // Validate merchant
    Merchant merchant = merchantRepo.findById(paymentRequestDto.merchantId())
        .orElseThrow(() -> new UserNotFoundException("Merchant not found"));
    
    if (merchant.getUser().getStatus() != UserStatus.ACTIVE) {
        throw new InvalidOperationException("Merchant account is inactive");
    }
    
    // Validate amount
    if (paymentRequestDto.amount().compareTo(BigDecimal.ZERO) <= 0) {
        throw new InvalidOperationException("Payment amount must be positive");
    }
    
    if (paymentRequestDto.amount().compareTo(new BigDecimal("999999.99")) > 0) {
        throw new InvalidOperationException("Payment amount exceeds maximum limit");
    }
    
    // Proceed with payment
    Payment payment = paymentMapper.toEntity(paymentRequestDto);
    // ... rest of implementation
}
```

### 9. **No Query Optimization** ⚠️

```java
// ❌ Lazy loading without proper handling
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "customer_id", nullable = false)
private Customer customer;
```

**Issue**: N+1 problem - each payment query will cause additional queries to load customer.

**Fix Required**: 
```java
// In repository
@Query("SELECT p FROM Payment p LEFT JOIN FETCH p.customer LEFT JOIN FETCH p.merchant")
List<Payment> findAllWithDetails();

// Or use DTO projection
public interface PaymentProjection {
    Long getId();
    String getPaymentReference();
    BigDecimal getAmount();
    PaymentStatus getStatus();
    // ... other fields
}
```

---

## 🟡 IMPROVEMENT OPPORTUNITIES (Nice to Have)

### 10. **No Unit/Integration Tests**
```java
// ❌ Only one empty test exists
@SpringBootTest
class PayguardApplicationTests {
    @Test
    void contextLoads() { }
}
```

**Recommendation**:
- Write unit tests for services
- Write integration tests for repositories
- Aim for 70%+ code coverage
- Test edge cases and error scenarios

Example:
```java
@SpringBootTest
class CustomerServiceTest {
    @MockBean
    private CustomerRepository customerRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Test
    void testCreateCustomer_Success() {
        // Arrange
        CustomerDto dto = new CustomerDto("John", "Doe", "john@example.com", "SecurePass123!");
        
        // Act
        CustomerResponseDto result = customerService.createCustomer(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals("john@example.com", result.email());
    }
    
    @Test
    void testCreateCustomer_DuplicateEmail_ThrowsException() {
        // When email exists, should throw UserAlreadyExistsException
    }
}
```

### 11. **SQL Injection Risk - String Parameters**
```java
// ⚠️ Potential risk
@GetMapping("/status")
public List<PaymentResponseDto> getPaymentsByStatus(@RequestParam String status) {
    return paymentService.getPaymentByStatus(status);  // String passed directly
}
```

**Better Approach**: Use Enum
```java
@GetMapping("/status")
public List<PaymentResponseDto> getPaymentsByStatus(
    @RequestParam PaymentStatus status) {  // Type-safe enum
    return paymentService.getPaymentByStatus(status);
}
```

### 12. **No API Documentation**
**Recommendation**: Add **Springdoc OpenAPI** (Swagger):
```xml
<!-- In pom.xml -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

Add annotations:
```java
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {
    
    @PostMapping
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "409", description = "Customer already exists")
    public ResponseEntity<CustomerResponseDto> createCustomer(
        @Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(customerService.createCustomer(customerDto));
    }
}
```

Access at: `http://localhost:8080/swagger-ui.html`

### 13. **Missing Audit Trail**
```java
// ✅ RECOMMENDATION: Add audit fields to entities
@Entity
@Table(name = "payments")
public class Payment {
    // ... existing fields ...
    
    @Column(nullable = false, updatable = false)
    private String createdBy;  // Who created this payment
    
    @Column
    private String updatedBy;  // Who last updated it
    
    private String changeReason;  // Why was it changed
}
```

### 14. **No Rate Limiting**
**Recommendation**: Add rate limiting for production:
```xml
<dependency>
    <groupId>io.github.bucket4j</groupId>
    <artifactId>bucket4j-core</artifactId>
    <version>7.6.0</version>
</dependency>
```

### 15. **Configuration Management**
**Recommendation**: Externalize configuration:
```properties
# application-prod.properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# Payment settings
app.payment.max-amount=999999.99
app.payment.timeout=30000
app.payment.retry-attempts=3
```

---

## 🚀 PRODUCTION READINESS CHECKLIST

- [ ] ✅ Implement JWT/OAuth authentication
- [ ] ✅ Add @PreAuthorize role-based access control to all endpoints
- [ ] ✅ Add input validation (@Valid, validation annotations)
- [ ] ✅ Create centralized exception handler (@ControllerAdvice)
- [ ] ✅ Add @Transactional to all state-changing operations
- [ ] ✅ Fix duplicate URL mappings
- [ ] ✅ Standardize API paths to /api/v1/* convention
- [ ] ✅ Remove commented code
- [ ] ✅ Complete all interface implementations
- [ ] ✅ Add comprehensive logging
- [ ] ✅ Add business logic validations
- [ ] ✅ Move credentials to environment variables
- [ ] ✅ Write unit/integration tests (70%+ coverage)
- [ ] ✅ Add Swagger/OpenAPI documentation
- [ ] ✅ Implement error response consistency
- [ ] ✅ Add database connection pooling
- [ ] ✅ Add actuator endpoints for monitoring
- [ ] ✅ Set up CI/CD pipeline
- [ ] ✅ Add performance monitoring/APM
- [ ] ✅ Implement audit logging
- [ ] ✅ Set up rate limiting

---

## 📋 PRIORITY FIX ORDER

**Phase 1 - Critical (Do First)**:
1. Implement authentication & authorization (Security)
2. Add input validation
3. Create global exception handler
4. Move secrets out of version control
5. Fix duplicate route mappings

**Phase 2 - High Priority (Do Before Production)**:
6. Add @Transactional consistently
7. Complete incomplete code
8. Add comprehensive logging
9. Standardize API paths
10. Add business logic validations

**Phase 3 - Important (Before Launch)**:
11. Write tests
12. Add API documentation
13. Performance optimization
14. Monitoring setup
15. Audit trail implementation

---

## 📊 OVERALL ASSESSMENT

| Aspect | Rating | Status |
|--------|--------|--------|
| Architecture | ⭐⭐⭐⭐⭐ | Excellent |
| Code Quality | ⭐⭐⭐⭐ | Good |
| Security | ⭐ | Critical - Must Fix |
| Testing | ⭐ | Non-existent |
| Documentation | ⭐⭐ | Minimal |
| Error Handling | ⭐⭐ | Basic |
| Logging | ⭐ | Missing |
| Production Ready | ❌ | **NO** |

**Conclusion**: Your project has an **excellent foundation** with proper architecture and design patterns. However, it requires **significant security hardening, validation, and error handling** before it's ready for production. With the fixes outlined above (particularly Phase 1), you can make this production-ready within 2-3 weeks.

---

## ✨ Additional Best Practices

### Performance
- Use pagination for list endpoints
- Implement caching where appropriate
- Use database indexes on frequently queried columns
- Monitor slow queries

### Security
- Implement HTTPS enforcement
- Add CORS configuration
- Implement API key management
- Add OWASP header security

### Maintainability
- Follow naming conventions consistently
- Add code comments for complex logic
- Keep service methods focused (Single Responsibility)
- Use constants for magic numbers/strings

Good luck with production deployment! 🚀
