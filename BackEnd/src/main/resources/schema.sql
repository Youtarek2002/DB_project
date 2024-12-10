
create  table IF NOT EXISTS users(
id  int auto_increment primary Key not null,
fname  varchar(100),
lname	varchar(100),
username	varchar(100) unique not null,
email	varchar(200) unique not null,
phone	varchar(100),
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
    location VARCHAR(500) NOT NULL,
    parking_lot_manager INT ,
    admitted BOOLEAN,
    FOREIGN KEY (parking_lot_manager) REFERENCES users(id)
);

create table IF NOT EXISTS parking_spots(
id int auto_increment primary key not null,
type	varchar(100),
price	int DEFAULT 0,
parking_lot_id	int,
foreign key (parking_lot_id) references parking_lots(id) on delete cascade
 );
 create table IF NOT EXISTS time_slots(
 id int auto_increment primary key not null,
 status	enum('OCCUPIED','AVAILABLE','RESERVED') not null DEFAULT 'AVAILABLE',
 parking_spot_id int,
 start_time	time,
 foreign key (parking_spot_id) references parking_spots(id) on delete cascade
  );

create table IF NOT EXISTS reports(
id int auto_increment primary key not null,
occupancy int,
violation	varchar(100),
revenue		int,
parking_spot_id	int,
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
