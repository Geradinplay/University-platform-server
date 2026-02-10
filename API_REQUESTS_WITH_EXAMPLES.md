# 📡 API ЗАПРОСЫ С ПРИМЕРАМИ ТЕЛ ЗАПРОСОВ

**Версия:** Spring Boot 3.x + Spring Security 6.x  
**URL базового сервера:** `http://localhost:8080`  
**Дата:** 2026-02-09

---

## 🔐 АУТЕНТИФИКАЦИЯ (Authentication)

### POST /api/auth/register
**Описание:** Регистрация нового пользователя

**Request Body:**
```json
{
  "username": "newuser",
  "name": "Новый Пользователь",
  "email": "newuser@example.com",
  "password": "securePassword123"
}
```

**Поля поддерживают кириллицу:**
```json
{
  "username": "иванов",
  "name": "Иван Иванович Иванов",
  "email": "ivan@example.com",
  "password": "пароль123"
}
```

**Или без поля name (будет использован username):**
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "securePassword123"
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "username": "newuser",
  "name": "Новый Пользователь",
  "email": "newuser@example.com",
  "role": "USER",
  "isBanned": false,
  "professorId": null
}
```

**Response 400 Bad Request (если юзер существует):**
```json
{
  "error": "Username 'newuser' is already taken"
}
```

---

### POST /api/auth/login
**Описание:** Вход в систему и получение JWT токена

**Request Body:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response 200 OK:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlkIjoxLCJyb2xlIjoiQURNSU4iLCJzdGF0dXMiOiJBQ1RJVkUiLCJpYXQiOjE3Mzk2MTIzMTIsImV4cCI6MTczOTY5ODcxMn0.pZhQc1d5gZ9eQ2pL8sR3nV4jK1wM6uT8oY2xA5bC9dE",
  "user": {
    "id": 1,
    "username": "admin",
    "name": "Администратор",
    "email": "admin@example.com",
    "role": "ADMIN",
    "isBanned": false,
    "professorId": null
  }
}
```

**Response 401 Unauthorized:**
```json
{
  "error": "Invalid username or password"
}
```

---

### GET /api/auth/me
**Описание:** Получить текущего пользователя по JWT токену

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Response 200 OK:**
```json
{
  "id": 1,
  "username": "newton",
  "name": "Исаак Ньютон",
  "email": "newton@example.com",
  "role": "ADMIN",
  "isBanned": false,
  "professorId": 1
}
```

**Response 401 Unauthorized (неверный или отсутствует токен):**
```json
{
  "error": "Unauthorized"
}
```

**Response 404 Not Found (пользователь удален из БД):**
```json
{
  "error": "User not found"
}
```

---

## 🏫 ФАКУЛЬТЕТЫ (Faculties)

### GET /api/faculties
**Описание:** Получить все факультеты

**Headers:**
```
Content-Type: application/json
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Факультет Информатики",
    "shortName": "ИМ"
  },
  {
    "id": 2,
    "name": "Факультет Математики",
    "shortName": "МА"
  },
  {
    "id": 3,
    "name": "Факультет Физики",
    "shortName": "ФИ"
  }
]
```

---

### GET /api/faculties/{id}
**Описание:** Получить факультет по ID

**Path Parameters:**
- `id` (Long, required) - ID факультета

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Факультет Информатики",
  "shortName": "ИМ"
}
```

**Response 404 Not Found:**
```json
{
  "error": "Faculty not found with id: 999"
}
```

---

### POST /api/faculties
**Описание:** Создать новый факультет

**Request Body:**
```json
{
  "name": "Факультет Экономики",
  "shortName": "ЭК"
}
```

**Response 201 Created:**
```json
{
  "id": 4,
  "name": "Факультет Экономики",
  "shortName": "ЭК"
}
```

**Response 400 Bad Request:**
```json
{
  "error": "Название факультета не может быть пустым"
}
```

---

### PUT /api/faculties/{id}
**Описание:** Обновить факультет

**Path Parameters:**
- `id` (Long, required) - ID факультета

**Request Body:**
```json
{
  "name": "Факультет Прикладной Информатики",
  "shortName": "ПИ"
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Факультет Прикладной Информатики",
  "shortName": "ПИ"
}
```

---

### DELETE /api/faculties/{id}
**Описание:** Удалить факультет

**Path Parameters:**
- `id` (Long, required) - ID факультета

**Response 204 No Content:**
```
(пустой ответ)
```

**Response 404 Not Found:**
```json
{
  "error": "Faculty with id 999 not found"
}
```

---

## 📚 ПРЕДМЕТЫ (Subjects)

### GET /api/subjects
**Описание:** Получить все предметы

**Headers:**
```
Content-Type: application/json
```

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Математика"
  },
  {
    "id": 2,
    "name": "Физика"
  },
  {
    "id": 3,
    "name": "Информатика"
  }
]
```

