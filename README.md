# diplom_progect

API-автотесты для [Restful Booker](https://restful-booker.herokuapp.com/apidoc/).

Проект сделан по аналогии с [qa_guru_book-club-api](https://github.com/Dmitry5401/qa_guru_book-club-api):
Java + Gradle + JUnit 5 + REST Assured + Allure. Структура разделена на слои
`api` / `models` / `specs` / `tests`, а тела/ответы описаны JSON-схемами.

## Что покрыто

По 2 теста на каждый HTTP-метод: один позитивный сценарий (код `200`) и один
негативный (код `4xx` / `5xx`).

| Метод | Позитивный сценарий | Негативный сценарий |
| --- | --- | --- |
| `POST /booking` | Создание бронирования → `200` | Некорректное тело запроса → `500` |
| `GET /booking/{id}` | Получение бронирования по id → `200` | Несуществующий id → `404` |
| `PUT /booking/{id}` | Полное обновление с токеном → `200` | Обновление без авторизации → `403` |
| `PATCH /booking/{id}` | Частичное обновление с токеном → `200` | Обновление без авторизации → `403` |
| `DELETE /booking/{id}` | Удаление с токеном → `201` | Удаление без авторизации → `403` |
| `POST /auth` | Получение токена с валидными данными → `200` | Неверный пароль → `Bad credentials` |

Токен для `PUT` / `PATCH` / `DELETE` берётся через `POST /auth`. Учётные данные по
умолчанию — `admin` / `password123`; их можно переопределить системными свойствами
`-DadminUsername=... -DadminPassword=...`.

Созданные в тестах бронирования удаляются в `@AfterEach`, чтобы не оставлять мусор
на сервере.

## Стек

- Java 21
- Gradle 8.13 (в репозитории есть wrapper — `./gradlew`)
- JUnit 5, REST Assured, JSON Schema Validator
- Allure, Datafaker, AssertJ

## Запуск тестов

```bash
./gradlew test
```

По умолчанию тесты идут против `https://restful-booker.herokuapp.com`.
Базовый адрес можно переопределить:

```bash
./gradlew test -DbaseUrl=https://restful-booker.herokuapp.com
```

## CI

Тесты автоматически запускаются в GitHub Actions на каждый push и pull request
в `main` (workflow `.github/workflows/tests.yml`). Отчёт и allure-results
сохраняются как артефакты сборки.

## Отчёт Allure

```bash
./gradlew test
./gradlew allureReport   # отчёт в build/reports/allure-report
```

## Структура проекта

```
src/test/java
├── api        # клиенты API (AuthApiClient, BookingApiClient)
├── allure     # кастомный listener для вложений Allure
├── models     # request/response модели (records)
├── specs      # request/response спецификации REST Assured
└── tests      # тест-классы и тестовые данные
src/test/resources
├── schemas    # JSON-схемы ответов
└── tpl        # шаблоны вложений Allure
```
