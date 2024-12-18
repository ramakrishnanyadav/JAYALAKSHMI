import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IceCreamShopGUI {
    // Ice cream emoji
    static String iceCream = "\uD83C\uDF66"; // Unicode for the ice cream emoji

    // Declare UI components
    private static JFrame frame;
    private static JTextField nameField, phoneField;
    private static JList<String> flavorList;
    private static JSpinner scoopSpinner;
    private static JComboBox<String> sprinkleComboBox;
    private static JTextArea billArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create frame
        frame = new JFrame("Ice Cream Shop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Create components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to the Ice Cream Shop! " + iceCream, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(welcomeLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Customer name input
        JLabel nameLabel = new JLabel("Enter your name:");
        panel.add(nameLabel);
        nameField = new JTextField();
        panel.add(nameField);

        // Phone number input
        JLabel phoneLabel = new JLabel("Enter your 10-digit phone number:");
        panel.add(phoneLabel);
        phoneField = new JTextField();
        panel.add(phoneField);

        // Ice cream flavor list
        JLabel flavorLabel = new JLabel("Select your flavors (hold Ctrl to select multiple):");
        panel.add(flavorLabel);

        List<String> flavors = new ArrayList<>();
        flavors.add("Vanilla");
        flavors.add("Chocolate");
        flavors.add("Strawberry");
        flavors.add("Mint");
        flavors.add("Cookies and Cream");
        flavors.add("Mango");
        flavors.add("Pistachio");
        flavors.add("Butterscotch");
        flavors.add("Coffee");
        flavors.add("Blackcurrant");

        flavorList = new JList<>(flavors.toArray(new String[0]));
        flavorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane flavorScrollPane = new JScrollPane(flavorList);
        panel.add(flavorScrollPane);

        // Number of scoops
        JLabel scoopLabel = new JLabel("Number of scoops:");
        panel.add(scoopLabel);
        scoopSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        panel.add(scoopSpinner);

        // Sprinkle options
        JLabel sprinkleLabel = new JLabel("Choose your sprinkle:");
        panel.add(sprinkleLabel);

        String[] sprinkleOptions = {
            "None", "Rainbow Sprinkles (RS. 10)", "Chocolate Sprinkles (RS. 15)", 
            "Caramel Crunch (RS. 20)", "Choco Chips (RS. 25)", "Nuts & Berries (RS. 30)"
        };
        sprinkleComboBox = new JComboBox<>(sprinkleOptions);
        panel.add(sprinkleComboBox);

        // Order summary and confirm
        JButton confirmButton = new JButton("Confirm Order");
        panel.add(confirmButton);

        // Bill area
        billArea = new JTextArea(10, 30);
        billArea.setEditable(false);
        JScrollPane billScrollPane = new JScrollPane(billArea);
        panel.add(billScrollPane);

        // Add panel to frame
        frame.add(panel, BorderLayout.CENTER);

        // Confirm button action
        confirmButton.addActionListener(e -> processOrder(flavors));

        frame.setVisible(true);
    }

    private static void processOrder(List<String> flavors) {
        String customerName = nameField.getText();
        String phoneNumber = phoneField.getText();

        if (customerName.isEmpty() || phoneNumber.isEmpty() || !phoneNumber.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(frame, "Please enter valid name and phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numScoops = (int) scoopSpinner.getValue();
        if (numScoops <= 0) {
            JOptionPane.showMessageDialog(frame, "Please select at least one scoop.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get selected flavors
        List<String> selectedFlavors = flavorList.getSelectedValuesList();
        if (selectedFlavors.size() != numScoops) {
            JOptionPane.showMessageDialog(frame, "Please select the correct number of scoops.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sprinkle cost
        int sprinkleChoice = sprinkleComboBox.getSelectedIndex();
        double sprinkleCost = 0.0;
        switch (sprinkleChoice) {
            case 1: sprinkleCost = 10.0; break;
            case 2: sprinkleCost = 15.0; break;
            case 3: sprinkleCost = 20.0; break;
            case 4: sprinkleCost = 25.0; break;
            case 5: sprinkleCost = 30.0; break;
        }

        // Flavor prices
        double flavorCost = 0.0;
        for (String flavor : selectedFlavors) {
            int flavorIndex = flavors.indexOf(flavor);
            switch (flavorIndex) {
                case 0: flavorCost += 60.0; break;
                case 1: flavorCost += 70.0; break;
                case 2: flavorCost += 65.0; break;
                case 3: flavorCost += 75.0; break;
                case 4: flavorCost += 80.0; break;
                case 5: flavorCost += 85.0; break;
                case 6: flavorCost += 90.0; break;
                case 7: flavorCost += 95.0; break;
                case 8: flavorCost += 100.0; break;
                case 9: flavorCost += 110.0; break;
            }
        }

        // Calculate total cost
        double totalCost = flavorCost + sprinkleCost;

        // Display bill summary
        billArea.setText("---- Bill Summary ----\n");
        billArea.append("Customer: " + customerName + "\n");
        billArea.append("Phone: " + phoneNumber + "\n");
        billArea.append("Flavors: " + String.join(", ", selectedFlavors) + "\n");
        billArea.append("Number of Scoops: " + numScoops + "\n");
        billArea.append("Sprinkles: " + (sprinkleChoice > 0 ? "Yes" : "No") + "\n");
        billArea.append("Total Cost: RS. " + totalCost + "\n");

        // Insert order into the database
        insertOrderIntoDatabase(customerName, phoneNumber, selectedFlavors, numScoops, sprinkleChoice, totalCost);

        // Thank you message
        JOptionPane.showMessageDialog(frame, "Thank you for your order! Enjoy your ice cream! " + iceCream);
    }

    private static void insertOrderIntoDatabase(String customerName, String phoneNumber, List<String> selectedFlavors, int numScoops, int sprinkleChoice, double totalCost) {
        try {
            // Connect to the database
            String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12752701";
            String username = "sql12752701";
            String password = "k69hhGhdvs";

            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "INSERT INTO ice_cream_orders (customer_name, phone_number, flavors, num_scoops, sprinkle_choice, total_cost) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, phoneNumber);
            preparedStatement.setString(3, String.join(", ", selectedFlavors));
            preparedStatement.setInt(4, numScoops);
            preparedStatement.setInt(5, sprinkleChoice);
            preparedStatement.setDouble(6, totalCost);

            // Execute the insert query
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nOrder has been successfully saved to the database.");
            }

            // Close the connection
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
}
