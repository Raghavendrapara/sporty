-- V3__Add_Indexes.sql

CREATE INDEX idx_venues_location ON venues(location);

CREATE INDEX idx_slots_start_time ON venue_slots(start_time);

CREATE INDEX idx_sports_name ON sports(name);