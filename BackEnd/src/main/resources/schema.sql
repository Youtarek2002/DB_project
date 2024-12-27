CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    fname VARCHAR(100),
    lname VARCHAR(100),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(200) UNIQUE NOT NULL,
    phone VARCHAR(100),
    number_of_reservations INT DEFAULT 0,
    password VARCHAR(100) NOT NULL,
    payment_method VARCHAR(100),
    role ENUM('DRIVER', 'PARKING_LOT_MANAGER', 'SYSTEM_ADMIN') NOT NULL,
    license_plate VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    status ENUM('PENDING', 'SENT', 'FAILED', 'DELIVERED') NOT NULL,
    time TIME,
    message VARCHAR(500),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    amount INT,
    time TIME
);



CREATE TABLE IF NOT EXISTS parking_lots (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    disabled_count INT NOT NULL DEFAULT 0,
    regular_count INT NOT NULL DEFAULT 0,
    EV_count INT NOT NULL DEFAULT 0,
    disabled_price INT NOT NULL DEFAULT 0,
    regular_price INT NOT NULL DEFAULT 0,
    EV_price INT NOT NULL DEFAULT 0,
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
#     if New.start_time<=now() then
#         UPDATE parking_spots SET status = 'RESERVED'  WHERE id = NEW.parking_spot_id;
#     end if;
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

create table IF NOT EXISTS time_slots(
 id int auto_increment primary key not null,
 status	enum('OCCUPIED','AVAILABLE','RESERVED') not null DEFAULT 'AVAILABLE',
 parking_spot_id int,
 start_time	time,
 end_time time,
 foreign key (parking_spot_id) references parking_spots(id) on delete cascade
);


CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    penalty INT,
    start_time DATETIME,
    end_time DATETIME,
    duration TIMESTAMP,
    user_id INT,
    parking_spot_id INT,
    transaction_id INT,
    cost INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spots(id) ON DELETE CASCADE,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tokens (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
