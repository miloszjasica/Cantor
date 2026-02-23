CREATE TABLE shedlock (
    name VARCHAR(64) NOT NULL,
    lock_until TIMESTAMP NOT NULL,
    locked_at TIMESTAMP NOT NULL,
    locked_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE rate_snapshots (
    id SERIAL PRIMARY KEY,
    effective_date DATE NOT NULL
);

CREATE TABLE rates (
    id SERIAL PRIMARY KEY,
    currency VARCHAR(3) NOT NULL,
    rate DECIMAL(19, 4) NOT NULL,
    snapshot_id INTEGER NOT NULL,
    FOREIGN KEY (snapshot_id) REFERENCES rate_snapshots(id) ON DELETE CASCADE
);

