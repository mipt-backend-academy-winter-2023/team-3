# team-3

## Как запустить проект
- скопировать файл `./docker-compose/.env.example` в `./docker-compose/.env`
- запустить `docker-compose.yml` в папке `./docker-compose`
    ```shell
    cd ./docker-compose
    docker-compose up -d
    ```
 - по-умолчанию `auth-service` доступен на порту `8082`
 - по-умолчанию `routing-service` доступен на порту `8084`
