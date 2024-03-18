# Authentication API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![H2 DataBase](https://img.shields.io/badge/H2-blue?style=for-the-badge&logo=H2)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

This project is an API built using **Java, Java Spring, H2 as the database, and Spring Security and JWT for authentication control.**

The API was developed for me as part of the assessment process for a teaching position at Ada
## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Database](#database)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/DalsinJr/moviesBattle.git
```

2. Install dependencies with Maven


## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080


## API Endpoints
The API provides the documentation using Swagger:

```markdown
GET  /swagger-ui.html - Retrieve the API documentation.
GET  /h2-console - Access the H2 database console.


POST /auth/login - Login into the App
POST /auth/register - Register a new user into the App
GET  /match - Retrieve a valid match for the logged user
POST /match/{movieId} to vote for a movie
POST /match/finish to finish the match for the logged user
GET  /ranking - Retrieve the ranking of matches
```

## Database
The project utilizes [H2 DataBase](https://www.h2database.com/html/main.html) for development and testing purposes. The database is configured to be in-memory.


