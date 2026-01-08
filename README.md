## Personal Suggestions/Changes/Assumptions

### Suggestions
#### Would like to add fuzzy search to ensure easy searching
#### Using postGres - as my research suggests its better suited owing to range query efficiency and wider usage and vector capability

### Assumptions
#### Using the API to fetch Sports - have created insert sql for starting and made the syncing mechanism async since it might create a blocking operation
#### Bookings currently done via Pessimistic Locking -> preventing multiple bookings at same slot
#### Added Spring Security, disabled it for running, was revising concepts so thought of adding/learning

### Continue to add
#### ToDo Change Location to actual address kinda columns so that searching becomes easier via state/city/area instead of 'LIKE' queries that are time taking
#### ToDo revisit the calling mechanism of API
#### ToDo revisit query performance and  scope for improving query performance
#### ToDo Expand ExceptionHandling and Nice UX(Custom Exceptions)

### My Current Assessment/Understanding of the desired flow
#### Venue Owner/StapuBox Admin can add Venues/Slots/
#### User can search via -> location/venue/sport -> display venues -> select slot -> book+pay
#### payment aspect is missing from code but can be extended, will perhaps continue to do it for my own learning after submission
#### No double bookings
#### Ability for UI to display a nice smooth BookMyShow kinda map of slots
#### Correct Status Codes and proper logging config



# Sporty - **StapuBox** Sports Booking API

## 🛠 Tech Stack

* **Language:** Java 25
* **Framework:** Spring Boot 4+
* **Database:** MySQL 9.0.4 (Running in Docker)
* **Migration:** Flyway
* **Testing:** HTTP Client (`requests.http`)

---

##  Setup & Installation

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

##  API Documentation

Please Refer to the gemini-requests.http file it runs nicely in IntelliJ IDE for more
Below are the mandatory ones

### Authentication

#### Register User
* **URL:** `/users`
* **Method:** `POST`
* **Body:**
    ```json
    {
      "name": "Donald Trump",
      "email": "gold@gold.com",
      "password": "YMCA"
    }
    ```
---

### Venues

#### Create Venue
* **URL:** `/venues`
* **Method:** `POST`
* **Body:**
    ```json
    {
      "name": "Mar-A-Lago",
      "location": "FloRida"
    }
    ```

#### Get All Venues
* **URL:** `/venues`
* **Method:** `GET`
* **Query Params:** `?location=florida` (Optional)

#### Get Venue Details
* **URL:** `/venues/{id}`
* **Method:** `GET`

#### Add Slot to Venue
* **URL:** `/venues/{id}/slots`
* **Method:** `POST`
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

#### Check Availability
* **URL:** `/venues/available`
* **Method:** `GET`
* **Query Params:**
    * `start`: ISO DateTime (e.g., `2026-02-01T09:00:00`)
    * `end`: ISO DateTime
    * `sport_id`: Integer (Optional)

---

### Bookings

#### Create Booking
* **URL:** `/bookings`
* **Method:** `POST`
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