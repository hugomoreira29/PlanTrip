CREATE TABLE expenses
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    currency    varchar(3)                                      NOT NULL,
    amount      decimal(9, 2)                                   NOT NULL,
    category    varchar(150)                                    NOT NULL,
    description varchar(50),
    trip_id     BIGINT                                          NOT NULL REFERENCES trips (id) ON DELETE CASCADE,
    CONSTRAINT verify_amount CHECK (amount > 0)
)