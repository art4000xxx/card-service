# Card Transfer Service 💳

Приложение для перевода денег с карты на карту. Бэкенд — Spring Boot, фронтенд — React.

⚠️ Фронтенд не включён в этот репозиторий. 

---

## 🚀 Быстрый старт

Перейдите в папку `backend/card-service` и выполните:

```bash
docker-compose up --build
```

После запуска сервисы будут доступны по адресам:

* **Фронтенд**: [http://localhost:3000](http://localhost:3000)  (запускать отдельно из репозитория фронтенда)
* **Backend API**: [http://localhost:8080](http://localhost:8080)

---

## 📂 Структура проекта

```
card-transfer-service/
├── backend/
│   └── card-service/
│       └── docker-compose.yml
└── README.md
```

---

## 🧪 Примеры запросов к API

### POST /transfer

Тело запроса:

```json
{
  "cardFromNumber": "1111",
  "cardFromValidTill": "12/25",
  "cardFromCVV": "123",
  "cardToNumber": "2222",
  "amount": {
    "value": 10000,
    "currency": "RUB"
  }
}
```

Пример ответа:

```json
{
  "operationId": "op-123",
  "confirmationCode": "0000"
}
```

### POST /confirmOperation

Тело запроса:

```json
{
  "operationId": "op-123",
  "code": "0000"
}
```

Пример ответа:

```json
{
  "operationId": "op-123"
}
```

---

## ⚙️ Требования

* Java 17
* Docker
* Docker Compose

---

## 👨‍💻 Автор

Артем Леонтьев — [github.com/art4000xxx](https://github.com/art4000xxx)
