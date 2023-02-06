## Spring Boot Datasource Hot Reload (PostgreSQL with TLS 1.3)

1. Download and install [Postgres.app](https://postgresapp.com/downloads.html)

<img src="https://raw.githubusercontent.com/dredwardhyde/spring-boot-datasource-hot-reload/main/readme/postgres_app.png" width="700"/>  

2. Add [server certificates](https://github.com/dredwardhyde/spring-boot-datasource-hot-reload/tree/main/server_certs) to the **data** folder

<img src="https://raw.githubusercontent.com/dredwardhyde/spring-boot-datasource-hot-reload/main/readme/certs_location.png" width="700"/>  

3. Edit **postgresql.conf**

<img src="https://raw.githubusercontent.com/dredwardhyde/spring-boot-datasource-hot-reload/main/readme/postgresql_conf_settings.png" width="700"/>  

4. Restart server
5. Check if TLS is enabled
<img src="https://raw.githubusercontent.com/dredwardhyde/spring-boot-datasource-hot-reload/main/readme/terminal_tls_psql.png" width="700"/>  

6. Create schema & user
```sql
CREATE SCHEMA IF NOT EXISTS test;
CREATE USER test WITH PASSWORD 'test';
GRANT ALL ON SCHEMA test TO test;
```

7. Run [ServerApplication](https://github.com/dredwardhyde/spring-boot-datasource-hot-reload/blob/main/src/main/kotlin/com/test/server/ServerApplication.kt)

8. Change contents of [folder_to_monitor](https://github.com/dredwardhyde/spring-boot-datasource-hot-reload/tree/main/folder_to_monitor) directory (create file or something)

9. Watch datasource reloading