---

### POST /api/subjects
**Описание:** Создать новый предмет

**Request Body:**
```json
{
  "name": "Английский язык"
}
```

**Response 201 Created:**
```json
{
  "id": 4,
  "name": "Английский язык"
}
```

**Response 400 Bad Request (если предмет уже существует):**
```json
{
  "error": "Предмет с таким названием уже существует"
}
```

---

### PUT /api/subjects/{id}
**Описание:** Обновить предмет по ID

**Пример URL:** `PUT /api/subjects/1`

**Request Body:**
```json
{
  "name": "Высшая математика"
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Высшая математика"
}
```

---

### DELETE /api/subjects/{id}
**Описание:** Удалить предмет по ID

**Пример URL:** `DELETE /api/subjects/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

**Response 404 Not Found:**
```json
{
  "error": "Subject not found with id: 1"
}
```

---

## 👨‍🏫 ПРЕПОДАВАТЕЛИ (Professors)

### GET /api/professors
**Описание:** Получить всех преподавателей

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Иван Петров"
  },
  {
    "id": 2,
    "name": "Мария Сидорова"
  }
]
```

---

### POST /api/professors
**Описание:** Создать нового преподавателя

**Request Body:**
```json
{
  "name": "Петр Иванов"
}
```

**Response 201 Created:**
```json
{
  "id": 3,
  "name": "Петр Иванов"
}
```

---

### PUT /api/professors/{id}
**Описание:** Обновить преподавателя по ID

**Пример URL:** `PUT /api/professors/1`

**Request Body:**
```json
{
  "name": "Иван Петров Викторович"
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Иван Петров Викторович"
}
```

---

### DELETE /api/professors/{id}
**Описание:** Удалить преподавателя по ID

**Пример URL:** `DELETE /api/professors/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

---

## 🏢 АУДИТОРИИ (Classrooms)

### GET /api/classrooms
**Описание:** Получить все аудитории

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "number": "101"
  },
  {
    "id": 2,
    "number": "205"
  },
  {
    "id": 3,
    "number": "301"
  }
]
```

---

### POST /api/classrooms
**Описание:** Создать новую аудиторию

**Request Body:**
```json
{
  "number": "401"
}
```

**Response 201 Created:**
```json
{
  "id": 4,
  "number": "401"
}
```

---

### PUT /api/classrooms/{id}
**Описание:** Обновить аудиторию по ID

**Пример URL:** `PUT /api/classrooms/1`

**Request Body:**
```json
{
  "number": "102"
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "number": "102"
}
```

---

### DELETE /api/classrooms/{id}
**Описание:** Удалить аудиторию по ID

**Пример URL:** `DELETE /api/classrooms/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

---

## 📅 РАСПИСАНИЯ (Schedules)

### GET /api/schedules
**Описание:** Получить все расписания

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "name": "Расписание 1 группы",
    "facultyId": 1
  },
  {
    "id": 2,
    "name": "Расписание 2 группы",
    "facultyId": 1
  }
]
```

---

### POST /api/schedules
**Описание:** Создать новое расписание

**Request Body:**
```json
{
  "name": "Расписание 3 группы",
  "facultyId": 1
}
```

**Response 201 Created:**
```json
{
  "id": 3,
  "name": "Расписание 3 группы",
  "facultyId": 1
}
```

---

### GET /api/schedules/{id}
**Описание:** Получить расписание по ID

**Пример URL:** `GET /api/schedules/1`

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Расписание 1 группы",
  "facultyId": 1
}
```

---

### PUT /api/schedules/{id}
**Описание:** Обновить расписание по ID

**Пример URL:** `PUT /api/schedules/1`

**Request Body:**
```json
{
  "name": "Обновлённое расписание 1 группы",
  "facultyId": 2
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "name": "Обновлённое расписание 1 группы",
  "facultyId": 2
}
```

---

### DELETE /api/schedules/{id}
**Описание:** Удалить расписание по ID

**Пример URL:** `DELETE /api/schedules/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

---

