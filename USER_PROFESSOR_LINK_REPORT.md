# ✅ Добавлена связь между User и Professor

## Что было сделано:

### 1. **Entity Layer** (UserEntity.java)
- ✅ Добавлено поле: `private Professor professor`
- ✅ ManyToOne связь с Professor (fetch = LAZY)
- ✅ Nullable (профессор может быть null)

### 2. **DTO Layer** (UserDTO.java)
- ✅ Добавлено поле: `private Long professorId`
- ✅ Содержит ID профессора (для передачи в API)

### 3. **Mapper** (UserMapper.java)
- ✅ Маппинг professorId при преобразовании Entity → DTO
- ✅ В методе toEntity() профессор маппится в сервисе (нужно получить из БД)

### 4. **Migration** (Liquibase)
- ✅ Создан файл: `021-add-professor-to-users.yml`
- ✅ Добавляет колонку `professor_id` в таблицу `users`
- ✅ Foreign Key на таблицу `professors`
- ✅ ON DELETE SET NULL (если удалить профессора, поле очистится)

### 5. **Master Changelog**
- ✅ Миграция добавлена между `008-create-users.yml` и `009-test-admin.yml`

## Использование в коде:

```java
// Получить профессора пользователя
Long professorId = userDTO.getProfessorId();

// Установить профессора при создании пользователя
UserDTO userDTO = new UserDTO();
userDTO.setProfessorId(1L);

// В UserEntity:
Professor professor = user.getProfessor(); // может быть null
```

## API пример:

```json
{
  "id": 1,
  "username": "teacher",
  "email": "teacher@example.com",
  "role": "ADMIN",
  "isBanned": false,
  "professorId": 5
}
```

✅ **Готово к развёртыванию!**

