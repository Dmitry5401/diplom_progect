<a id="readme-top"></a>
<h1>Проект по автоматизации тестирования: API и UI</h1>

Дипломный проект с автотестами двух видов:

- **API-тесты** сервиса [Restful Booker](https://restful-booker.herokuapp.com/apidoc/) (бронирования);
- **UI-тесты** платформы [Restful Booker Platform](https://automationintesting.online/) (комнаты),
  где пре-условия и очистка данных выполняются через API, а проверка — через браузер.

## ☑️ Содержание:

- [Технологии и инструменты](#tools)
- [Архитектура и Best Practices](#architecture)
- [Структура проекта](#structure)
- [Список проверок, реализованных в тестах](#cases)
- [Запуск тестов из терминала](#run)
- [Прогон API-тестов в Jenkins](#jenkins)
- [Сборка в GitHub Actions](#ci)
- [Allure-отчёт](#allure)
- [Интеграция с Allure TestOps](#testops)
- [Интеграция с Jira](#jira)
- [Уведомление в Telegram](#telegram)
- [Демонстрация UI-теста](#ui-demo)

<a id="tools"></a>
## :ballot_box_with_check: Технологии и инструменты:

| Java | IntelliJ IDEA | GitHub | JUnit 5 | Gradle | REST Assured | Selenide | Selenoid | Allure | Jenkins | Allure TestOps | Jira | Telegram |
|:----:|:-------------:|:------:|:-------:|:------:|:------------:|:--------:|:--------:|:------:|:-------:|:--------------:|:----:|:--------:|
| <a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50" alt="Java"/></a> | <a href="https://www.jetbrains.com/idea/"><img src="images/logo/Idea.svg" width="50" height="50" alt="IDEA"/></a> | <a href="https://github.com/"><img src="images/logo/GitHub.png" width="50" height="50" alt="GitHub"/></a> | <a href="https://junit.org/junit5/"><img src="images/logo/Junit5.svg" width="50" height="50" alt="JUnit 5"/></a> | <a href="https://gradle.org/"><img src="images/logo/Gradle.png" width="50" height="50" alt="Gradle"/></a> | <a href="https://rest-assured.io/"><img src="https://img.shields.io/badge/REST-Assured-2ea44f?style=for-the-badge" height="34" alt="REST Assured"/></a> | <a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50" alt="Selenide"/></a> | <a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50" alt="Selenoid"/></a> | <a href="https://github.com/allure-framework"><img src="images/logo/Allure.svg" width="50" height="50" alt="Allure"/></a> | <a href="https://www.jenkins.io/"><img src="images/logo/Jenkins.svg" width="50" height="50" alt="Jenkins"/></a> | <a href="https://qameta.io/"><img src="images/logo/Allure_TO.svg" width="50" height="50" alt="Allure TestOps"/></a> | <a href="https://www.atlassian.com/software/jira"><img src="images/logo/Jira.svg" width="50" height="50" alt="Jira"/></a> | <a href="https://web.telegram.org/"><img src="images/logo/Telegram.svg" width="50" height="50" alt="Telegram"/></a> |

Тесты написаны на языке <code>Java</code>, сборщик — <code>Gradle</code>, в качестве фреймворка
модульного тестирования используется <code>JUnit 5</code>. API-тесты построены на
[REST Assured](https://rest-assured.io/) с валидацией ответов по JSON-схемам, UI-тесты — на
[Selenide](https://selenide.org/). Для генерации тестовых данных применяется <code>Datafaker</code>,
для проверок — <code>AssertJ</code>, отчётность — <code>Allure</code>. UI-тесты можно запускать
удалённо в [Selenoid](https://aerokube.com/selenoid/) (тогда в отчёт попадает видео прогона).
Прогон API-тестов публикуется из [Jenkins](https://www.jenkins.io/): Allure-отчёт, запуск в
[Allure TestOps](https://qameta.io/), связь тест-кейсов с задачей в
[Jira](https://www.atlassian.com/software/jira) и уведомление в
[Telegram](https://web.telegram.org/).

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="architecture"></a>
## :ballot_box_with_check: Архитектура и Best Practices

Тест описывает сценарий. HTTP-запрос, локаторы и вложения отчёта вынесены в отдельные слои.

### 1. API-клиенты отдельно от тестов

Вызовы Restful Booker собраны в `AuthApiClient` и `BookingApiClient`, доступ к ним даёт `ApiClient`.
Админка платформы для UI-тестов живёт отдельно: `PlatformApiClient`, `PlatformAuthApi`, `PlatformRoomApi`.
Методы клиентов размечены `@Step`, поэтому в Allure виден шаг запроса.

### 2. Спецификации и модели

Код ответа, JSON-схема и обязательные поля описаны в `specs` (`AuthSpec`, `BookingSpec`, `PlatformSpec`).
Тела запросов и ответов — Java records в `models`. Схемы лежат в `src/test/resources/schemas`.

### 3. Page Object для UI

Страница бронирования описана отдельным классом в пакете `pages` (паттерн Page Object):
тест вызывает его методы, чтобы открыть страницу, проверить комнату и отправить форму гостя
(`openReservation`, `openBookingForm`, `submitGuest`, `checkValidationError`,
`checkRoomDisplayed`, `checkRoomNotDisplayed`).
Класс `helpers/Attach` после прогона добавляет в отчёт Allure скриншот, код страницы,
логи браузера и видео. Браузер, размер окна, запуск без окна и адрес Selenoid задаются
при старте параметрами `-Dbrowser`, `-DbrowserSize`, `-Dheadless` и `-DremoteUrl`.

### 4. Данные готовятся через API и удаляются после теста

UI-тест создаёт комнату через API, проверяет её в браузере и удаляет комнату в `@AfterEach`.
API-тесты запоминают id созданных бронирований и удаляют их после теста.

### 5. Тестовые данные

Постоянные значения (логин, неверный пароль, тексты ошибок) лежат в `TestData`.
Имя, фамилию, цену и удобства генерирует Datafaker, чтобы тесты не зависели от заранее созданных записей.

### 6. Отчёт Allure

`CustomAllureListener` подключает шаблоны `request.ftl` и `response.ftl` к каждому API-запросу.
Для UI `helpers/Attach` прикладывает скриншот, page source, логи браузера и видео прогона в Selenoid.

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="structure"></a>
## :ballot_box_with_check: Структура проекта

```text
src/test
├── java
│   ├── allure
│   │   └── CustomAllureListener          # шаблоны request/response в отчёте Allure
│   ├── api
│   │   ├── ApiClient                     # фасад Restful Booker: auth + booking
│   │   ├── AuthApiClient                 # POST /auth
│   │   ├── BookingApiClient              # CRUD /booking
│   │   └── platform
│   │       ├── PlatformApiClient         # фасад админки automationintesting.online
│   │       ├── PlatformAuthApi           # логин администратора
│   │       └── PlatformRoomApi           # создание, поиск и удаление комнат
│   ├── helpers
│   │   └── Attach                        # скриншот, page source, логи браузера, видео
│   ├── models
│   │   ├── auth                          # тело и ответ POST /auth
│   │   ├── booking                       # бронирование, даты, ответ создания, PATCH
│   │   └── platform                      # логин и комнаты платформы
│   ├── pages
│   │   └── ReservationPage               # страница бронирования комнаты
│   ├── specs
│   │   ├── BaseSpec                      # общий request spec и фильтр Allure
│   │   ├── auth/AuthSpec                 # 200 и схема токена, Bad credentials
│   │   ├── booking/BookingSpec           # коды и схемы CRUD бронирования
│   │   └── platform/PlatformSpec         # запросы к /api платформы
│   └── tests
│       ├── api                           # 12 тестов Restful Booker, TestBase, TestData
│       └── ui                            # RoomUiTests, BookingFormTests, BaseTest, testdata/TestData
└── resources
    ├── allure.properties
    ├── schemas                           # JSON-схемы auth и booking
    └── tpl                               # request.ftl, response.ftl
```

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="cases"></a>
## :ballot_box_with_check: Реализованные проверки:

### API — Restful Booker (по 2 теста на метод: позитивный и негативный)

| Метод | Позитивный сценарий | Негативный сценарий |
| --- | --- | --- |
| `POST /auth` | получение токена с валидными данными → `200` | неверный пароль → `200`, reason `Bad credentials` |
| `POST /booking` | создание бронирования → `200` | некорректное тело → `500` |
| `GET /booking/{id}` | получение по id → `200` | несуществующий id → `404` |
| `PUT /booking/{id}` | полное обновление с токеном → `200` | без авторизации → `403` |
| `PATCH /booking/{id}` | частичное обновление с токеном → `200` | без авторизации → `403` |
| `DELETE /booking/{id}` | удаление с токеном → `201` | без авторизации → `403` |

### UI — Restful Booker Platform

- Созданная через API комната корректно отображается на странице бронирования (тип, цена, описание, удобства);
- Удалённая через API комната больше не отображается в UI;
- `BookingFormTests`: форма бронирования показывает ошибку валидации для пустого имени, имени короче 3 символов, пустой фамилии, некорректного email и телефона короче 11 символов (заезд — сегодня плюс 7 дней, выезд — сегодня плюс 8 дней; комната создаётся через API и удаляется в `@AfterEach`).

Схема UI-теста: **pre-conditions через API** (создание комнаты) → **проверка через браузер** →
**post-conditions через API** (удаление комнаты в `@AfterEach`).

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="run"></a>
## :ballot_box_with_check: Запуск тестов из терминала

Запуск всех тестов:

```sh
./gradlew clean test
```

Только API- или только UI-тесты:

```sh
./gradlew test --tests "tests.api.*"
./gradlew test --tests "tests.ui.*"
```

Полезные параметры UI-тестов (по умолчанию — локальный Chrome):

```sh
./gradlew test -Dheadless=true                 # запуск без окна браузера
./gradlew test -DbrowserSize=1920x1080         # размер окна
./gradlew test -DremoteUrl=https://user:pass@selenoid.autotests.cloud/wd/hub   # запуск в Selenoid
```

Переопределение адресов и учётных данных:

```sh
./gradlew test -DbaseUrl=https://restful-booker.herokuapp.com   # API restful-booker
./gradlew test -DplatformUrl=https://automationintesting.online # платформа для UI
./gradlew test -DadminUsername=admin -DadminPassword=password   # креды админки платформы
```

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="jenkins"></a>
## <img alt="Jenkins" height="25" src="images/logo/Jenkins.svg" width="25"/> Прогон API-тестов в Jenkins

API-тесты Restful Booker запускаются джобой
[kdp029_diplom](https://jenkins.qa.guru/job/kdp029_diplom/) в Jenkins.
После сборки публикуется Allure-отчёт: 12 сценариев (авторизация и CRUD бронирования),
исполнитель сборки — Jenkins.

<p align="center">
<img title="Allure-отчёт прогона API-тестов в Jenkins" src="images/screen/Jankins1.png" width="850" alt="Прогон API-тестов в Jenkins: 12 тестов, 100%">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="ci"></a>
## <img alt="GitHub Actions" height="25" src="images/logo/GitHub.png" width="25"/> Сборка в GitHub Actions

Тесты запускаются в [GitHub Actions](../../actions) на каждый push и pull request в `main`,
а также вручную (workflow `Tests`, кнопка **Run workflow**). Конфигурация — в
[`.github/workflows/tests.yml`](.github/workflows/tests.yml). Результаты (`allure-results`
и HTML-отчёт) сохраняются как артефакты сборки.

### Параметры запуска (Run workflow)

- `browser` — браузер (по умолчанию `chrome`);
- `browserVersion` — версия браузера (по умолчанию последняя);
- `browserSize` — размер окна браузера (по умолчанию `1920x1080`);
- `headless` — запуск без окна браузера (по умолчанию `true`).

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="allure"></a>
## <img alt="Allure" height="25" src="images/logo/Allure.svg" width="25"/> Allure-отчёт

Формирование и просмотр отчёта после прогона:

```sh
./gradlew test
./gradlew allureServe    # открыть отчёт в браузере
# либо
./gradlew allureReport   # статический отчёт в build/reports/allure-report
```

Опубликованный отчёт прогона API-тестов — в разделе [Jenkins](#jenkins).

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="testops"></a>
## <img alt="Allure TestOps" height="25" src="images/logo/Allure_TO.svg" width="25"/> Интеграция с Allure TestOps

Прогоны API-тестов загружаются в проект Allure TestOps `kdp029_diplom_testops`.
На дашборде — 12 автоматизированных тестов (100%). Тест-кейсы сгруппированы по методам
Restful Booker: авторизация, создание, получение, обновление и удаление бронирования.

### Дашборд

<p align="center">
<img title="Дашборд Allure TestOps" src="images/screen/ApiDashboard.png" width="850" alt="Дашборд TestOps: 12 автотестов, 100%">
</p>

### Тест-кейсы

<p align="center">
<img title="Тест-кейсы Allure TestOps" src="images/screen/AddManualTest.png" width="850" alt="Тест-кейсы API в TestOps">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="jira"></a>
## <img alt="Jira" height="25" src="images/logo/Jira.svg" width="25"/> Интеграция с Jira

Задача **MUL-55** «Разработка автотестов API CRUD для бронирования в Restful Booker (Booking)»
связана с тест-кейсами TestOps: в тикете, в блоке TestOps, видны сценарии методов
`POST /auth`, `GET`, `POST`, `PUT`, `PATCH` и `DELETE /booking`.

<p align="center">
<img title="Задача Jira MUL-55 со связанными тест-кейсами" src="images/screen/Jira.png" width="850" alt="Jira: задача MUL-55 и артефакты тестов TestOps">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

____
<a id="telegram"></a>
## <img alt="Telegram" height="25" src="images/logo/Telegram.svg" width="25"/> Уведомление в Telegram
____

По завершении прогона бот присылает в [Telegram](https://web.telegram.org/) сводку:
окружение DEV, 12 из 12 успешных сценариев и ссылку на Allure-отчёт Jenkins.

<p align="center">
<img title="Уведомление Telegram о прогоне API-тестов" src="images/screen/Telrgramm1.png" width="550" alt="Telegram: 12 успешных API-тестов">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="ui-demo"></a>
## <img alt="Selenide" height="25" src="images/logo/Selenide.svg" width="25"/> Демонстрация UI-теста

Комната, созданная через API, корректно отображается на странице бронирования:

<p align="center">
<img title="Созданная через API комната отображается в UI" src="images/screen/ui_room_displayed.png" width="850">
</p>

После удаления комнаты через API (`DELETE /api/room/{id}`) та же страница её больше не показывает:

<p align="center">
<img title="Удалённая комната больше не отображается" src="images/screen/ui_room_deleted.png" width="850">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>
