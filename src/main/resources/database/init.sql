DROP TABLE IF EXISTS money_management.participants;
DROP TABLE IF EXISTS money_management.shareparts;
DROP TABLE IF EXISTS money_management.spendings;
DROP TABLE IF EXISTS money_management.users;
DROP TABLE IF EXISTS money_management.trips;

DROP SCHEMA IF EXISTS money_management;
CREATE SCHEMA money_management;
SET SEARCH_PATH TO money_management;

CREATE TABLE users (
  user_id VARCHAR(50) PRIMARY KEY,
  email VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE trips (
  trip_id VARCHAR(50) PRIMARY KEY,
  name VARCHAR(50),
  description TEXT,
  status SMALLINT
);

CREATE TABLE participants (
  id BIGINT PRIMARY KEY,
  trip_id VARCHAR(50) NOT NULL,
  user_id VARCHAR(50) NOT NULL,
  FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  UNIQUE (trip_id, user_id)
);

CREATE TABLE spendings (
  id BIGINT PRIMARY KEY,
  trip_id VARCHAR(50) NOT NULL,
  description VARCHAR(30) NOT NULL,
  spent_date BIGINT NOT NULL,
  amount FLOAT8 NOT NULL,
  crediter VARCHAR(50) NOT NULL,
  FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
  FOREIGN KEY (crediter) REFERENCES users(user_id)
);

CREATE TABLE shareparts (
  id BIGINT PRIMARY KEY,
  spending_id BIGINT NOT NULL,
  amount FLOAT8 NOT NULL,
  debiter VARCHAR(50) NOT NULL,
  FOREIGN KEY (spending_id) REFERENCES spendings(id),
  FOREIGN KEY (debiter) REFERENCES users(user_id)
);