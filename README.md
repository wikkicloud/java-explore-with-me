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
Подписка
```json
{
    "id": 3,
    "name": "Usver",
    "email": "email189@yandex.ru",
    "subscriptions": [
        {
            "id": 8,
            "name": "Usver",
            "email": "email198@gmail.com"
        },
        {
            "id": 21,
            "name": "Usver",
            "email": "email222@yandex.ru"
        },
        {
            "id": 12,
            "name": "Usver",
            "email": "email205@gmail.com"
        },
        {
            "id": 4,
            "name": "Usver",
            "email": "email191@yandex.ru"
        },
        {
            "id": 5,
            "name": "Usver",
            "email": "email193@yandex.ru"
        },
        {
            "id": 23,
            "name": "Usver",
            "email": "email225@yandex.ru"
        }
    ]
}
```

События  
```json
[
    {
        "title": "title",
        "id": 4,
        "annotation": "annotation169119129619612961691616161651651",
        "category": {
            "id": 7,
            "name": "name192"
        },
        "confirmedRequests": 0,
        "eventDate": "2095-09-06 13:30:38",
        "initiator": {
            "id": 4,
            "name": "Usver"
        },
        "paid": true,
        "views": 0
    },
    {
        "title": "title",
        "id": 5,
        "annotation": "annotation169119129619612961691616161651651",
        "category": {
            "id": 8,
            "name": "name194"
        },
        "confirmedRequests": 0,
        "eventDate": "2095-09-06 13:30:38",
        "initiator": {
            "id": 5,
            "name": "Usver"
        },
        "paid": true,
        "views": 0
    },
    {
        "title": "Сап прогулки по рекам и каналам",
        "id": 8,
        "annotation": "Сап прогулки по рекам и каналам – это возможность увидеть Практикбург с другого ракурса",
        "category": {
            "id": 11,
            "name": "name199"
        },
        "confirmedRequests": 0,
        "eventDate": "2027-09-11 23:02:19",
        "initiator": {
            "id": 8,
            "name": "Usver"
        },
        "paid": true,
        "views": 0
    }
]
```