### GET /api/schedules/{id}/lessons
**Описание:** Получить все занятия для конкретного расписания

**Пример URL:** `GET /api/schedules/1/lessons`

**Path Parameters:**
- `id` (Long, required) - ID расписания

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "startTime": "09:00",
    "endTime": "10:30",
    "day": 1,
    "subject": {
      "id": 1,
      "name": "Математика"
    },
    "professor": {
      "id": 1,
      "name": "Иван Петров"
    },
    "classroom": {
      "id": 1,
      "number": "101"
    },
    "scheduleId": 1
  },
  {
    "id": 2,
    "startTime": "10:45",
    "endTime": "12:15",
    "day": 1,
    "subject": {
      "id": 2,
      "name": "Физика"
    },
    "professor": {
      "id": 2,
      "name": "Мария Сидорова"
    },
    "classroom": {
      "id": 2,
      "number": "205"
    },
    "scheduleId": 1
  }
]
```

**Response 404 Not Found (если расписание не найдено):**
```json
{
  "error": "Schedule not found with id: 999"
}
```

---

### GET /api/schedules/{id}/breaks
**Описание:** Получить все перерывы для конкретного расписания

**Пример URL:** `GET /api/schedules/1/breaks`

**Path Parameters:**
- `id` (Long, required) - ID расписания

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "startTime": "10:30",
    "endTime": "10:45",
    "day": 1,
    "scheduleId": 1
  },
  {
    "id": 2,
    "startTime": "12:15",
    "endTime": "12:30",
    "day": 1,
    "scheduleId": 1
  }
]
```

**Response 404 Not Found (если расписание не найдено):**
```json
{
  "error": "Schedule not found with id: 999"
}
```

---

## 📝 ЗАНЯТИЯ (Lessons/Schedule)

### GET /api/schedule
**Описание:** Получить все занятия

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "startTime": "09:00",
    "endTime": "10:30",
    "day": 1,
    "subject": {
      "id": 1,
      "name": "Математика"
    },
    "professor": {
      "id": 1,
      "name": "Иван Петров"
    },
    "classroom": {
      "id": 1,
      "number": "101"
    }
  },
  {
    "id": 2,
    "startTime": "10:45",
    "endTime": "12:15",
    "day": 1,
    "subject": {
      "id": 2,
      "name": "Физика"
    },
    "professor": {
      "id": 2,
      "name": "Мария Сидорова"
    },
    "classroom": {
      "id": 2,
      "number": "205"
    }
  }
]
```

---

### POST /api/schedule
**Описание:** Создать новое занятие

**Request Body:**
```json
{
  "startTime": "09:00",
  "endTime": "10:30",
  "day": 1,
  "subjectId": 1,
  "professorId": 1,
  "classroomId": 1,
  "scheduleId": 1
}
```

**Объяснение полей:**
- `startTime` (string, HH:mm): Время начала занятия (09:00)
- `endTime` (string, HH:mm): Время окончания занятия (10:30)
- `day` (integer, 0-7): День недели (0-7, где 0 - понедельник)
- `subjectId` (integer): ID предмета
- `professorId` (integer): ID преподавателя
- `classroomId` (integer): ID аудитории
- `scheduleId` (integer): ID расписания

**Response 200 OK:**
```json
{
  "id": 1,
  "startTime": "09:00",
  "endTime": "10:30",
  "day": 1,
  "subject": {
    "id": 1,
    "name": "Математика"
  },
  "professor": {
    "id": 1,
    "name": "Иван Петров"
  },
  "classroom": {
    "id": 1,
    "number": "101"
  }
}
```

**Response 404 Not Found (если один из ID не найден):**
```json
{
  "error": "Subject not found with id: 999"
}
```

---

### PUT /api/schedule/{id}
**Описание:** Обновить занятие по ID

**Пример URL:** `PUT /api/schedule/1`

**Request Body:**
```json
{
  "startTime": "10:00",
  "endTime": "11:30",
  "day": 1,
  "subjectId": 2,
  "professorId": 2,
  "classroomId": 2,
  "scheduleId": 1
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "startTime": "10:00",
  "endTime": "11:30",
  "day": 1,
  "subject": {
    "id": 2,
    "name": "Физика"
  },
  "professor": {
    "id": 2,
    "name": "Мария Сидорова"
  },
  "classroom": {
    "id": 2,
    "number": "205"
  }
}
```

---

### DELETE /api/schedule/{id}
**Описание:** Удалить занятие по ID

**Пример URL:** `DELETE /api/schedule/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

