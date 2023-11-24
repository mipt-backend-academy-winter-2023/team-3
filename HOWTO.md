## Как запустить проект

- скопировать файл `./.env.example` в `./.env`
  ```shell
  cp ./.env.example ./.env
  ```
- запустить сервисы, используя `compose.yaml`
  ```shell
  docker-compose up --build -d
  ```
    - по-умолчанию `auth-service` доступен на порту `8082`
    - по-умолчанию `routing-service` доступен на порту `8084`
    - по-умолчанию `image-service` доступен на порту `8090`
- остановить сервисы, используя `compose.yaml`
  ```shell
  docker-compose down
  ```