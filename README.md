Данное приложение осуществляет поиск расходов в базе данных с возможностью выбора нескольких критериев.

Для запуска приложения необходимо:

1. Установленная СУБД postgtesql с pgAdmin 4 версии 6.1
2. Установленный контейнер сервлетов Apache Tomcat версии 9.0.58

Порядок запуска приложения:
1. В СУБД postgresql выполняем следующие действия:
	- запускаем pgAdmin 4;
	- задаем пароль стандартному пользователю. Пользователь: postgres, пароль: 12345 ;
	- выбираем БД postgres;
	- запускаем Query Tool;
	- выполняем SQL запрос create database из файла create_db_and_tables.sql, который находится в папке webapp\src\main\webapp\WEB-INF\;
	- после создания БД, переходим в нее, запускаем Query Tool и выполняем все остальные запросы из файла create_db_and_tables.sql;
	- после создания структуры, в том же Query Tool выполняем все запросы из файла add_data_to_tables.sql, который находится в папке webapp\src\main\webapp\WEB-INF\ 
	  Это наполнит БД тестовыми данными.
2. В контейнере сервлетов Apache Tomcat выполняем следующие действия:
	- запускаем менеджер приложений http://адрес сервера Tomcat/manager;
	- в менеджере приложений загружаем файл webapp.war из папки target и жмем "Развернуть";
	- после этого в таблице приложений появится новая строка /webapp
3. После этого, в браузере на сервере переходим по следующему адресу http://адрес сервера Tomcat/webapp
4. Если все выполнено правильно, то появится страница Welcome to expense_management, а также поля для выбора критериев поиска по базе данных.
5. Поле Category предназначено для выбора категории поиска. Для выбора всех категорий его значение должно быть равно Category. 
6. Поля календаря предназначены для выбора периода расходов с и по соответственно.
7. Кнопка Search предназначена для выполнения запроса в базу данных и отражения результата поиска.
8. После таблицы с результатами поиска отображается таблица со статистическими данными, основанными на результатах.
9. Поскольку в ТЗ указана структура таблиц с использованием внешнего ключа, то внести значение поля "Category" таблицы "expense", 
которое отсутствует в поле "id" таблицы "category"не представляется возможным из-за вышеуказанного ограничения.
