## Как запустить проект
- скопировать файл `./.env.example` в `./.env`
  ```shell
  cp ./.env.example ./.env
  ```
- запустить `compose.yaml`
  ```shell
  docker-compose up -d
  ```
- по-умолчанию `auth-service` доступен на порту `8082`
- по-умолчанию `routing-service` доступен на порту `8084`
