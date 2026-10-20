CREATE TABLE trips (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(100) NOT NULL,
    destination varchar(100) NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL,
    CONSTRAINT verify_dates CHECK (end_date >= start_date)
);