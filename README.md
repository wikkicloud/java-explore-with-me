# java-explore-with-me

https://github.com/wikkicloud/java-explore-with-me/pull/1

Это приложение дает возможность делиться информацией об
интересных событиях и помогать найти компанию для участия в них.

Проект основан на микросервисной архетиктре, поэтому для его запуска 
понадобится Docker

Для запуска сервиса:
- необходимо создать пакеты jar `mvn clean package`
- в корне проекта выполнить команду
`docker compose up`

По-умолчанию сервис запуститься на порте 8080, но его можно изменить в файле
`docker-compose.yml`. 

Спецификации API:  
- [Спецификация основного сервиса](ewm-main-service-spec.json)
- [Спецификация сервиса статистики](ewm-stats-service-spec.json)

Для просмотра спецификации можно пользоватся сервисом  
[Swagger Editor](https://editor.swagger.io/)

ERD схема базы данных сервиса 

![ERD схема](ERD-service.png)

### Дополнителный функционал

Для разработки фичи я выбрал подписку на пользователя оба варианта

- Показывает сбытия созданные пользователем на которых подписан текущий пользователь
- Показывает события в которых будет участвовать пользователь на которого есть подписка

Эндпоинты сервиса подписки:
- Подписка на пользователя  
`POST http://localhost:8080/users/3/subscriptions/12`
- Отписка от пользователя  
`PATCH http://localhost:8080/users/3/subscriptions/12/remove`
- События инициатором которых является пользователь в подписках
`GET http://localhost:8080/users/3/subscriptions`
- События в которых будет участвовать пользователь, т.е. с подтвержденной заявкой на участие  
`GET http://localhost:8080/users/3/subscriptions/participant`

Примеры ответов:  

События возвращает объект Page
```json
{
  "content": [
    {
      "title": "Perferendis placeat eos facere recusandae.",
      "id": 7,
      "annotation": "Nihil distinctio inventore error enim dolores et placeat. Corrupti et autem magnam quis. Est debitis est commodi hic commodi blanditiis atque eos porro. Qui omnis et sit esse deleniti velit. Id et soluta.",
      "category": {
        "id": 10,
        "name": "Somalia synthesizing"
      },
      "confirmedRequests": 0,
      "eventDate": "2022-10-04 16:46:39",
      "initiator": {
        "id": 7,
        "name": "Wilfred Ortiz"
      },
      "paid": false,
      "views": 0
    },
    {
      "title": "title",
      "id": 9,
      "annotation": "annotation169119129619612961691616161651651",
      "category": {
        "id": 12,
        "name": "name18"
      },
      "confirmedRequests": 0,
      "eventDate": "2095-09-06 13:30:38",
      "initiator": {
        "id": 9,
        "name": "Usver"
      },
      "paid": true,
      "views": 0
    }
  ],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```