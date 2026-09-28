<a id="readme-top"></a>
<h1>Проект по автоматизации тестирования: API и UI</h1>

Дипломный проект с автотестами двух видов:

- **API-тесты** сервиса [Restful Booker](https://restful-booker.herokuapp.com/apidoc/) (бронирования);
- **UI-тесты** платформы [Restful Booker Platform](https://automationintesting.online/) (комнаты),
  где пре-условия и очистка данных выполняются через API, а проверка — через браузер.

## ☑️ Содержание:

- [Технологии и инструменты](#tools)
- [Список проверок, реализованных в тестах](#cases)
- [Запуск тестов из терминала](#run)
- [Сборка в GitHub Actions](#ci)
- [Allure-отчёт](#allure)
- [Интеграция с Allure TestOps](#testops)
- [Интеграция с Jira](#jira)
- [Уведомление в Telegram](#telegram)
- [Демонстрация UI-теста](#ui-demo)

<a id="tools"></a>
## :ballot_box_with_check: Технологии и инструменты:

| Java | IntelliJ IDEA | GitHub | JUnit 5 | Gradle | REST Assured | Selenide | Selenoid | Allure |
|:----:|:-------------:|:------:|:-------:|:------:|:------------:|:--------:|:--------:|:------:|
| <a href="https://www.java.com/"><img src="images/logo/Java.svg" width="50" height="50" alt="Java"/></a> | <a href="https://www.jetbrains.com/idea/"><img src="images/logo/Idea.svg" width="50" height="50" alt="IDEA"/></a> | <a href="https://github.com/"><img src="images/logo/GitHub.png" width="50" height="50" alt="GitHub"/></a> | <a href="https://junit.org/junit5/"><img src="images/logo/Junit5.svg" width="50" height="50" alt="JUnit 5"/></a> | <a href="https://gradle.org/"><img src="images/logo/Gradle.png" width="50" height="50" alt="Gradle"/></a> | <a href="https://rest-assured.io/"><img src="https://img.shields.io/badge/REST-Assured-2ea44f?style=for-the-badge" height="34" alt="REST Assured"/></a> | <a href="https://selenide.org/"><img src="images/logo/Selenide.svg" width="50" height="50" alt="Selenide"/></a> | <a href="https://aerokube.com/selenoid/"><img src="images/logo/Selenoid.svg" width="50" height="50" alt="Selenoid"/></a> | <a href="https://github.com/allure-framework"><img src="images/logo/Allure.svg" width="50" height="50" alt="Allure"/></a> |

Тесты написаны на языке <code>Java</code>, сборщик — <code>Gradle</code>, в качестве фреймворка
модульного тестирования используется <code>JUnit 5</code>. API-тесты построены на
[REST Assured](https://rest-assured.io/) с валидацией ответов по JSON-схемам, UI-тесты — на
[Selenide](https://selenide.org/). Для генерации тестовых данных применяется <code>Datafaker</code>,
для проверок — <code>AssertJ</code>, отчётность — <code>Allure</code>. UI-тесты можно запускать
удалённо в [Selenoid](https://aerokube.com/selenoid/) (тогда в отчёт попадает видео прогона).

UI-часть выполнена по образцу проекта
[qa_guru_course_ui](https://github.com/Dmitry5401/qa_guru_course_ui): Page Objects (`pages`),
`helpers/Attach` для вложений Allure, конфигурация браузера через системные свойства.

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="cases"></a>
## :ballot_box_with_check: Реализованные проверки:

### API — Restful Booker (по 2 теста на метод: позитивный и негативный)

| Метод | Позитивный сценарий | Негативный сценарий |
| --- | --- | --- |
| `POST /booking` | создание бронирования → `200` | некорректное тело → `500` |
| `GET /booking/{id}` | получение по id → `200` | несуществующий id → `404` |
| `PUT /booking/{id}` | полное обновление с токеном → `200` | без авторизации → `403` |
| `PATCH /booking/{id}` | частичное обновление с токеном → `200` | без авторизации → `403` |
| `DELETE /booking/{id}` | удаление с токеном → `201` | без авторизации → `403` |

### UI — Restful Booker Platform

- Созданная через API комната корректно отображается на странице бронирования (тип, цена, описание, удобства);
- Удалённая через API комната больше не отображается в UI.

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

<!-- СКРИНШОТ ДОБАВИТЬ: успешный прогон во вкладке Actions. Сохранить как images/screen/github-actions.png -->
<p align="center">
<img title="GitHub Actions" src="images/screen/github-actions.png" width="850">
</p>

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

### Основная страница отчёта

<!-- СКРИНШОТ ДОБАВИТЬ: главная страница Allure (Overview). Сохранить как images/screen/allure-overview.png -->
<p align="center">
<img title="Allure Overview" src="images/screen/allure-overview.png" width="850">
</p>

### Тест-кейсы

<!-- СКРИНШОТ ДОБАВИТЬ: список тестов в Allure (Suites/Behaviors). Сохранить как images/screen/allure-testcases.png -->
<p align="center">
<img title="Allure Test Cases" src="images/screen/allure-testcases.png" width="850">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="testops"></a>
## <img alt="Allure TestOps" height="25" src="images/logo/Allure_TO.svg" width="25"/> Интеграция с Allure TestOps

Проект может быть подключён к [Allure TestOps](https://qameta.io/): автоматические прогоны
загружаются как launch'и, тест-кейсы синхронизируются с кодом.
_Интеграция настраивается через токен и endpoint TestOps; после подключения добавьте скриншоты._

### Allure TestOps Dashboard

<!-- СКРИНШОТ ДОБАВИТЬ: дашборд Allure TestOps. Сохранить как images/screen/allure-testops-dashboard.png -->
<p align="center">
<img title="Allure TestOps Dashboard" src="images/screen/allure-testops-dashboard.png" width="850">
</p>

### Авто и ручные тест-кейсы

<!-- СКРИНШОТ ДОБАВИТЬ: список тест-кейсов в TestOps. Сохранить как images/screen/allure-testops-testcases.png -->
<p align="center">
<img title="Allure TestOps Test Cases" src="images/screen/allure-testops-testcases.png" width="850">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

<a id="jira"></a>
## <img alt="Jira" height="25" src="images/logo/Jira.svg" width="25"/> Интеграция с Jira

Тест-кейсы и запуски связываются с задачами в [Jira](https://www.atlassian.com/software/jira),
что позволяет видеть статус автоматизации прямо в тикете.
_После настройки интеграции добавьте скриншот связанной задачи._

<!-- СКРИНШОТ ДОБАВИТЬ: задача Jira со связанными тестами/запусками. Сохранить как images/screen/jira.png -->
<p align="center">
<img title="Jira" src="images/screen/jira.png" width="850">
</p>

<p align="right"><a href="#readme-top">back to top</a></p>

____
<a id="telegram"></a>
## <img alt="Telegram" height="25" src="images/logo/Telegram.svg" width="25"/> Уведомление в Telegram
____

По завершении прогона бот присылает в [Telegram](https://web.telegram.org/) сводку с
результатами и ссылкой на Allure-отчёт.
_После подключения бота добавьте скриншот уведомления._

<!-- СКРИНШОТ ДОБАВИТЬ: сообщение бота с результатами прогона. Сохранить как images/screen/telegram.png -->
<p align="center">
<img title="Telegram" src="images/screen/telegram.png" width="550">
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
