# Airbnb Booking System – Java + MySQL Project

This is a simple console-based Airbnb clone developed using Java and MySQL. It allows users to register, login, book properties, and manage property listings depending on their role (User, Host, Admin).

---

## ✨ Features

- **User Registration and Login**  
  Secure login and signup system for users, hosts, and admins.

- **Property Listing & Booking**  
  - Users can view available Airbnb listings and book them.
  - Hosts can add and manage their properties.
  - Admins can oversee all listings and bookings.

- **Role-Based Access**  
  - `Admin`: View/manage users and listings.  
  - `Host`: Add/update Airbnb listings.  
  - `User`: Book available properties.

- **MySQL Integration**  
  All data is stored and retrieved from a MySQL database.

---

## 🛠️ Technologies Used

- Java (Core Java, OOP concepts)
- MySQL (Relational database)
- JDBC (Java Database Connectivity)
- Console-based UI

---

## 🗃️ Project Structure

├── AirBnB.java # Main entry point of the application
├── Login_SignUp.java # Handles user login and signup
├── Admin.java # Admin functionalities
├── Host.java # Host functionalities
├── User.java # User functionalities
├── Booking.java # Booking operations
├── Airbnbdetails.java # Airbnb property data model


---

## 🧰 Setup Instructions

### ✅ Prerequisites
- Java 8 or above
- MySQL Server
- MySQL JDBC Driver (make sure it's added to your classpath)

---

### 🔧 Database Setup

1. Open MySQL and run:
     CREATE DATABASE airbnb;
2.Create required tables (users, listings, bookings, etc.) using the SQL schema you’ve created (not included here, let me know if you want help writing it).

3. Update DB credentials in your Java files if needed:
   String url = "jdbc:mysql://localhost:3306/airbnb";
   String username = "root";
   String password = "your_mysql_password";

🚀 Running the App
Compile the project:

  bash
  Copy
  Edit
  javac *.java

Run the main program:

bash
Copy
Edit
java AirBnB


🧪 Sample Workflow
Launch program

Choose to Login or Sign Up

Depending on your role:

Admin → Manage users and properties

Host → Add/Edit listings

User → View and book listings

📌 Notes
Ensure MySQL server is running before launching the application.

Console UI is used; no GUI.

This is a basic educational project and doesn't include advanced features like search filters, payments, or UI.

📜 License
This project is for educational purposes only.
