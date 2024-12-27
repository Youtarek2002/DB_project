
create  table IF NOT EXISTS users(
id  int auto_increment primary Key not null,
fname  varchar(100),
lname	varchar(100),
username	varchar(100) unique not null,
email	varchar(200) unique not null,
phone	varchar(100),
number_of_reservations	int DEFAULT 0,
password	varchar(100) not null,
payment_method	varchar(100),
role	enum('DRIVER', 'PARKING_LOT_MANAGER', 'SYSTEM_ADMIN') not null,
license_plate	varchar(100));

create table IF NOT EXISTS notifications(
id int auto_increment primary Key not null,
status	enum('PENDING','SENT','FAILED','DELIVERED') not null,
time	time,
message	varchar(500),
user_id	int,
foreign key (user_id) references users(id) on delete cascade
);

create table IF NOT EXISTS transactions(
id	int auto_increment primary key not null,
amount	int,
time	time);


CREATE TABLE IF NOT EXISTS parking_lots (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    disabled_count INT NOT NULL DEFAULT 0,
    regular_count INT NOT NULL DEFAULT 0,
    EV_count INT NOT NULL DEFAULT 0,
    revenue INT NOT NULL DEFAULT 0,
    location VARCHAR(500) NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    parking_lot_manager INT,
    admitted BOOLEAN,
    FOREIGN KEY (parking_lot_manager) REFERENCES users(id)
);

create table IF NOT EXISTS parking_spots(
    id int auto_increment primary key not null,
    type	varchar(100),
    price	int DEFAULT 0,
    parking_lot_id	int,
    status	enum('OCCUPIED','AVAILABLE','RESERVED') not null DEFAULT 'AVAILABLE',
    revenue int DEFAULT 0,
    penalty int DEFAULT 0,
    foreign key (parking_lot_id) references parking_lots(id) on delete cascade
);
CREATE TABLE IF NOT EXISTS parking_spot_changes (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    parking_spot_id INT NOT NULL,
    old_status ENUM('OCCUPIED', 'AVAILABLE', 'RESERVED') NOT NULL,
    new_status ENUM('OCCUPIED', 'AVAILABLE', 'RESERVED') NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spots(id) ON DELETE CASCADE
);
# CREATE TRIGGER after_update_parking_spots
#     AFTER UPDATE ON parking_spots
#     FOR EACH ROW
# BEGIN
#     INSERT INTO parking_spot_changes (parking_spot_id, old_status, new_status)
#     VALUES (OLD.id, OLD.status, NEW.status);
# END;
#
# CREATE TRIGGER after_adding_reservations
#     AFTER INSERT ON reservations
#     FOR EACH ROW
# BEGIN
#     DECLARE spot_price DECIMAL(10, 2);
#     SELECT price INTO spot_price FROM parking_spots WHERE id = NEW.parking_spot_id;
#     UPDATE users SET number_of_reservations = number_of_reservations + 1 WHERE id = NEW.user_id;
#     UPDATE parking_lots
#     SET revenue = revenue + spot_price
#     WHERE id = (SELECT parking_lot_id FROM parking_spots WHERE id = NEW.parking_spot_id);
#     UPDATE parking_spots
#     SET revenue = revenue + spot_price
#     WHERE id = NEW.parking_spot_id;
#     UPDATE parking_spots SET status = 'RESERVED'  WHERE id = NEW.parking_spot_id;
# end;
# CREATE TRIGGER after_delete_reservations
#     AFTER DELETE ON reservations
#     FOR EACH ROW
# BEGIN
#     UPDATE parking_spots SET status = 'AVAILABLE' WHERE id = OLD.parking_spot_id;
# end;

# DROP TRIGGER IF EXISTS after_update_parking_spots;
# DROP TRIGGER IF EXISTS after_adding_reservations;
# DROP TRIGGER IF EXISTS after_delete_reservations;
# SHOW GRANTS FOR CURRENT_USER;
# ALTER TABLE parking_spot_changes DROP FOREIGN KEY parking_spot_changes_ibfk_1;
 create table IF NOT EXISTS time_slots(
 id int auto_increment primary key not null,
 status	enum('OCCUPIED','AVAILABLE','RESERVED') not null DEFAULT 'AVAILABLE',
 parking_spot_id int,
 start_time	time,
 end_time time,
 foreign key (parking_spot_id) references parking_spots(id) on delete cascade
  );

create table IF NOT EXISTS reservations(
id	int auto_increment primary key not null,
penalty	int,
start_time	datetime,
end_time	datetime,
duration	timestamp,
user_id		int,
parking_spot_id	int,
transaction_id	int,
foreign key (user_id) references users(id),
foreign key (parking_spot_id) references parking_spots(id),
foreign key (transaction_id) references transactions(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS tokens (
    id int auto_increment primary key not null,
    token VARCHAR(255) NOT NULL UNIQUE,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id int,
    foreign key (user_id) references users(id)
);
