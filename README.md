# Приложение списка покупок
___

## API

### post /list

Добавление нового элемента в список\
Тело запроса:\
{\
    "name": "string",\
    "price": double,\
    "amount": int\
}

### delete /list/{id}

Удаление элемента с id

### post /list/update/{id}

Обновление элемента с id
Тело запроса:\
{\
"name": "string",\
"price": double,\
"amount": int\
}

### get /list/{id}

Получение элемента с id

### get /list

Получение списка покупок