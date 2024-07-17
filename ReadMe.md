# Тестовое задание CaseLab Java

## Задача
Необходимо реализовать микросервис, который будет выполнять роль хранилища различных файлов и их атрибутов.
Микросервис должен предоставлять HTTP API и принимать/отдавать запросы/ответы в формате JSON.
Разработка UI-интерфейса не требуется. Для тестирования можно использовать Postman или аналог.
Должны быть реализованы следующие API-методы:
1. **Создание файла.** </br>
На вход методу отправляется JSON, включающий в себя файл (в формате base64) и его атрибуты (название - title, дата и время отправки - creation_date, краткое описание документа - description), на выходе метод возвращает id созданного файла.
2. **Получение файла.**</br>
На вход методу отправляется id файла, на выходе метод возвращает JSON, включающий в себя файл (в формате base64) и его атрибуты (название - title, дата и время отправки - creation_date, краткое описание документа - description)

## Технологический стек
* Микросервис реализован на - Java + Spring Boot
* Для хранения данных - СУБД PostgreSQL
* Для миграции базы данных - Liquibase
* Для покрытия тестами - JUnit и Mockito
* Для сборки и контейнеризации - Docker и Docker-compose

## Описание решения



## Запуск приложения
``` bash
docker-compose up -d
```
После выполнения данной команды будет произведены:
1. Запуск базы данных PostgreSQL
2. Сборка микросервиса
3. Копирование файлов для запуска
4. Запуск миросервиса + создание таблицы в бд + заполнение таблицы тестовыми данными (10000 строк)

