# guard-api

Получить всех юристов
GET - запрос
/api/v1/lawyer/lawyers

Response
```json
{
    "lawyers": [
        {
            "id": 3,
            "firstName": "cool_lawyer",
            "lastName": "guy",
            "email": "bestlawyer@email.com",
            "phoneNumber": "3434535",
            "photo": "",
            "averageRate": 5.0,
            "cityCode": 99,
            "countryCode": 7
        },
        {
            "id": 22,
            "firstName": "qwe",
            "lastName": "qwe",
            "email": "qwe@email.com",
            "phoneNumber": null,
            "photo": null,
            "averageRate": null,
            "cityCode": 99,
            "countryCode": 7
        }
    ]
}
```
