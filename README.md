# Sporty - Sports Booking API

## 🛠 Tech Stack

* **Language:** Java 25 (Virtual Threads enabled)
* **Framework:** Spring Boot 4+
* **Database:** MySQL 9.0.4 (Running in Docker)
* **Migration:** Flyway
* **Testing:** HTTP Client (`requests.http`)

---

## ⚙️ Setup & Installation

### 1. Prerequisites
* Java 21+ installed
* Docker installed
* Maven installed (or use `./mvnw`)

### Docker run
```bash
docker compose up --build
```
### If Local Run
Use Docker to spin up the MySQL container. The app is configured to connect to `localhost:3306`.

```bash
docker run -d \
    --name mysql-container \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=sports_booking \
    mysql:9.0.4
```
```bash
mvn spring-boot:run
```

## Configuration
All settings are in src/main/resources/application.properties.

## 📡 API Documentation

### 🔐 Authentication

#### Register User
* **URL:** `/users`
* **Method:** `POST`
* **Auth Required:** ❌
* **Body:**
    ```json
    {
      "name": "John Doe",
      "email": "john@example.com",
      "password": "password123"
    }
    ```

#### Login
* **URL:** `/auth/login`
* **Method:** `POST`
* **Auth Required:** ❌
* **Body:**
    ```json
    {
      "email": "john@example.com",
      "password": "password123"
    }
    ```
* **Response:** `{ "token": "eyJhbGciOiJIUzI1Ni..." }`

---

### 🏟️ Venues

#### Create Venue
* **URL:** `/venues`
* **Method:** `POST`
* **Auth Required:** ✅ (Admin)
* **Body:**
    ```json
    {
      "name": "City Sports Complex",
      "location": "Downtown"
    }
    ```

#### Get All Venues
* **URL:** `/venues`
* **Method:** `GET`
* **Query Params:** `?location=Downtown` (Optional)
* **Auth Required:** ❌

#### Get Venue Details
* **URL:** `/venues/{id}`
* **Method:** `GET`
* **Auth Required:** ❌

#### Add Slot to Venue
* **URL:** `/venues/{id}/slots`
* **Method:** `POST`
* **Auth Required:** ✅ (Admin)
* **Body:**
    ```json
    {
      "startTime": "2026-02-01T10:00:00",
      "endTime": "2026-02-01T11:00:00"
    }
    ```

#### Get All Slots for Venue
* **URL:** `/venues/{id}/slots`
* **Method:** `GET`
* **Auth Required:** ❌

#### Check Availability
* **URL:** `/venues/available`
* **Method:** `GET`
* **Query Params:**
    * `start`: ISO DateTime (e.g., `2026-02-01T09:00:00`)
    * `end`: ISO DateTime
    * `sport_id`: Integer (Optional)
* **Auth Required:** ❌

---

### 📅 Bookings

#### Create Booking
* **URL:** `/bookings`
* **Method:** `POST`
* **Auth Required:** ✅ (User)
* **Body:**
    ```json
    {
      "user_id": "uuid-string-here",
      "venue_slot_id": 1,
      "sport_id": 1
    }
    ```

#### Cancel Booking
* **URL:** `/bookings/{id}/cancel`
* **Method:** `PUT`
* **Auth Required:** ✅ (User)
