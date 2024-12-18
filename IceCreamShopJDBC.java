import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class IceCreamShopJDBC {

    // Ice cream emoji
    static String iceCream = "\uD83C\uDF66"; // Unicode for the ice cream emoji

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Greet the customer with the ice cream emoji
        System.out.println("Welcome to the Ice Cream Shop! " + iceCream);
        System.out.print("Please enter your name: ");
        String customerName = scanner.nextLine();

        // Validate phone number to ensure it has exactly 10 digits
        String phoneNumber = "";
        while (true) {
            System.out.print("Please enter your 10-digit phone number: ");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number! Please enter exactly 10 digits.");
            }
        }

        // Ice cream flavors and prices stored in Lists
        List<String> flavors = new ArrayList<>();
        List<Double> prices = new ArrayList<>();

        // Add flavors and prices to the List
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

        prices.add(60.0);
        prices.add(70.0);
        prices.add(65.0);
        prices.add(75.0);
        prices.add(80.0);
        prices.add(85.0);
        prices.add(90.0);
        prices.add(95.0);
        prices.add(100.0);
        prices.add(110.0);

        System.out.println("\nIce Cream Flavors with Prices (RS.):");
        for (int i = 0; i < flavors.size(); i++) {
            System.out.println((i + 1) + ". " + flavors.get(i) + " - RS. " + prices.get(i));
        }

        // Ask for number of scoops (no limit)
        System.out.print("\nHow many scoops would you like? ");
        int numScoops = scanner.nextInt();

        // List to store selected flavors
        List<String> selectedFlavors = new ArrayList<>();
        double flavorCost = 0.0;

        for (int i = 0; i < numScoops; i++) {
            System.out.print("Select scoop " + (i + 1) + " (choose a number between 1 and 10): ");
            int flavorChoice = scanner.nextInt();

            if (flavorChoice < 1 || flavorChoice > 10) {
                System.out.println("Invalid choice! Exiting program.");
                return;
            }

            selectedFlavors.add(flavors.get(flavorChoice - 1));
            flavorCost += prices.get(flavorChoice - 1);
        }

        // If more than one scoop, ask if the customer wants to confirm or change the order
        if (numScoops > 1) {
            scanner.nextLine();  // Consume newline character after nextInt()
            System.out.println("\nYou have selected " + numScoops + " scoops.");
            System.out.print("Do you want to confirm your order? (Yes/No): ");
            String confirmation = scanner.nextLine().toLowerCase();

            if (confirmation.equals("no")) {
                System.out.println("You can change your order.");
                System.out.print("How many scoops would you like? ");
                numScoops = scanner.nextInt();

                // Re-select flavors
                selectedFlavors.clear();  // Clear the previous selection
                flavorCost = 0.0;

                for (int i = 0; i < numScoops; i++) {
                    System.out.print("Select scoop " + (i + 1) + " (choose a number between 1 and 10): ");
                    int flavorChoice = scanner.nextInt();

                    if (flavorChoice < 1 || flavorChoice > 10) {
                        System.out.println("Invalid choice! Exiting program.");
                        return;
                    }

                    selectedFlavors.add(flavors.get(flavorChoice - 1));
                    flavorCost += prices.get(flavorChoice - 1);
                }
            }
        }

        // Ask for sprinkling options
        System.out.println("\nChoose your sprinkle option:");
        System.out.println("1. Rainbow Sprinkles (RS. 10)");
        System.out.println("2. Chocolate Sprinkles (RS. 15)");
        System.out.println("3. Caramel Crunch (RS. 20)");
        System.out.println("4. Choco Chips (RS. 25)");
        System.out.println("5. Nuts & Berries (RS. 30)");
        System.out.print("Please select a sprinkle option (1-5): ");
        int sprinkleChoice = scanner.nextInt();
        double sprinkleCost = 0.0;

        // Set the sprinkle cost based on user's selection
        switch (sprinkleChoice) {
            case 1:
                sprinkleCost = 10.0;
                break;
            case 2:
                sprinkleCost = 15.0;
                break;
            case 3:
                sprinkleCost = 20.0;
                break;
            case 4:
                sprinkleCost = 25.0;
                break;
            case 5:
                sprinkleCost = 30.0;
                break;
            default:
                System.out.println("Invalid choice! No sprinkles added.");
        }

        // Ask for order confirmation or cancellation
        System.out.println("\nPlease confirm your order:");
        System.out.println("Name: " + customerName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Flavors: ");
        for (String flavor : selectedFlavors) {
            System.out.print(flavor + " ");
        }
        System.out.println("\nNumber of Scoops: " + numScoops);
        System.out.println("Sprinkles: " + (sprinkleChoice >= 1 && sprinkleChoice <= 5 ? "Yes" : "No"));
        
        System.out.print("\nDo you want to confirm your order? (Yes/No): ");
        scanner.nextLine();  // Consume newline character
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("no")) {
            System.out.println("Your order has been canceled.");
            return;
        }

        // Calculate total cost
        double totalCost = flavorCost + sprinkleCost;

        // Insert order details into the database
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

        // Display bill summary
        System.out.println("\n---- Bill Summary ----");
        System.out.println("Customer: " + customerName);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Flavors: ");
        for (String flavor : selectedFlavors) {
            System.out.print(flavor + " ");
        }
        System.out.println("\nNumber of Scoops: " + numScoops);
        System.out.println("Sprinkles: " + (sprinkleChoice >= 1 && sprinkleChoice <= 5 ? "Yes" : "No"));
        System.out.println("Total Cost: RS. " + totalCost);

        System.out.println("\nThank you for your order! Enjoy your ice cream! " + iceCream);

        // Close the scanner to avoid resource leaks
        scanner.close();
    }
}
