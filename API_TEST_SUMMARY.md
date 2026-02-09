# API Тестирования - Сводка

## Base URL
```
http://localhost:8080
```

---

## 1. ПРЕДМЕТЫ (Subjects)

### 1.1 Получить все предметы
**Запрос:**
```
GET /api/subjects
```

**Ответ:** (200 OK)
```json
[
  {
    "id": 1,
    "name": "Математика"
  },
  {
    "id": 2,
    "name": "Физика"
  }
]
```

### 1.2 Создать новый предмет
**Запрос:**
```
POST /api/subjects
Content-Type: application/json
```

**Тело запроса:** (id не требуется)
```json
{
  "name": "Математика"
}
```

**Ответ:** (201 Created)
```json
{
  "id": 1,
  "name": "Математика"
}
```

### 1.3 Обновить предмет
**Запрос:**
```
PUT /api/subjects/{id}
Content-Type: application/json
```

Пример:
```
PUT /api/subjects/1
```

**Тело запроса:** (id не требуется, передается в URL)
```json
{
  "name": "Алгебра"
}
```

**Ответ:** (200 OK)
```json
{
  "id": 1,
  "name": "Алгебра"
}
```

### 1.4 Удалить предмет
**Запрос:**
```
DELETE /api/subjects/{id}
```
Пример:
```
DELETE /api/subjects/1
```

**Ответ:** (204 No Content)

---

## 2. ПРЕПОДАВАТЕЛИ (Professors)

### 2.1 Получить всех преподавателей
**Запрос:**
```
GET /api/professors
```

**Ответ:** (200 OK)
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

### 2.2 Создать нового преподавателя
**Запрос:**
```
POST /api/professors
Content-Type: application/json
```

**Тело запроса:** (id не требуется)
```json
{
  "name": "Иван Петров"
}
```

**Ответ:** (201 Created)
```json
{
  "id": 1,
  "name": "Иван Петров"
}
```

### 2.3 Обновить преподавателя
**Запрос:**
```
PUT /api/professors/{id}
Content-Type: application/json
```

Пример:
```
PUT /api/professors/1
```

**Тело запроса:** (id не требуется, передается в URL)
```json
{
  "name": "Иван Сергеевич Петров"
}
```

**Ответ:** (200 OK)
```json
{
  "id": 1,
  "name": "Иван Сергеевич Петров"
}
```

### 2.4 Удалить преподавателя
**Запрос:**
```
DELETE /api/professors/{id}
```

Пример:
```
DELETE /api/professors/1
```

**Ответ:** (204 No Content)

---

## 3. КЛАССНЫЕ КОМНАТЫ (Classrooms)

### 3.1 Получить все классные комнаты
**Запрос:**
```
GET /api/classrooms
```

**Ответ:** (200 OK)
```json
[
  {
    "id": 1,
    "number": "101"
  },
  {
    "id": 2,
    "number": "202"
  }
]
```

### 3.2 Создать новую классную комнату
**Запрос:**
```
POST /api/classrooms
Content-Type: application/json
```

**Тело запроса:** (id не требуется)
```json
{
  "number": "101"
}
```

**Ответ:** (201 Created)
```json
{
  "id": 1,
  "number": "101"
}
```

### 3.3 Обновить классную комнату
**Запрос:**
```
PUT /api/classrooms/{id}
Content-Type: application/json
```

Пример:
```
PUT /api/classrooms/1
```

**Тело запроса:** (id не требуется, передается в URL)
```json
{
  "number": "102"
}
```

**Ответ:** (200 OK)
```json
{
  "id": 1,
  "number": "102"
}
```

### 3.4 Удалить классную комнату
**Запрос:**
```
DELETE /api/classrooms/{id}
```

Пример:
```
DELETE /api/classrooms/1
```

**Ответ:** (204 No Content)

---

## 4. УРОКИ/РАСПИСАНИЕ (Lessons)

### 4.1 Получить расписание (все уроки)
**Запрос:**
```
GET /api/schedule
```

