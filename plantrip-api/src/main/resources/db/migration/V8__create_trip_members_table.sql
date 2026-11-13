CREATE TABLE trip_members
(
    trip_id BIGINT REFERENCES trips (id) ON DELETE CASCADE,
    user_id  BIGINT REFERENCES users (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, trip_id)
);