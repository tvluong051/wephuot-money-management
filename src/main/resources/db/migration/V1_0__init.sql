CREATE SCHEMA IF NOT EXISTS money_management;
SET SEARCH_PATH TO money_management;

CREATE TABLE IF NOT EXISTS users (
  user_id VARCHAR(50) PRIMARY KEY,
  email VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS trips (
  trip_id VARCHAR(50) PRIMARY KEY,
  name VARCHAR(200),
  description TEXT,
  status SMALLINT,
  cover_photo VARCHAR(200),
  created_date BIGINT,
  last_modified BIGINT
);

CREATE TABLE IF NOT EXISTS participants (
  id BIGSERIAL PRIMARY KEY,
  trip_id VARCHAR(50) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  UNIQUE (trip_id, user_id)
);

CREATE TABLE IF NOT EXISTS spendings (
  id BIGSERIAL PRIMARY KEY,
  trip_id VARCHAR(50) NOT NULL,
  description VARCHAR(200) NOT NULL,
  spent_date BIGINT NOT NULL,
  amount FLOAT8 NOT NULL,
  equally_divided BOOLEAN,
  crediter VARCHAR(50) NOT NULL,
  FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
  FOREIGN KEY (crediter) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS shareparts (
  id BIGSERIAL PRIMARY KEY,
  spending_id BIGINT NOT NULL,
  amount FLOAT8 NOT NULL,
  debiter VARCHAR(50) NOT NULL,
  FOREIGN KEY (spending_id) REFERENCES spendings(id),
  FOREIGN KEY (debiter) REFERENCES users(user_id),
  UNIQUE (spending_id, debiter)
);
