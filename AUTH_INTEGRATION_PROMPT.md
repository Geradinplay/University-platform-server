# Промт для интеграции системы Регистрации и Авторизации

## Архитектура системы Auth/Register

### 1. СТРУКТУРА СЛОЕВ

#### API Layer (Controller) - `/api/auth`
```
AuthController (@RestController)
├── POST /register - регистрация нового пользователя
├── POST /login - вход в систему с получением JWT токена
└── Зависимости:
    ├── AuthService (бизнес-логика)
    ├── JwtService (генерация токенов)
    └── UserMapper (преобразование Entity <-> DTO)
```

#### Service Layer
```
AuthService (@Service)
├── register(RegisterRequest) -> UserEntity
│   ├── Проверка existsByUsername
│   ├── Проверка existsByEmail
│   ├── Хеширование пароля (PasswordEncoder)
│   ├── Сохранение в БД с обработкой DataIntegrityViolationException
│   └── По умолчанию: role=USER, status=ACTIVE
│
└── authenticate(username, password) -> UserEntity
    ├── Поиск по username
    ├── Проверка пароля (PasswordEncoder.matches)
    ├── Проверка статуса (не BANNED)
    └── Выброс AuthException при ошибке

JwtService (@Service)
├── generateToken(UserEntity) -> String
│   ├── Claims: id, role, status
│   ├── Subject: username
│   ├── Expiration: 24 часа
│   └── Algorithm: HS256
│
└── extractUserName(token) -> String
└── isTokenValid(token) -> boolean
```

#### Repository Layer
```
UserRepo extends JpaRepository<UserEntity, Long>
├── findByUsername(String) -> Optional<UserEntity>
├── existsByUsername(String) -> boolean
└── existsByEmail(String) -> boolean
```

### 2. ENTITY: UserEntity

**Таблица:** `users`
**Location:** `repository/src/main/java/com/example/entity/UserEntity.java`

```java
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;           // Логин (индексирован)
    
    @Column(nullable = false, unique = true)
    private String email;              // Email (индексирован)
    
    @Column(nullable = false)
    private String password;           // Хэш пароля (bcrypt)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;             // USER или ADMIN (по умолчанию USER)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;         // ACTIVE или BANNED (по умолчанию ACTIVE)
}
```

**Enums:**
- `UserRole`: USER, ADMIN
- `UserStatus`: ACTIVE, BANNED

### 3. DTO (Data Transfer Objects)

#### RegisterRequest
```java
@Data
public class RegisterRequest {
    private String username;  // Обязателен
    private String email;     // Обязателен
    private String password;  // Обязателен, хэшируется на сервере
}
```

#### LoginRequest
```java
@Data
public class LoginRequest {
    private String username;  // Обязателен
    private String password;  // Обязателен
}
```

#### UserDTO (ответ при регистрации)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;      // "USER" или "ADMIN"
    private Boolean isBanned;  // true если status == BANNED
}
```

#### AuthResponse (ответ при логине)
```java
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;  // JWT токен
}
```

### 4. API ENDPOINTS

#### POST /api/auth/register
**Request:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "isBanned": false
}
```

**Error Responses:**
- **400 Bad Request** - если username/email уже существуют
- **400 Bad Request** - при нарушении целостности данных (race condition)

**Обработанные исключения:**
- `UserAlreadyExistsException` - логин или email заняты
- `DataIntegrityViolationException` - нарушение уникальности на уровне БД (обработка race condition)

---

