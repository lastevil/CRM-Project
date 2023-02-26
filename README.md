<h1>Проект</h1>
<h3>Унифицированная CRM (UniCrm)</h3>
Состав команды:
<div>
<a href="https://github.com/lastevil"> <img width="20" background="white" src="https://icons-for-free.com/download-icon-coding+development+github+programming+social+icon-1320086085448562008_0.svg" alt="Lastevil"> Lastevil </a>
<a href="https://github.com/BaFkit"> <img width="20" src="https://icons-for-free.com/download-icon-coding+development+github+programming+social+icon-1320086085448562008_0.svg" alt="BaFkit"> BaFkit </a>
<a href="https://github.com/basilomp"> <img width="20" src="https://icons-for-free.com/download-icon-coding+development+github+programming+social+icon-1320086085448562008_0.svg" alt="basilomp"> basilomp </a>
<a href="https://github.com/LidiaEvmenenko"><img width="20" src="https://icons-for-free.com/download-icon-coding+development+github+programming+social+icon-1320086085448562008_0.svg" alt="LidiaEvmenenko"> LidiaEvmenenko</a>
</div>
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
	  <li>уведомления пользователя о поступлении заявки(e-mail и/или "корпоративный чат") ;</li>
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
	<li>Eurica, discovery service — межсервисное взаимодействие</li>
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
<img width="60" src="https://www.svgrepo.com/show/303388/java-4-logo.svg"  title="Java 11+" onclick="">
<img width="60" src="https://www.svgrepo.com/show/452045/js.svg" title="JavaScrypt" onclick="">
<img width="60" src="https://www.svgrepo.com/show/7866/html.svg" title="Html" onclick="">
<img width="60" src="https://www.svgrepo.com/show/376350/spring.svg" title="Spring" onclick="">
<img width="60" src="https://www.svgrepo.com/show/354200/postgresql.svg" title="Postgresql" onclick="">
<img width="60" src="https://www.businessprocessincubator.com/wp-content/uploads/thumbnails/thumbnail-152417.png" title="Apache Kafka" onclick="">
<img width="60" src="/images/Liquibase1.svg" title="Liquibase" onclick="">
<img width="60" src="https://www.svgrepo.com/show/354420/swagger.svg" title="Swagger" onclick="">
<img width="60" src="https://www.svgrepo.com/show/452192/docker.svg" title="Docker" onclick="">
<img width="60" src="https://www.svgrepo.com/show/452210/git.svg" title="Git" onclick="">
</p>
<h1>Скриншоты:</h1>
Страница входа:

![Login img](/images/login.png)

Страница вывода задач:

![tickets](/images/tickets.jpg)

Окно создания задач:

![tickets create](/images/tickets_create.jpg)

Окно изменения задачи:

![tickets update](/images/tickets_update.jpg)

Страница чата:

![Chat](/images/chat.jpg)

Окно создания группы чата:

![Chat group](/images/chatgroup.jpg)

Окно панели администратора:

![Adminpanel](/images/adminpanel.jpg)

Окно информации пользователя:

![User page](/images/userpage.jpg)

Аналитика текущего пользователя:

![User analytic](/images/Analytic.jpg)