---

## ⏱️ ПЕРЕРЫВЫ (Breaks)

### GET /api/break
**Описание:** Получить все перерывы

**Response 200 OK:**
```json
[
  {
    "id": 1,
    "startTime": "10:30",
    "endTime": "10:45",
    "day": 1,
    "scheduleId": 1
  },
  {
    "id": 2,
    "startTime": "12:15",
    "endTime": "12:30",
    "day": 1,
    "scheduleId": 1
  }
]
```

---

### POST /api/break
**Описание:** Создать новый перерыв

**Request Body:**
```json
{
  "startTime": "10:30",
  "endTime": "10:45",
  "day": 1,
  "scheduleId": 1
}
```

**Объяснение полей:**
- `startTime` (string, HH:mm): Время начала перерыва
- `endTime` (string, HH:mm): Время окончания перерыва
- `day` (integer, 0-7): День недели
- `scheduleId` (integer): ID расписания

**Response 201 Created:**
```json
{
  "id": 1,
  "startTime": "10:30",
  "endTime": "10:45",
  "day": 1,
  "scheduleId": 1
}
```

---

### PUT /api/break/{id}
**Описание:** Обновить перерыв по ID

**Пример URL:** `PUT /api/break/1`

**Request Body:**
```json
{
  "startTime": "10:35",
  "endTime": "10:50",
  "day": 1,
  "scheduleId": 1
}
```

**Response 200 OK:**
```json
{
  "id": 1,
  "startTime": "10:35",
  "endTime": "10:50",
  "day": 1,
  "scheduleId": 1
}
```

---

### DELETE /api/break/{id}
**Описание:** Удалить перерыв по ID

**Пример URL:** `DELETE /api/break/1`

**Response 204 No Content:**
```
(нет тела ответа)
```

---

## 🧪 ПРИМЕРЫ ИСПОЛЬЗОВАНИЯ С cURL

### Регистрация пользователя
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "name": "John Doe",
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

### Регистрация с кириллицей
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "иванов",
    "name": "Иван Иванович Иванов",
    "email": "ivan@example.com",
    "password": "пароль123"
  }'
```

### Регистрация без указания name (будет использован username)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jane_doe",
    "email": "jane@example.com",
    "password": "SecurePass123"
  }'
```

### Вход в систему
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "SecurePass123"
  }'
```

### Получить текущего пользователя по токену
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json"
```

### Получить все предметы
```bash
curl -X GET http://localhost:8080/api/subjects \
  -H "Content-Type: application/json"
```

### Создать предмет
```bash
curl -X POST http://localhost:8080/api/subjects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Химия"
  }'
```

### Создать преподавателя
```bash
curl -X POST http://localhost:8080/api/professors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Елена Иванова"
  }'
```

### Создать аудиторию
```bash
curl -X POST http://localhost:8080/api/classrooms \
  -H "Content-Type: application/json" \
  -d '{
    "number": "501"
  }'
```

### Создать расписание
```bash
curl -X POST http://localhost:8080/api/schedules \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Расписание 4 группы",
    "facultyId": 1
  }'
```

### Получить занятия конкретного расписания
```bash
curl -X GET http://localhost:8080/api/schedules/1/lessons \
  -H "Content-Type: application/json"
```

### Получить перерывы конкретного расписания
```bash
curl -X GET http://localhost:8080/api/schedules/1/breaks \
  -H "Content-Type: application/json"
```

### Создать занятие ⭐ (САМОЕ ВАЖНОЕ)
```bash
curl -X POST http://localhost:8080/api/schedule \
  -H "Content-Type: application/json" \
  -d '{
    "startTime": "14:00",
    "endTime": "15:30",
    "day": 2,
    "subjectId": 1,
    "professorId": 1,
    "classroomId": 1,
    "scheduleId": 1
  }'
```

### Создать перерыв
```bash
curl -X POST http://localhost:8080/api/break \
  -H "Content-Type: application/json" \
  -d '{
    "startTime": "12:00",
    "endTime": "12:15",
    "day": 1,
    "scheduleId": 1
  }'
```

### Обновить занятие
```bash
curl -X PUT http://localhost:8080/api/schedule/1 \
  -H "Content-Type: application/json" \
  -d '{
    "startTime": "15:00",
    "endTime": "16:30",
    "day": 3,
    "subjectId": 2,
    "professorId": 2,
    "classroomId": 2,
    "scheduleId": 1
  }'
```