## Примеры тестовых запросов
Для удобства был создан [файл с документацией](https://github.com/A192747/File-Handler/blob/main/file-handler-documentation.yaml)

### Запрос на добавление нового файла:
<span style="color:lightgreen">В случае правильного тела запроса:</span>
```http request
POST http://localhost:8081/api/v1/files
```
``` json
{
    "title": "Title",
    "creation_date": "2019-08-24T14:15:22Z",
    "description": "Some file",
    "file": "base64file"
}
```
<span style="color:lightgreen">В ответ клиент получает id сохраненного файла:</span>
``` json
1
```


<span style="color:pink">В случае неправильного тела запроса:</span>
``` json
{
    "title": "",
    "creation_date": "2019-08-24T14:15:22Z",
    "description": "Some file",
    "file": ""
}
```
<span style="color:pink">В ответ клиент получает:</span>
``` json
{
    {
    "errors": [
        {
            "message": "title - must not be blank"
        },
        {
            "message": "data - must not be blank"
        }
    ],
    "timestamp": "2024-07-17T15:48:33.830+00:00"
}
}
```

### Запрос на получение файла по id:
<span style="color:lightblue">id файла - целое положительное число (> 0)</span>
```http request
GET http://localhost:8081/api/v1/files/{id}
```


<span style="color:lightgreen">В случае правильного запроса</span> </br>
```http request
GET http://localhost:8081/api/v1/files/1
```
<span style="color:lightgreen">В ответ клиент получает:</span>
```json
{
  "title": "Title",
  "creation_date": "2019-08-24T14:15:22Z",
  "description": "Some file",
  "file": "base64file"
}
```
<span style="color:pink">В случае неправильного запроса:</span>
```http request
GET http://localhost:8081/api/v1/files/0
```
``` json
{
    "errors": [
        {
            "message": "getFileById.fileId - must be greater than or equal to 1"
        }
    ],
    "timestamp": "2024-07-17T15:59:35.232+00:00"
}
```
```http request
GET http://localhost:8081/api/v1/files/asdf
```
``` json
{
    "timestamp": "2024-07-17T16:00:14.987+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/v1/files/asdf"
}
```

### Запрос на получение списка всех файлов (и их атрибутов):
В данном запросе мной была реализована пагинация и сортировка по времени создания файлов

Для запроса существуют 3 необязательных параметра:
* **page** - номер необходимой сраницы, если не указан, то page=1
* **limit** - количество элементов на одной старнице, если не указан, то limit=5
* **sortDirection** - сортировка по возрастанию даты или по убыванию, если не указан, то sortDirection=DESC (От новых к старым)

Также каждое из полей имеет свои ограничения:
* **page** - Min(value = 1)
* **limit** - Min(value = 1) и Max(value = 100). Решил указать макс. значение, чтобы пользователь не мог выбрать все данные из бд и тем самым сильно её нагрузить
* **sortDirection** - дожден быть либо **DESC**, либо **ASC**

<span style="color:lightgreen">Примеры валидных запросов:</span> </br>
<span style="color:lightgreen">1. Запрос (без параметров)</span>
```http request
GET http://localhost:8081/api/v1/files
```
Ответ:
``` json
{
    "files": [
        {
            "title": "a22be8318b6e234e1672d64b3a4b4bdb",
            "creation_date": "2024-07-12T00:00:00.000+00:00",
            "description": "0c57c0cc1a85d2fb2c62e27e2ffeed62",
            "file": "c3e2ebf18b9d1b859ba23c7d1af65f3a"
        },
        {
            "title": "852a432e4e6823666e0352ff160d3580",
            "creation_date": "2024-07-12T00:00:00.000+00:00",
            "description": "7b908f293631b9445513ee12484727a7",
            "file": "79e13afd2649aa76601aa793c14319c9"
        },
        {
            "title": "d824200eda97764ca0667a78c7cc553f",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "fd08568a266afed72af0234c1294fc84",
            "file": "cc8eaa5c075f02a853cbfcfc85b41298"
        },
        {
            "title": "09c3b3bd328c262ebe3bbf9446a57e2d",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "34397d76ce313b03a6eb3679d17dca9d",
            "file": "2289eb47603313fe6cc1a2199a5a9d20"
        },
        {
            "title": "ae7d14461d252bf257280b520256f26b",
            "creation_date": "2024-07-07T00:00:00.000+00:00",
            "description": "9431dbc68fb84410d5c379b2c05855e3",
            "file": "eac5961444ebeb94d911e7526c10fd1a"
        }
    ],
    "total_files": 10007,
    "total_pages": 2002,
    "current_page": 1
}
```
<span style="color:lightgreen">2. Запрос (с параметром page)</span>
```http request
GET http://localhost:8081/api/v1/files?page=2
```
Ответ:
``` json
{
    "files": [
        {
            "title": "b5f4333f2c1833d8fe5812086eccd9f7",
            "creation_date": "2024-07-06T00:00:00.000+00:00",
            "description": "3ca51292708f7d6b71815483948fa463",
            "file": "fc1498d9345fb3a656de68d0ea6029fa"
        },
        {
            "title": "5246e0aa65de3595ddb4b015a0aedecd",
            "creation_date": "2024-07-06T00:00:00.000+00:00",
            "description": "4ae7de5a8a326b74b735dc2bbe4092c9",
            "file": "782175fbc95fca37cb9e82c79b1eddd5"
        },
        {
            "title": "2e0d441f026ec3f899ddf67fa7acf460",
            "creation_date": "2024-07-05T00:00:00.000+00:00",
            "description": "50aad3fd1a0a343338fb565982b41175",
            "file": "202a6069040eba44d5104cc27ed2cdba"
        },
        {
            "title": "10c19a68e7826a2d97c965ba7db0bc50",
            "creation_date": "2024-07-03T00:00:00.000+00:00",
            "description": "24a9db9eacd4dfdf1bf5ffcdb8f4b2b9",
            "file": "2fbdf1336f344bef9491a39a40b86522"
        },
        {
            "title": "28307b76e6e06fe5928fc4b63503ae28",
            "creation_date": "2024-07-03T00:00:00.000+00:00",
            "description": "d78c2f12cd9fb427cc7142010ce1263f",
            "file": "c11c19301eeeb61da57b14f79a8abc2d"
        }
    ],
    "total_files": 10007,
    "total_pages": 2002,
    "current_page": 2
}
```
<span style="color:lightgreen">3. Запрос (с параметром limit)</span>
```http request
GET http://localhost:8081/api/v1/files?limit=1
```
Ответ:
``` json
{
    "files": [
        {
            "title": "a22be8318b6e234e1672d64b3a4b4bdb",
            "creation_date": "2024-07-12T00:00:00.000+00:00",
            "description": "0c57c0cc1a85d2fb2c62e27e2ffeed62",
            "file": "c3e2ebf18b9d1b859ba23c7d1af65f3a"
        }
    ],
    "total_files": 10007,
    "total_pages": 10007,
    "current_page": 1
}
```
<span style="color:lightgreen">3. Запрос (с параметром limit)</span>
```http request
GET http://localhost:8081/api/v1/files?limit=1
```
Ответ:
``` json
{
    "files": [
        {
            "title": "a22be8318b6e234e1672d64b3a4b4bdb",
            "creation_date": "2024-07-12T00:00:00.000+00:00",
            "description": "0c57c0cc1a85d2fb2c62e27e2ffeed62",
            "file": "c3e2ebf18b9d1b859ba23c7d1af65f3a"
        }
    ],
    "total_files": 10007,
    "total_pages": 10007,
    "current_page": 1
}
```
<span style="color:lightgreen">3. Запрос (с параметром sortDirection)</span>
```http request
GET http://localhost:8081/api/v1/files?sortDirection=ASC
```
Ответ:
``` json
{
    "files": [
        {
            "title": "6956905eca5b09c4785215f7fb64fdcb",
            "creation_date": "2000-01-01T00:00:00.000+00:00",
            "description": "dcf8747b9cf04656af7c1b954a216da6",
            "file": "1c32ea82b80c3f215b6a0a1cca4bb85c"
        },
        {
            "title": "f31a2505265fa7afa255737ed6e8affc",
            "creation_date": "2000-01-02T00:00:00.000+00:00",
            "description": "98e02c48ee45702763c42910011b5289",
            "file": "c98ef34bb0a8c06bd1fe54d5727d8950"
        },
        {
            "title": "5ced6f7a53b120a8739f81e29736ebd5",
            "creation_date": "2000-01-03T00:00:00.000+00:00",
            "description": "97650f612c20d623c1b8ed8ff02ecfae",
            "file": "c5ede03f198885e52c29d06ef58ba9fd"
        },
        {
            "title": "1d71afabcac5a0373499144b9623e7a0",
            "creation_date": "2000-01-03T00:00:00.000+00:00",
            "description": "8c760d5cc34976e57f8446c83f9dce83",
            "file": "f62b44f4b952b09b4a7b4364982b5eb8"
        },
        {
            "title": "807960a5ede0210245929de56587d9bd",
            "creation_date": "2000-01-03T00:00:00.000+00:00",
            "description": "7efcd4dedaacc204b610615f9d372fd6",
            "file": "152872b38b2c54785e78d32bc564b43d"
        }
    ],
    "total_files": 10007,
    "total_pages": 2002,
    "current_page": 1
}
```
<span style="color:lightgreen">4. Запрос (с параметром page + limit)</span>
```http request
GET http://localhost:8081/api/v1/files?page=2&limit=2
```
Ответ:
``` json
{
    "files": [
        {
            "title": "09c3b3bd328c262ebe3bbf9446a57e2d",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "34397d76ce313b03a6eb3679d17dca9d",
            "file": "2289eb47603313fe6cc1a2199a5a9d20"
        },
        {
            "title": "d824200eda97764ca0667a78c7cc553f",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "fd08568a266afed72af0234c1294fc84",
            "file": "cc8eaa5c075f02a853cbfcfc85b41298"
        }
    ],
    "total_files": 10007,
    "total_pages": 5004,
    "current_page": 2
}
```

<span style="color:lightgreen">5. Запрос (с параметром page + sortDirection)</span>
```http request
GET http://localhost:8081/api/v1/files?page=2&sortDirection=ASC
```
Ответ:
``` json
{
    "files": [
        {
            "title": "807960a5ede0210245929de56587d9bd",
            "creation_date": "2000-01-03T00:00:00.000+00:00",
            "description": "7efcd4dedaacc204b610615f9d372fd6",
            "file": "152872b38b2c54785e78d32bc564b43d"
        },
        {
            "title": "1d71afabcac5a0373499144b9623e7a0",
            "creation_date": "2000-01-03T00:00:00.000+00:00",
            "description": "8c760d5cc34976e57f8446c83f9dce83",
            "file": "f62b44f4b952b09b4a7b4364982b5eb8"
        },
        {
            "title": "60288da5bc42051032d902db9ad90dc6",
            "creation_date": "2000-01-04T00:00:00.000+00:00",
            "description": "b1c429dad88b2b87df46f97fba5dcd37",
            "file": "d0433fda6587997be4428ce77f79fa5e"
        },
        {
            "title": "e7b4cf6e98a402098135146770f1dbf3",
            "creation_date": "2000-01-05T00:00:00.000+00:00",
            "description": "79159a92e2d5e7b0454609e83961c51b",
            "file": "8df700066874e3afe079e6c31d15a6f8"
        },
        {
            "title": "826bd9910289b4c5e74e76ef7771f4ca",
            "creation_date": "2000-01-06T00:00:00.000+00:00",
            "description": "87c1765c62709a683959c2a5642a270a",
            "file": "9026028bc0c7621a7ba986f5826f63f2"
        }
    ],
    "total_files": 10007,
    "total_pages": 2002,
    "current_page": 2
}
```
<span style="color:lightgreen">6. Запрос (с параметром limit + sortDirection)</span>
```http request
GET http://localhost:8081/api/v1/files?limit=2&sortDirection=ASC
```
Ответ:
``` json
{
    "files": [
        {
            "title": "6956905eca5b09c4785215f7fb64fdcb",
            "creation_date": "2000-01-01T00:00:00.000+00:00",
            "description": "dcf8747b9cf04656af7c1b954a216da6",
            "file": "1c32ea82b80c3f215b6a0a1cca4bb85c"
        },
        {
            "title": "f31a2505265fa7afa255737ed6e8affc",
            "creation_date": "2000-01-02T00:00:00.000+00:00",
            "description": "98e02c48ee45702763c42910011b5289",
            "file": "c98ef34bb0a8c06bd1fe54d5727d8950"
        }
    ],
    "total_files": 10007,
    "total_pages": 5004,
    "current_page": 1
}
```

<span style="color:lightgreen">7. Запрос (с параметром page + limit + sortDirection)</span>
```http request
GET http://localhost:8081/api/v1/files?page=2&limit=2&sortDirection=DESC
```
Ответ:
``` json
{
    "files": [
        {
            "title": "09c3b3bd328c262ebe3bbf9446a57e2d",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "34397d76ce313b03a6eb3679d17dca9d",
            "file": "2289eb47603313fe6cc1a2199a5a9d20"
        },
        {
            "title": "d824200eda97764ca0667a78c7cc553f",
            "creation_date": "2024-07-11T00:00:00.000+00:00",
            "description": "fd08568a266afed72af0234c1294fc84",
            "file": "cc8eaa5c075f02a853cbfcfc85b41298"
        }
    ],
    "total_files": 10007,
    "total_pages": 5004,
    "current_page": 2
}
```










