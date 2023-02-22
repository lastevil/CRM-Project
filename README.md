<h1>Проект</h1>
<h3>Унифицированная CRM (UniCrm)</h3>
Состав команды:
<p>
<a src="https://github.com/lastevil"> <img width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" alt="Lastevil"> Lastevil </a>
<a src="https://github.com/BaFkit"> <img width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" alt="BaFkit"> BaFkit </a>
<a src="https://github.com/basilomp"> <img width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" alt="basilomp"> basilomp </a>
<a src="https://github.com/LidiaEvmenenko"><img width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" alt="LidiaEvmenenko"> LidiaEvmenenko</a>
</p>
<h1>Цель</h1>
<p>Разработать многомодульную унифицированную CRM систему.
</p>
Сервис должен работать в браузере.
<br>
<p>Базовый сервис позволяет:
</p>
<ol>
	<li>Модуль "Корпоративный чат":</li>
  <ul>
	  <li>общение 1 на 1;</li>
	  <li>групповое общение;</li>
	  <li>передача файлов;</li>
	  <li>хранение истории сообщений.</li>
  </ul>
	<li>Модуль "Менеджер заявок":</li>
  <ul>
	  <li>создание заявок;</li>
	  <li>просмотр заявок пользователя;</li>
	  <li>уведомления пользователя о поступлении заявки(e-mail и(или) "корпоративный чат") ;</li>
	  <li>для начальников отделов возможность просматривать заявки подчиненных;</li>
	  <li>взаимодействие с заявками(изменение статуса, подтверждение и т.д.)</li>
  </ul>
	<li>Модуль "Аналитика":</li>
  <ul>
	  <li>анализ работы отделов </li>
	  <li>анализ работы сотрудников</li>
	  <li>формирование отчетов</li>
  </ul>
</ol>
<h1>Архитектура</h1>

![Architecture img](/images/Architecture.jpg)

<p>Решение представляет собой набор из сервисов
</p>
<ul>
	<li>Eurica, discovery service - межсервисное взаимодействие</li>
	<li>Сервис авторизации</li>
	<li>Сервис чата</li>
	<li>Сервис заявок</li>
	<li>Сервис аналитики</li>
	<li>Базы данных</li>
</ul>
<h1>Технологии </h1>
<p>В проекте используются следующие технологии:
</p>
<p border="2">
<img width="60" src="https://www.svgrepo.com/show/303388/java-4-logo.svg"  alt="Java 11+">
<img width="60" src="https://www.svgrepo.com/show/452045/js.svg" alt="JavaScrypt">
<img width="60" src="https://www.svgrepo.com/show/478176/html.svg" alt="Html">
<img width="60" src="https://www.svgrepo.com/show/376350/spring.svg" alt="Spring">
<img width="60" src="https://www.svgrepo.com/show/354200/postgresql.svg" alt="Postgresql">
<img width="60" src="https://www.svgrepo.com/show/329947/apachekafka.svg" alt="Apache Kafka">
<img width="60" src="https://fontawesomeicons.com/lib/svg/liquibase.svg" alt="Liquibase">
<img width="60" src="https://www.svgrepo.com/show/354420/swagger.svg" alt="Swagger">
<img width="60" src="https://www.svgrepo.com/show/452192/docker.svg" alt="Docker">
<img width="60" src="https://www.svgrepo.com/show/452210/git.svg" alt="Git">
</p>
<h1>Скриншоты:</h1>
Страница входа:

![Login img](/images/login.png)

Страница вывода задач:

![tickets](/images/tickets.jpg)