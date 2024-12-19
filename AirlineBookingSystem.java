import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AirlineBookingSystem {

    // Database connection variables
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airline_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Patna@1234";

    public static void main(String[] args) {
        new AirlineBookingSystem().createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Airline Booking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Panel for navigation
        JPanel navPanel = new JPanel();
        JButton manageFlightsButton = new JButton("Manage Flights");
        JButton bookTicketButton = new JButton("Book Ticket");
        JButton viewBookingsButton = new JButton("View Bookings");

        navPanel.add(manageFlightsButton);
        navPanel.add(bookTicketButton);
        navPanel.add(viewBookingsButton);

        // Card layout for dynamic content
        JPanel contentPanel = new JPanel(new CardLayout());
        JPanel flightPanel = new JPanel();
        JPanel bookingPanel = new JPanel();
        JPanel viewBookingPanel = new JPanel();

        contentPanel.add(flightPanel, "Manage Flights");
        contentPanel.add(bookingPanel, "Book Ticket");
        contentPanel.add(viewBookingPanel, "View Bookings");

        // Event listeners for buttons
        manageFlightsButton.addActionListener(e -> switchPanel(contentPanel, "Manage Flights"));
        bookTicketButton.addActionListener(e -> switchPanel(contentPanel, "Book Ticket"));
        viewBookingsButton.addActionListener(e -> switchPanel(contentPanel, "View Bookings"));

        // Flight Management Panel
        setupFlightManagementPanel(flightPanel);

        // Booking Panel
        setupBookingPanel(bookingPanel);

        // View Bookings Panel
        setupViewBookingsPanel(viewBookingPanel);

        frame.setLayout(new BorderLayout());
        frame.add(navPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void switchPanel(JPanel contentPanel, String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }

    private void setupFlightManagementPanel(JPanel flightPanel) {
        flightPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Manage Flights", SwingConstants.CENTER);
        flightPanel.add(label, BorderLayout.NORTH);

        JButton addFlightButton = new JButton("Add Flight");
        JButton viewFlightsButton = new JButton("View Flights");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addFlightButton);
        buttonPanel.add(viewFlightsButton);

        flightPanel.add(buttonPanel, BorderLayout.CENTER);

        addFlightButton.addActionListener(e -> addFlight());
        viewFlightsButton.addActionListener(e -> viewFlights());
    }

    private void addFlight() {
        String flightName = JOptionPane.showInputDialog("Enter Flight Name:");
        String departure = JOptionPane.showInputDialog("Enter Departure City:");
        String arrival = JOptionPane.showInputDialog("Enter Arrival City:");
        String capacityStr = JOptionPane.showInputDialog("Enter Capacity:");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO flights (flight_name, departure, arrival, capacity) VALUES (?, ?, ?, ?)");) {
            stmt.setString(1, flightName);
            stmt.setString(2, departure);
            stmt.setString(3, arrival);
            stmt.setInt(4, Integer.parseInt(capacityStr));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Flight added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding flight.");
        }
    }

    private void viewFlights() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flights");) {

            StringBuilder flightDetails = new StringBuilder("Flight Details:\n\n");
            while (rs.next()) {
                flightDetails.append("Flight ID: ").append(rs.getInt("flight_id"))
                        .append(", Name: ").append(rs.getString("flight_name"))
                        .append(", Departure: ").append(rs.getString("departure"))
                        .append(", Arrival: ").append(rs.getString("arrival"))
                        .append(", Capacity: ").append(rs.getInt("capacity"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, flightDetails.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching flight details.");
        }
    }

    private void setupBookingPanel(JPanel bookingPanel) {
        bookingPanel.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Passenger Name:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        JLabel contactLabel = new JLabel("Contact:");
        JTextField contactField = new JTextField();
        JLabel flightLabel = new JLabel("Flight ID:");
        JTextField flightField = new JTextField();

        JButton bookButton = new JButton("Book Ticket");

        bookingPanel.add(nameLabel);
        bookingPanel.add(nameField);
        bookingPanel.add(ageLabel);
        bookingPanel.add(ageField);
        bookingPanel.add(contactLabel);
        bookingPanel.add(contactField);
        bookingPanel.add(flightLabel);
        bookingPanel.add(flightField);
        bookingPanel.add(new JLabel());
        bookingPanel.add(bookButton);

        bookButton.addActionListener(e -> bookTicket(nameField.getText(), ageField.getText(), contactField.getText(), flightField.getText()));
    }

    private void bookTicket(String name, String age, String contact, String flightId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO passengers (name, age, contact) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);) {

            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, contact);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int passengerId = generatedKeys.getInt(1);

                try (PreparedStatement bookingStmt = conn.prepareStatement("INSERT INTO bookings (flight_id, passenger_id, seat_number, booking_date) VALUES (?, ?, ?, NOW())")) {
                    bookingStmt.setInt(1, Integer.parseInt(flightId));
                    bookingStmt.setInt(2, passengerId);
                    bookingStmt.setString(3, "Seat " + (int) (Math.random() * 100 + 1));
                    bookingStmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Ticket booked successfully!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error booking ticket.");
        }
    }

    private void setupViewBookingsPanel(JPanel viewBookingPanel) {
        viewBookingPanel.setLayout(new BorderLayout());

        JButton viewAllBookingsButton = new JButton("View All Bookings");
        viewBookingPanel.add(viewAllBookingsButton, BorderLayout.CENTER);

        viewAllBookingsButton.addActionListener(e -> viewBookings());
    }

    private void viewBookings() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT b.booking_id, p.name, f.flight_name, b.seat_number, b.booking_date FROM bookings b JOIN passengers p ON b.passenger_id = p.passenger_id JOIN flights f ON b.flight_id = f.flight_id");) {

            StringBuilder bookingDetails = new StringBuilder("Booking Details:\n\n");
            while (rs.next()) {
                bookingDetails.append("Booking ID: ").append(rs.getInt("booking_id"))
                        .append(", Passenger: ").append(rs.getString("name"))
                        .append(", Flight: ").append(rs.getString("flight_name"))
                        .append(", Seat: ").append(rs.getString("seat_number"))
                        .append(", Date: ").append(rs.getDate("booking_date"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, bookingDetails.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching booking details.");
        }
    }
}
