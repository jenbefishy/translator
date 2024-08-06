# Translator API

This project is a web application developed in Java for translating a set of words from one language to another using an external translation service [Yandex Translator](https://yandex.cloud/en-ru/services/translate). The application supports multi-threaded translation and stores request details in a relational database.

## Features

- **Translation**: Translates a string of words from a source language to a target language.
- **Multi-threaded Processing**: Each word is translated in a separate thread.
- **Database Logging**: Stores the user's IP address, input string, and translation result in a relational database.
- **Spring Boot Framework**: Uses Spring Boot for application setup and REST API implementation.
- **JDBC for Database Access**: Utilizes JDBC for database interactions.
- **External API Calls**: Leverages RestTemplate for calling external translation services.

## Setup Instructions
### 1. Clone the Repository
```bash
git clone https://github.com/jenbefishy/translator
cd translator
```

### 2. Get API Key
Create an account on [Yandex Cloud](https://yandex.cloud/en-ru/services/translate), and go to the [console](https://console.yandex.cloud/). Create a new catalog, and in it, create a new service account and assign it the role `ai.translate`. Create a key pair and enter the Secret key in application.yaml at `src/main/resources/` (see Configuration).


### 3 Configuration
Enter your database URL, username, and password. The default path is the project directory.
```yaml
spring:
  datasource:
    url: jdbc:h2:file:./database
    username: <username>
    password: <password>
```
Configure your API key and save the file:
```yaml
translation:
  api:
    url: https://translate.api.cloud.yandex.net/translate/v2/translate
    key: <your-api-key>
```

### 4 Running 
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

Or if you need a `.jar` file:
```bash
./mvnw clean install
java -jar target/translator-0.0.1-SNAPSHOT.jar
```

### 5 Testing
```bash
./mvnw test
```

## API Endpoints
Translate Endpoint
```
URL: /api/v1/requests
```

**Method: Get** <br>
Return the list of requests from database<br>
**Method: POST**<br>
Request Parameters:<br>
**In url:**<br>
sourceLanguage: The source language code.<br>
targetLanguage: The target language code.<br>
**In body:**<br>
String: The text for translation<br>
### Example:
```
/api/v1/requests?sourceLanguage=en&targetLanguage=ru
```
Body:
```
Hello world, this is my first program
```

Out
```
200 Здравствуйте мир, этот является мой первый программа
```