#### POST /api/auth/login
**Request:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwicm9sZSI6IlVTRVIiLCJzdGF0dXMiOiJBQ1RJVkUiLCJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTcwODkzNDI3MCwiZXhwIjoxNzA4OTQxMjcwfQ.signature"
}
```

**Error Responses:**
- **401 Unauthorized** - неверный username или пароль
- **401 Unauthorized** - аккаунт заблокирован (status = BANNED)

**Обработанные исключения:**
- `AuthException` - неверные учетные данные или заблокирован аккаунт

---

### 5. JWT TOKEN STRUCTURE

**Алгоритм:** HS256
**Время жизни:** 24 часа
**Секретный ключ:** из `application.properties` параметр `token.signing.key`

**Claims (полезная нагрузка):**
```
{
  "id": <Long>,           // ID пользователя
  "role": "USER|ADMIN",   // Роль пользователя
  "status": "ACTIVE|BANNED", // Статус аккаунта
  "sub": "<username>",    // Subject (имя пользователя)
  "iat": <timestamp>,     // Issued at
  "exp": <timestamp>      // Expiration
}
```

**Использование токена в запросах:**
```
Authorization: Bearer <token>
```

---

### 6. ОБРАБОТКА ИСКЛЮЧЕНИЙ

**GlobalExceptionHandler** (`api/src/main/java/com/example/exception/GlobalExceptionHandler.java`)

```
@ExceptionHandler(UserAlreadyExistsException.class)
  ↓
  400 Bad Request + сообщение об ошибке

@ExceptionHandler(DataIntegrityViolationException.class)
  ↓
  400 Bad Request + "Username is already taken" или "Email is already in use"

@ExceptionHandler(AuthException.class)
  ↓
  401 Unauthorized + сообщение об ошибке

@ExceptionHandler(Exception.class)
  ↓
  500 Internal Server Error
```

---

### 7. PROCESS FLOW

#### РЕГИСТРАЦИЯ
```
1. POST /api/auth/register с RegisterRequest
2. AuthController.register()
   ├── authService.register(request)
   │   ├── Check existsByUsername(username)
   │   ├── Check existsByEmail(email)
   │   ├── Create UserEntity
   │   ├── passwordEncoder.encode(password)
   │   ├── userRepository.save(user)  // <-- может выбросить DataIntegrityViolationException
   │   └── return UserEntity
   ├── userMapper.toDto(userEntity)
   └── ResponseEntity.ok(userDTO)
3. GlobalExceptionHandler ловит исключения
4. Возвращает UserDTO или ошибку 400
```

#### АВТОРИЗАЦИЯ
```
1. POST /api/auth/login с LoginRequest
2. AuthController.login()
   ├── authService.authenticate(username, password)
   │   ├── userRepository.findByUsername(username)
   │   ├── passwordEncoder.matches(password, hashedPassword)
   │   ├── Check status != BANNED
   │   └── return UserEntity
   ├── jwtService.generateToken(user)
   └── ResponseEntity.ok(new AuthResponse(token))
3. GlobalExceptionHandler ловит AuthException
4. Возвращает AuthResponse с токеном или ошибку 401
```

---

### 8. КОНФИГУРАЦИЯ И ЗАВИСИМОСТИ

**application.properties:**
```properties
# JWT Secret (BASE64 encoded)
token.signing.key=${JWT_SECRET:2ISC2VL5vm45TU5M81cPRQKiUev49FUDF9n3WK2ahoQ=}

# Spring Security (PasswordEncoder bean должен быть сконфигурирован)
# Обычно используется BCryptPasswordEncoder
```

**Dependencies (в build.gradle):**
```gradle
// Spring Security (для PasswordEncoder)
implementation 'org.springframework.boot:spring-boot-starter-security'

// JWT (jjwt)
implementation 'io.jsonwebtoken:jjwt-api:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.3'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.3'

// Lombok (для @Data, @AllArgsConstructor и т.д.)
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'
```

---

### 9. SECURITY FEATURES

✅ **Реализовано:**
- Хеширование паролей (bcrypt через PasswordEncoder)
- JWT токены с истечением (24 часа)
- Проверка уникальности username и email (дублирование на уровне БД и приложения)
- Обработка race condition при одновременной регистрации
- Проверка статуса аккаунта (BANNED)
- Обработка исключений с информативными сообщениями

⚠️ **Требует добавления:**
- JWT фильтр для защиты других эндпоинтов (JwtAuthenticationFilter)
- Проверка токена в каждом запросе к protected эндпоинтам
- Refresh токены (опционально)
- Rate limiting на эндпоинты auth (защита от brute force)
- Логирование попыток входа

---

### 10. ТЕСТИРОВАНИЕ

#### cURL примеры

**Регистрация:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Логин:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Использование токена:**
```bash
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer <token_from_login>"
```

---

## КАК ИНТЕГРИРОВАТЬ В НОВЫЕ МОДУЛИ

### Шаг 1: Добавить JWT фильтр для защиты эндпоинтов
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepo userRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     FilterChain filterChain) {
        String token = extractTokenFromHeader(request);
        if (token != null && jwtService.isTokenValid(token)) {
            String username = jwtService.extractUserName(token);
            UserEntity user = userRepository.findByUsername(username).orElse(null);
            // Установить SecurityContext
        }
        filterChain.doFilter(request, response);
    }
}
```

