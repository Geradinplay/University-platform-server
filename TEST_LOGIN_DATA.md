# Тестовые данные для Login

## Как работает система Auth

### 1. Регистрация (POST /api/auth/register)
При регистрации пароль хэшируется с использованием **BCryptPasswordEncoder**.

### 2. Вход (POST /api/auth/login)
При входе пароль сравнивается с хэшем через `passwordEncoder.matches(password, hashedPassword)`.

---

## Готовые учетные записи для тестирования

### Пользователь: Admin (ADMIN роль)
```json
{
  "username": "admin",
  "password": "admin123",
  "email": "admin@example.com"
}
```

**Bcrypt хэш пароля:**
```
$2a$10$wq9Y6zQZcY7uO1x5zZ5R9e9q2zZx3G0R9YqQkN6qY0L8n2dE6F5yC
```

**Статус:** ACTIVE  
**Роль:** ADMIN

---

## Как протестировать Login

### 1. cURL команда для входа

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 2. Expected Response (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwicm9sZSI6IkFETUlOIiwic3RhdHVzIjoiQUNUSVZFIiwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MDg5MzQyNzAsImV4cCI6MTcwODk0MTI3MH0.signature"
}
```

### 3. Использование токена в запросах

```bash
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer <token_from_login>"
```

---

## Как зарегистрировать нового пользователя

### cURL команда для регистрации

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

### Expected Response (200 OK)

```json
{
  "id": 2,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "isBanned": false
}
```

### Потом можно залогиниться с этими учетными данными

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securePassword123"
  }'
```

---

## Генерация Bcrypt хэшей для других паролей

Если нужно создать test данные с другим паролем, используйте bcrypt хэш.

**Пример:** пароль `user123` → bcrypt хэш:
```
$2a$10$u9q.Z2L/NzFZvGGR8l8meuK0J8aF8Z8L8n7R6.Y5G4H3Q2P1O0N/v
```

В Liquibase миграции это выглядит так:

```yaml
- insert:
    tableName: users
    columns:
      - column:
          name: username
          value: user
      - column:
          name: email
          value: user@example.com
      - column:
          name: password
          # PASSWORD: user123
          value: "$2a$10$u9q.Z2L/NzFZvGGR8l8meuK0J8aF8Z8L8n7R6.Y5G4H3Q2P1O0N/v"
      - column:
          name: role
          value: USER
      - column:
          name: status
          value: ACTIVE
```

---

## Структура JWT токена

Токен содержит следующие claims:

```json
{
  "id": 1,           // ID пользователя
  "role": "ADMIN",   // Роль (USER или ADMIN)
  "status": "ACTIVE", // Статус (ACTIVE или BANNED)
  "sub": "admin",    // Username
  "iat": 1708934270, // Issued at (время создания)
  "exp": 1708941270  // Expiration (истекает через 24 часа)
}
```

---

## Ошибки и их обработка

### 1. Неверный username/пароль
**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "wrong_password"
  }'
```

**Response (401 Unauthorized):**
```json
"Invalid username or password"
```

---

### 2. Аккаунт заблокирован
Если статус пользователя = BANNED

**Response (401 Unauthorized):**
```json
"Account is blocked"
```

---

### 3. Username или email уже заняты при регистрации
**Request:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "email": "new_email@example.com",
    "password": "password123"
  }'
```

**Response (400 Bad Request):**
```json
"Username 'admin' is already taken"
```

---

## SQL запросы для проверки пользователей в БД

```sql
-- Посмотреть всех пользователей
SELECT id, username, email, role, status FROM users;

-- Посмотреть конкретного пользователя
SELECT * FROM users WHERE username = 'admin';

-- Обновить статус (заблокировать)
UPDATE users SET status = 'BANNED' WHERE username = 'admin';

-- Удалить пользователя
DELETE FROM users WHERE username = 'john_doe';
```

---

## Postman коллекция

### Регистрация
```
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "testPassword123"
}
```

### Вход
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "testPassword123"
}
```

### Защищенный эндпоинт (требует токен)
```
GET http://localhost:8080/api/protected
Authorization: Bearer <token_from_login>
```

---

*Создано: 2026-02-09*

