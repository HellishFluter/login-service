# Environment variables

| Name                            | Required  | Default                                 | Description                                  | Example                                       |
|---------------------------------|-----------|-----------------------------------------|----------------------------------------------|-----------------------------------------------|
| POSTGRESQL_JDBC_DRIVER          | No        | org.postgresql.Driver                   | Postgres DB driver                           | org.postgresql.Driver                         |
| POSTGRESQL_JDBC_URL             | Yes       |                                         | Postgres DB url                              | jdbc:postgresql://postgres:5432/login_service |
| POSTGRESQL_JDBC_USERNAME        | Yes       |                                         | Postgres DB user                             | admin                                         |
| POSTGRESQL_JDBC_PASSWORD        | Yes       |                                         | Postgres DB user password                    | admin                                         |
| HIBERNATE_DDL_AUTO              | No        | validate                                | Hibernate schema generation strategies       | validate                                      |
| HIBERNATE_DIALECT               | No        | org.hibernate.dialect.PostgreSQLDialect | Bridge between Java JDBC types and SQL types | org.hibernate.dialect.PostgreSQLDialect       |
| ALLOWED_URLS                    | Yes       | *                                       | URLs for which access is allowed             | https://login.weber.ru, http://localhost:3000 |
| HTTP_SERVER_HOST                | No        | 0.0.0.0                                 | http server adress                           | 0.0.0.0                                       |
| HTTP_SERVER_PORT                | No        | 8080                                    | http server port                             | 8080                                          |