### Удалить занятие
```bash
curl -X DELETE http://localhost:8080/api/schedule/1 \
  -H "Content-Type: application/json"
```

### С авторизацией (JWT токен)
```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## 📊 ТАБЛИЦА ВСЕх ENDPOINTS

| Метод | Endpoint | Описание | Public |
|-------|----------|---------|--------|
| POST | `/api/auth/register` | Регистрация | ✅ |
| POST | `/api/auth/login` | Вход | ✅ |
| GET | `/api/auth/me` | Получить текущего пользователя | ✅ |
| GET | `/api/subjects` | Получить все предметы | ✅ |
| POST | `/api/subjects` | Создать предмет | ✅ |
| PUT | `/api/subjects/{id}` | Обновить предмет | ✅ |
| DELETE | `/api/subjects/{id}` | Удалить предмет | ✅ |
| GET | `/api/professors` | Получить всех преподавателей | ✅ |
| POST | `/api/professors` | Создать преподавателя | ✅ |
| PUT | `/api/professors/{id}` | Обновить преподавателя | ✅ |
| DELETE | `/api/professors/{id}` | Удалить преподавателя | ✅ |
| GET | `/api/classrooms` | Получить все аудитории | ✅ |
| POST | `/api/classrooms` | Создать аудиторию | ✅ |
| PUT | `/api/classrooms/{id}` | Обновить аудиторию | ✅ |
| DELETE | `/api/classrooms/{id}` | Удалить аудиторию | ✅ |
| GET | `/api/schedules` | Получить все расписания | ✅ |
| POST | `/api/schedules` | Создать расписание | ✅ |
| GET | `/api/schedules/{id}` | Получить расписание | ✅ |
| PUT | `/api/schedules/{id}` | Обновить расписание | ✅ |
| DELETE | `/api/schedules/{id}` | Удалить расписание | ✅ |
| GET | `/api/schedules/{id}/lessons` | Получить занятия расписания | ✅ |
| GET | `/api/schedules/{id}/breaks` | Получить перерывы расписания | ✅ |
| GET | `/api/schedule` | Получить все занятия | ✅ |
| POST | `/api/schedule` | Создать занятие | ✅ |
| PUT | `/api/schedule/{id}` | Обновить занятие | ✅ |
| DELETE | `/api/schedule/{id}` | Удалить занятие | ✅ |
| GET | `/api/break` | Получить все перерывы | ✅ |
| POST | `/api/break` | Создать перерыв | ✅ |
| PUT | `/api/break/{id}` | Обновить перерыв | ✅ |
| DELETE | `/api/break/{id}` | Удалить перерыв | ✅ |

---

## ⚠️ ОБРАБОТКА ОШИБОК

### 400 Bad Request
```json
{
  "error": "Validation error or invalid input"
}
```

### 401 Unauthorized
```json
{
  "error": "Invalid username or password"
}
```

### 404 Not Found
```json
{
  "error": "Resource not found with id: 999"
}
```

### 500 Internal Server Error
```json
{
  "error": "Internal server error"
}
```

---

## 🎯 ПОСЛЕДОВАТЕЛЬНОСТЬ СОЗДАНИЯ ПОЛНОГО РАСПИСАНИЯ

1. **Создать предметы:**
   ```bash
   POST /api/subjects {"name": "Математика"}
   POST /api/subjects {"name": "Физика"}
   ```

2. **Создать преподавателей:**
   ```bash
   POST /api/professors {"name": "Иван Петров"}
   POST /api/professors {"name": "Мария Сидорова"}
   ```

3. **Создать аудитории:**
   ```bash
   POST /api/classrooms {"number": "101"}
   POST /api/classrooms {"number": "205"}
   ```

4. **Создать расписание:**
   ```bash
   POST /api/schedules {"name": "Расписание 1 группы", "facultyId": 1}
   ```

5. **Создать занятия:**
   ```bash
   POST /api/schedule {
     "startTime": "09:00",
     "endTime": "10:30",
     "day": 1,
     "subjectId": 1,
     "professorId": 1,
     "classroomId": 1,
     "scheduleId": 1
   }
   ```

6. **Создать перерывы:**
   ```bash
   POST /api/break {
     "startTime": "10:30",
     "endTime": "10:45",
     "day": 1,
     "scheduleId": 1
   }
   ```

---

**Создано:** 2026-02-09  
**Версия:** 1.0  
**Автор:** GitHub Copilot