**Ответ:** (200 OK)
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
  }
]
```

### 4.2 Создать новый урок
**Запрос:**
```
POST /api/schedule
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "startTime": "09:00",
  "endTime": "10:30",
  "day": 1,
  "subjectId": 1,
  "professorId": 1,
  "classroomId": 1
}
```

**Ответ:** (200 OK)
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
  },
  "breakInfo": null
}
```

### 4.3 Обновить урок
**Запрос:**
```
PUT /api/schedule/{id}
Content-Type: application/json
```

Пример:
```
PUT /api/schedule/1
```

**Тело запроса:**
```json
{
  "startTime": "10:00",
  "endTime": "11:30",
  "day": 1
}
```

**Ответ:** (200 OK)
```json
{
  "id": 1,
  "startTime": "10:00",
  "endTime": "11:30",
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
  "breakInfo": null
}
```

### 4.4 Удалить урок
**Запрос:**
```
DELETE /api/schedule/{id}
```

Пример:
```
DELETE /api/schedule/1
```

**Ответ:** (204 No Content)

---

## 5. ПЕРЕРЫВЫ (Breaks)

### 5.1 Получить все перерывы
**Запрос:**
```
GET api/break
```

**Ответ:** (200 OK)
```json
[
  {
    "id": 1,
    "startTime": "10:30",
    "endTime": "10:40",
    "day": 1
  }
]
```

### 5.2 Создать перерыв
**Запрос:**
```
POST /api/break
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "startTime": "10:30",
  "endTime": "10:40",
  "day": 1
}
```

**Ответ:** (201 Created)
```json
{
  "id": 1,
  "startTime": "10:30",
  "endTime": "10:40",
  "day": 1
}
```

### 5.3 Обновить перерыв
**Запрос:**
```
PUT /api/break/{id}
Content-Type: application/json
```

Пример:
```
PUT /api/break/1
```

**Тело запроса:**
```json
{
  "startTime": "10:35",
  "endTime": "10:45",
  "day": 1
}
```

**Ответ:** (200 OK)
```json
{
  "id": 1,
  "startTime": "10:35",
  "endTime": "10:45",
  "day": 1
}
```

### 5.4 Удалить перерыв
**Запрос:**
```
DELETE /api/break/{id}
```

Пример:
```
DELETE /api/break/1
```

**Ответ:** (204 No Content)

---

## Коды ответов

| Код | Описание |
|-----|----------|
| 200 | OK - Успешный запрос |
| 201 | Created - Ресурс создан |
| 204 | No Content - Успешно удалено |
| 400 | Bad Request - Ошибка в запросе (например, неверные данные) |
| 404 | Not Found - Ресурс не найден |
| 500 | Internal Server Error - Ошибка сервера |

---

## Параметры дней недели

День недели передается как число от 0 до 7:
- 0 = Понедельник
- 1 = Вторник
- 2 = Среда
- 3 = Четверг
- 4 = Пятница
- 5 = Суббота
- 6 = Воскресенье
- 7 = Понедельник (альтернативное значение)

---

## Пример полного сценария (Step-by-step)

### Шаг 1: Создайте предмет
```bash
curl -X POST http://localhost:8080/api/subjects \
  -H "Content-Type: application/json" \
  -d '{"name":"Математика"}'
```

### Шаг 2: Создайте преподавателя
```bash
curl -X POST http://localhost:8080/api/professors \
  -H "Content-Type: application/json" \
  -d '{"name":"Иван Петров"}'
```

### Шаг 3: Создайте классную комнату
```bash
curl -X POST http://localhost:8080/api/classrooms \
  -H "Content-Type: application/json" \
  -d '{"number":"101"}'
```

### Шаг 4: Создайте урок (используйте ID из предыдущих шагов)
```bash
curl -X POST http://localhost:8080/schedule \
  -H "Content-Type: application/json" \
  -d '{
    "startTime":"09:00",
    "endTime":"10:30",
    "day":1,
    "subjectId":1,
    "professorId":1,
    "classroomId":1
  }'
```

### Шаг 5: Получите расписание
```bash
curl http://localhost:8080/schedule
```

### Шаг 6: Создайте перерыв
```bash
curl -X POST http://localhost:8080/break \
  -H "Content-Type: application/json" \
  -d '{
    "startTime":"10:30",
    "endTime":"10:40",
    "day":1
  }'
```

