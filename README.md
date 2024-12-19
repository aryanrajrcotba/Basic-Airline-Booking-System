# Basic Airline Booking System

## Overview
The Airline Booking System is a Java Swing application integrated with a MySQL database. It allows users to manage flights, book tickets, and view bookings. The system provides an interactive user interface for ease of use and ensures secure data handling through database integration.

## Features
1. **Flight Management**:
   - Add flights with details such as name, departure, arrival, and capacity.
   - View all flights in the database.

2. **Passenger Booking**:
   - Enter passenger details (name, age, contact).
   - Select a flight and automatically assign a seat.

3. **View Bookings**:
   - View all bookings, including passenger and flight details.

4. **Interactive UI**:
   - User-friendly navigation using Java Swing.
   - Seamless switching between panels for different features.

## Prerequisites
1. **Java Development Kit (JDK)**: Version 8 or above.
2. **MySQL Database**: Ensure MySQL server is installed and running.
3. **JDBC Driver**: Add the MySQL JDBC driver to your project classpath.
4. **IDE**: Any IDE supporting Java Swing (e.g., IntelliJ IDEA, Eclipse, NetBeans).

## Database Setup
### Steps to Create the Database:
1. Open your MySQL client or SQL Workbench.
2. Execute the following SQL commands given in DataBase Folder

## How to Run the Application
1. Clone this repository or download the source code.
2. Open the project in your IDE.
3. Update the database connection details in the `AirlineBookingSystem` class:
   - **DB_URL**: `jdbc:mysql://localhost:3306/airline_db`
   - **DB_USER**: Your MySQL username (default: `root`).
   - **DB_PASSWORD**: Your MySQL password.
4. Compile and run the `AirlineBookingSystem` class.

## Usage
- Navigate through the application using the buttons for "Manage Flights," "Book Ticket," and "View Bookings."
- Add flight details before booking tickets.
- Use the "View Bookings" option to verify all bookings.

## Troubleshooting
1. **Database Connection Issues**:
   - Ensure the MySQL server is running.
   - Verify the database credentials.

2. **Missing Tables**:
   - Re-run the SQL commands to create the tables.

3. **JDBC Driver Error**:
   - Download and add the MySQL JDBC driver to your project classpath.



