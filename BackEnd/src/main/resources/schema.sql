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
    FOREIGN KEY (parking_lot_manager) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parking_spots (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    type VARCHAR(100),
    price INT DEFAULT 0,
    parking_lot_id INT,
    status ENUM('OCCUPIED', 'AVAILABLE', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    FOREIGN KEY (parking_lot_id) REFERENCES parking_lots(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS time_slots (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    status ENUM('OCCUPIED', 'AVAILABLE', 'RESERVED') NOT NULL DEFAULT 'AVAILABLE',
    parking_spot_id INT,
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spots(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reports (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    occupancy INT,
    violation VARCHAR(100),
    revenue INT,
    parking_spot_id INT,
    FOREIGN KEY (parking_spot_id) REFERENCES parking_spots(id) ON DELETE CASCADE
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
