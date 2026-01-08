-- 01_init_schema.sql

-- 1. USERS
CREATE TABLE users (
                       id binary(16) PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. SPORTS
CREATE TABLE sports (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        external_id VARCHAR(100) NOT NULL UNIQUE
);

-- 3. VENUES
CREATE TABLE venues (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        location VARCHAR(255) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. VENUE_SLOTS
CREATE TABLE venue_slots (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             venue_id BIGINT NOT NULL,
                             start_time DATETIME NOT NULL,
                             end_time DATETIME NOT NULL,
                             is_booked BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY (venue_id) REFERENCES venues(id) ON DELETE CASCADE,
                             UNIQUE KEY uk_venue_slot_time (venue_id, start_time, end_time)
);

-- 5. BOOKINGS
CREATE TABLE bookings (
                          id binary(16) PRIMARY KEY,
                          user_id binary(16) NOT NULL,
                          venue_slot_id BIGINT NOT NULL,
                          sport_id INT NOT NULL,
                          booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          status ENUM('CONFIRMED', 'CANCELLED', 'COMPLETED') DEFAULT 'CONFIRMED',

                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                          FOREIGN KEY (venue_slot_id) REFERENCES venue_slots(id) ON DELETE CASCADE,
                          FOREIGN KEY (sport_id) REFERENCES sports(id) ON DELETE CASCADE
);



-- INDEX
CREATE INDEX idx_venues_location ON venues(location);

CREATE INDEX idx_slots_start_time ON venue_slots(start_time);

CREATE INDEX idx_sports_name ON sports(name);

-- INIT SPORTS

INSERT INTO sports (name, external_id) VALUES
                                           ('badminton', '7020104'),
                                           ('basketball', '7020111'),
                                           ('cricket', '7031809'),
                                           ('football', '7061509'),
                                           ('tennis', '7200514');


