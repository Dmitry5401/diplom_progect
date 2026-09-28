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

Токен для `PUT` / `PATCH` / `DELETE` берётся через `POST /auth` (`admin` / `password123`).

## UI-тесты (automationintesting.online)

UI-тесты для [Restful Booker Platform](https://automationintesting.online/) построены по
схеме «пре-условия и очистка через API, проверка через браузер»:

1. **Pre-conditions (API)** — авторизация в админке (`POST /api/auth/login`) и создание
   комнаты (`POST /api/room`) готовыми API-запросами.
2. **Проверка (UI)** — Selenide открывает страницу комнаты и проверяет, что созданная
   комната корректно отображается (тип, цена, описание, удобства).
3. **Post-conditions (API)** — созданные комнаты удаляются в `@AfterEach`
   через `DELETE /api/room/{id}`, чтобы не засорять базу.

| Сценарий | Проверка |
| --- | --- |
| Созданная через API комната | корректно отображается на странице бронирования |
| Удалённая через API комната | больше не отображается в UI |

Тесты идут в headless-режиме. Запуск с видимым браузером: `./gradlew test -Dheadless=false`.
Адрес платформы переопределяется `-DplatformUrl=...`, учётные данные админки —
`-DadminUsername=... -DadminPassword=...`.

## Стек

- Java 21
- Gradle 8.13 (в репозитории есть wrapper — `./gradlew`)
- JUnit 5, REST Assured, JSON Schema Validator
- Selenide (UI), Allure, Datafaker, AssertJ

## Запуск тестов

```bash
./gradlew test
```

По умолчанию тесты идут против `https://restful-booker.herokuapp.com`.
Базовый адрес можно переопределить:

```bash
./gradlew test -DbaseUrl=https://restful-booker.herokuapp.com
```

## Отчёт Allure

```bash
./gradlew test
./gradlew allureReport   # отчёт в build/reports/allure-report
```

## Структура проекта

```
src/test/java
├── api                 # клиенты restful-booker (AuthApiClient, BookingApiClient)
│   └── platform        # клиенты automationintesting (PlatformAuthApi, PlatformRoomApi)
├── allure              # кастомный listener для вложений Allure
├── models              # request/response модели (records), в т.ч. models/platform
├── specs               # спецификации REST Assured, в т.ч. specs/platform
├── ui/pages            # Page Objects (Selenide)
└── tests
    ├── api             # API-тесты restful-booker
    └── ui              # UI-тесты automationintesting.online
src/test/resources
├── schemas             # JSON-схемы ответов
└── tpl                 # шаблоны вложений Allure
```
