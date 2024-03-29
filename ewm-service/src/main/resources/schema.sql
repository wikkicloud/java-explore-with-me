--DROP TABLE IF EXISTS users CASCADE;
--DROP TABLE IF EXISTS categories CASCADE;
--DROP TABLE IF EXISTS events CASCADE;
--DROP TABLE IF EXISTS event_requests CASCADE;
--DROP TABLE IF EXISTS compilations CASCADE;
--DROP TABLE IF EXISTS compilation_event CASCADE;


CREATE TABLE IF NOT EXISTS users (
   id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   email VARCHAR(512) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published TIMESTAMP WITHOUT TIME ZONE,
    category_id INTEGER REFERENCES categories (id),
    initiator_id INTEGER REFERENCES users (id),
    paid BOOLEAN NOT NULL,
    request_moderation BOOLEAN NOT NULL,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,
    participant_limit INT NOT NULL,
    state VARCHAR(50) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS event_requests (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id INTEGER REFERENCES events (id) ON DELETE CASCADE,
    requester_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
    state VARCHAR(50) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT unique_event_id_requester_id UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_event (
    compilation_id INTEGER REFERENCES compilations (id) ON DELETE CASCADE,
    event_id INTEGER REFERENCES events (id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);

