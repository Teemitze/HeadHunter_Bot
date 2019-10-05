# HeadHunter_Bot
> Данное приложение позволяет приглашать кандидатов на работу. Приложение сделано для сайта hh.ru

*Поддерживаются все ОС с Java 8.*

### Запуск
1) Пожалуйста [**Скачайте**](https://www.java.com/en/download/) и установите JRE
2) Создайте `MySQL` базу с названием `headHunter_bot` с помощью команды `create database "headHunter_bot";`
3) [**Скачайте**](https://github.com/Teemitze/HeadHunter_Bot/releases/latest) SQL скрипт и создайте таблицы.
4) Запустите приложение в IDE или скомпилируйте jar файл с помощью команды `mvn clean compile assembly:single`

##### Настройка конфигурационного файла
* startPage - страница с которой надо начать
* endPage - страница на которой нужно закончить (включительно)
* profession - профессия которую будем искать
* vacancy - название вакансии
* login - ваш логин
* password - ваш пароль
* geckoDriver - путь то geckoDriver
* maxLimitVacancySendOffer - на одну вакансию можно пригласить максимум 2000 человек (ограничения HH). 
Данный пункт позволяет остановить программу, если лимит превышен. 
(Жертвуем скоростью при включении)
* maxLimitResumeView = в день можно просматривать 500 резюме (ограничения HH).
Данный пункт позволяет остановить программу, если лимит превышен. 
(Жертвуем скоростью при включении)

##### Пример работы