# Защищённый REST API — лабораторная работа №1

Учебный backend-сервис для хранения текстовых записей. Приложение использует
PostgreSQL в Docker, выдаёт JWT после успешного входа и разрешает читать,
добавлять и удалять данные только аутентифицированному пользователю.

## Стек

- Java 17 и Spring Boot;
- Spring Web, Validation и Data JPA/Hibernate;
- PostgreSQL в Docker Compose;
- BCrypt для хранения пароля;
- JWT (JJWT) для аутентификации;
- SpotBugs и OWASP Dependency-Check в GitHub Actions.

## API

| Метод | Путь | Доступ | Назначение |
|---|---|---|---|
| `POST` | `/auth/login` | публичный | вход и получение JWT |
| `GET` | `/api/data` | JWT | получение всех записей |
| `POST` | `/api/data` | JWT | добавление записи |
| `DELETE` | `/api/data/{id}` | JWT | удаление записи по идентификатору |

Пользователь создаётся автоматически при первом запуске. Логин и пароль берутся
из `APP_USER` и `APP_PASSWORD`; в PostgreSQL сохраняется только BCrypt-хэш.

## Запуск

Требуются Java 17 и Docker с поддержкой `docker compose`.

1. Создайте локальный файл с настройками:

   ```bash
   cp .env.example .env
   ```

   Перед запуском измените `POSTGRES_PASSWORD` и `APP_PASSWORD` в `.env`.

2. Загрузите переменные окружения и создайте JWT-секрет:

   ```bash
   set -a
   source .env
   set +a
   export JWT_SECRET="$(openssl rand -base64 32)"
   ```

3. Запустите PostgreSQL:

   ```bash
   docker compose up -d
   ```

4. Запустите API:

   ```bash
   ./mvnw spring-boot:run
   ```

Приложение будет доступно по адресу `http://localhost:8080`. Hibernate создаст
таблицы, а инициализатор добавит пользователя и две демонстрационные записи.

## Примеры curl

### Получение JWT

Используйте значения `APP_USER` и `APP_PASSWORD` из `.env`:

```bash
curl -i -X POST http://localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"login":"student","password":"change-this-password"}'
```

Ответ:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Далее вместо `<JWT>` нужно подставлять полученный токен.

### Получение данных

```bash
curl -i http://localhost:8080/api/data \
  -H 'Authorization: Bearer <JWT>'
```

Запрос без токена или с повреждённым токеном вернёт `401 Unauthorized`.

### Добавление данных

```bash
curl -i -X POST http://localhost:8080/api/data \
  -H 'Authorization: Bearer <JWT>' \
  -H 'Content-Type: application/json' \
  -d '{"content":"Новая запись"}'
```

Пример ответа:

```json
{
  "id": 3,
  "content": "Новая запись"
}
```

### Удаление данных

```bash
curl -i -X DELETE http://localhost:8080/api/data/3 \
  -H 'Authorization: Bearer <JWT>'
```

Успешное удаление возвращает `204 No Content`, отсутствующая запись — `404`.

## Реализованные меры защиты

### Защита от SQL-инъекций

Доступ к PostgreSQL выполняется через Spring Data JPA и Hibernate. Поиск,
сохранение и удаление используют методы `JpaRepository`; SQL-строки не
собираются конкатенацией пользовательского ввода. Формат логина дополнительно
ограничен Jakarta Validation.

### Защита от XSS

Перед отправкой клиенту содержимое каждой записи экранируется с помощью
`HtmlUtils.htmlEscape`. Приложение преобразует JPA-сущность в отдельный DTO,
поэтому экранирование ответа не изменяет исходное значение в базе данных.

### Защита аутентификации

- исходный пароль существует только в переменной окружения, а в PostgreSQL
  сохраняется BCrypt-хэш;
- после успешного входа создаётся подписанный JWT с ограниченным сроком жизни;
- JWT-секрет передаётся через `JWT_SECRET` и не хранится в Git;
- `JwtAuthenticationFilter` защищает все маршруты `/api/**`;
- отсутствующий, неверный или просроченный токен возвращает `401`;
- ответ при неверном логине и пароле одинаковый, что затрудняет перебор
  существующих пользователей.

### Валидация

Логин может содержать только латинские буквы, цифры и символы `._-`. Длина
полей ограничена. Пустое содержимое записи и некорректные входные данные
возвращают `400 Bad Request`.

## CI/CD и security-сканирование

Workflow `.github/workflows/ci.yml` автоматически запускается при каждом push и
pull request. В нём есть две независимые задачи:

1. SAST через SpotBugs;
2. SCA через OWASP Dependency-Check с остановкой pipeline при уязвимости с
   CVSS 7.0 или выше.

Отчёты загружаются в **Artifacts** соответствующего запуска. Для ускорения SCA
рекомендуется добавить NVD API key в GitHub Secret `NVD_API_KEY`.

## Скриншоты отчётов

После первого успешного запуска GitHub Actions необходимо добавить:

- `docs/img/spotbugs.png` — скриншот отчёта SAST;
- `docs/img/dependency-check.png` — скриншот отчёта SCA;
- ссылку на последний успешный запуск workflow.