### Шаг 2: Защитить эндпоинты через @PreAuthorize
```java
@PreAuthorize("hasRole('USER')")
@GetMapping("/my-data")
public ResponseEntity<?> getMyData() {
    // Только авторизованные пользователи
}

@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/admin/users/{id}")
public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    // Только администраторы
}
```

### Шаг 3: Получить текущего пользователя в контроллере
```java
@GetMapping("/profile")
public ResponseEntity<UserDTO> getProfile(
    @AuthenticationPrincipal UserEntity currentUser) {
    return ResponseEntity.ok(userMapper.toDto(currentUser));
}

// Или через SecurityContext
UserEntity currentUser = (UserEntity) 
    SecurityContextHolder.getContext()
    .getAuthentication()
    .getPrincipal();
```

### Шаг 4: Добавить в конфиг Spring Security
```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf().disable()
           .authorizeHttpRequests((authz) -> authz
               .requestMatchers("/api/auth/**").permitAll()
               .anyRequest().authenticated()
           )
           .addFilterBefore(jwtAuthenticationFilter, 
               UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
```

---

## CHECKLIST ДЛЯ ИНТЕГРАЦИИ

- [ ] Скопировать/создать DTO классы (RegisterRequest, LoginRequest, AuthResponse, UserDTO)
- [ ] Создать UserEntity с полями: id, username, email, password, role, status
- [ ] Создать Enums: UserRole (USER, ADMIN), UserStatus (ACTIVE, BANNED)
- [ ] Создать UserRepository с методами: findByUsername, existsByUsername, existsByEmail
- [ ] Создать AuthService с методами: register, authenticate
- [ ] Создать JwtService с методами: generateToken, extractUserName, isTokenValid
- [ ] Создать AuthController с эндпоинтами: /register, /login
- [ ] Создать UserMapper для преобразования Entity <-> DTO
- [ ] Обновить GlobalExceptionHandler: добавить обработчики для UserAlreadyExistsException, DataIntegrityViolationException, AuthException
- [ ] Настроить Liquibase миграцию для создания таблицы users с индексами
- [ ] Добавить PasswordEncoder bean в конфиг
- [ ] Настроить JWT секретный ключ в application.properties
- [ ] Создать JwtAuthenticationFilter для защиты protected эндпоинтов
- [ ] Настроить SecurityConfig для интеграции фильтра
- [ ] Добавить @PreAuthorize к защищенным методам
- [ ] Протестировать через cURL или Postman

---

## ССЫЛКИ НА КЛАССЫ В ПРОЕКТЕ

- **Controller:** `api/src/main/java/com/example/controller/AuthController.java`
- **Service:** `service/src/main/java/com/example/service/AuthService.java`
- **JWT Service:** `service/src/main/java/com/example/service/JwtService.java`
- **Entity:** `repository/src/main/java/com/example/entity/UserEntity.java`
- **Repository:** `repository/src/main/java/com/example/repository/UserRepo.java`
- **DTOs:** `infrastructure/src/main/java/com/example/dto/{RegisterRequest, LoginRequest, AuthResponse, UserDTO}.java`
- **Mapper:** `infrastructure/src/main/java/com/example/mapper/UserMapper.java`
- **Exception Handler:** `api/src/main/java/com/example/exception/GlobalExceptionHandler.java`
- **DB Migration:** `app/src/main/resources/changelog/changes/008-create-users.yaml`
- **Config:** `app/src/main/resources/application.properties`

---

*Создано: 2026-02-09 00:00*
*Система: Spring Boot 3.x + Spring Security 6.x + jjwt*

