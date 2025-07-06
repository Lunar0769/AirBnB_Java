CREATE DATABASE IF NOT EXISTS airbnb;
USE airbnb;

-- USERS table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role ENUM('user', 'host', 'admin') NOT NULL
);

-- PROPERTIES table (Airbnb listings)
CREATE TABLE listings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    host_id INT,
    title VARCHAR(100),
    description TEXT,
    location VARCHAR(100),
    price_per_night DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (host_id) REFERENCES users(id)
);

-- BOOKINGS table
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    listing_id INT,
    start_date DATE,
    end_date DATE,
    total_cost DECIMAL(10, 2),
    booking_status ENUM('booked', 'cancelled', 'completed') DEFAULT 'booked',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (listing_id) REFERENCES listings(id)
);
