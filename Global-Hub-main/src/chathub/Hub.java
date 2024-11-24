package chathub; // Declaring the package for this class

import java.util.ArrayList; // Importing ArrayList from the Java Collections Framework
import java.io.*; // Importing classes for input and output operations, including serialization

// The Hub class represents a central component of the chat application, managing users and messages
public class Hub implements Serializable {
    // Static variable to define the filename for the database
    public static String databaseFile = "data.bin";

    // ArrayList to store User objects
    private ArrayList<User> users = new ArrayList<User>();
    
    // ArrayList to store Message objects
    private ArrayList<Message> messages = new ArrayList<Message>();

    // Variable to keep track of the currently authenticated user
    private User currentUser ;

    // Constructor for the Hub class
    public Hub() {
        // Load existing users and messages from the database file
        this.load();
        // Initialize currentUser  to null (no user is logged in initially)
        currentUser  = null;
    }

    // Getters for accessing private member variables
    public ArrayList<Message> get_messages() {
        return this.messages; // Return the list of messages
    }

    public ArrayList<User> get_users() {
        return this.users; // Return the list of users
    }

    public User get_currentUser () {
        return this.currentUser ; // Return the currently authenticated user
    }

    public void set_currentUser (User user) {
        this.currentUser  = user; // Set the currently authenticated user
    }

    // Private method to add a user to the users list
    private void add_user(User User) {
        this.users.add(User); // Add the user to the ArrayList
    }

    // Private method to add a message to the messages list
    private void add_message(Message msg) {
        this.messages.add(msg); // Add the message to the ArrayList
    }

    // Method to validate if a username already exists in the users list
    private void validate_username_exist(String username) {
        for (User  u : this.users) {
            // Check if the username matches an existing user
            if (u.get_username().equals(username)) {
                throw new IllegalArgumentException("The username already exists"); // Throw exception if it does
            }
        }
    }

    // Method to create a new user
    public void create_user(String username, String password, String name, String email, int age) {
        // Validate that the username does not already exist
        validate_username_exist(username);
        
        // Create a new User object with the provided details
        User u = new User(username, password, name, email, age);
        
        // Add the new user to the users list
        this.add_user(u);
        
        // Save the current state of the Hub to the database file
        this.save();
    }

    // Method to create a new message
    public void create_message(User user, String message) {
        // Create a new Message object with the user and message content
        Message msg = new Message(user, message);
        
        // Add the message to the messages list
        this.add_message(msg);
        
        // Save the current state of the Hub to the database file
        this.save();
    }

    // Method to authenticate a user based on username and password
    public User authenticate(String username, String password) {
        for (User  u : this.users) {
            // Check if the username and password match an existing user
            if (u.get_username().equals(username) && u.match_password(password)) {
                return u; // Return the authenticated user
            }
        }
        // Throw an exception if authentication fails
        throw new IllegalArgumentException("Invalid username or password");
    }

    // Method to save the current state of the Hub to a file
    public void save() {
        try {
            // Create a FileOutputStream to write to the database file
            FileOutputStream fos = new FileOutputStream(databaseFile, false);
            // Create an ObjectOutputStream to serialize the Hub object
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this); // Write the current Hub object to the file
            oos.close(); // Close the ObjectOutputStream
            fos.close(); // Close the FileOutputStream
            // System.out.println("Chats saved"); // Optional debug print statement
        } catch (IOException e) {
            // Print error message if saving fails
            System.out.println("Error saving chats");
            e.printStackTrace();
        }
    }

    // Method to load the state of the Hub from a file
    public void load() {
        try {
            // Create a FileInputStream to read from the database file
            FileInputStream fis = new FileInputStream(databaseFile);
            // Create an ObjectInputStream to deserialize the Hub object
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Read the Hub object from the file
            Hub hub = (Hub) ois.readObject();
            ois.close(); // Close the ObjectInputStream
            fis.close(); // Close the FileInputStream
            if (hub != null) {
                // If the hub object is not null, update the current instance's users and messages
                this.users = hub.get_users();
                this.messages = hub.get_messages();
            }
            // System.out.println("Chats loaded"); // Optional debug print statement
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions that may occur during loading
            // Create the file if it does not exist
            if (e instanceof FileNotFoundException) {
                try {
                    // Attempt to create a new file for the database
                    File file = new File(databaseFile);
                    file.createNewFile();
                    System.out.println("Database file created"); // Notify that the file was created
                } catch (IOException ioException) {
                    // Print error message if file creation fails
                    System.out.println("Error creating database file");
                    ioException.printStackTrace();
                }
            } else if (e instanceof EOFException) {
                // Handle the case where the database file is empty
                System.out.println("Database file is empty, no data to load");
            } else {
                // Print error message for other exceptions
                System.out.println("Error loading chats");
                e.printStackTrace();
            }
        }
    }

    // For testing purposes - method to print all usernames
    public void print_user() {
        for (User  u : this.users) {
            System.out.println(u.get_username()); // Print each user's username
        }
    }

    // For testing purposes - method to print all messages
    public void print_messages() {
        for (Message m : this.messages) {
            // Print the username of the message sender and the message content
            System.out.println(m.user.get_username() + " : " + m.message);
        }
    }
}