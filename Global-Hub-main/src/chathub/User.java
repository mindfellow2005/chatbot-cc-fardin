package chathub; // Declaring the package for this class

import java.io.Serializable; // Importing Serializable interface for object serialization

// The User class represents a user in the chat application
public class User implements Serializable {
    // Private instance variables to hold user information
    private String username; // The user's username
    private String password; // The user's password
    private String email; // The user's email address
    private String name; // The user's full name
    private int age; // The user's age

    // Constructor for creating a User with just username and password
    public User(String username, String password) {
        this.username = username; // Assign the username
        this.password = password; // Assign the password
    }

    // Constructor for creating a User with all details
    public User(String username, String password, String name, String email, int age) {
        // Validate the user's input for various fields
        validate_username(username); // Validate the username
        validate_email(email); // Validate the email
        validate_name(name); // Validate the name
        validate_age(age); // Validate the age

        // Assign the validated values to the instance variables
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    // Getters for accessing private member variables
    public String get_username() {
        return this.username; // Return the username
    }

    public String get_email() {
        return this.email; // Return the email address
    }

    public String get_name() {
        return this.name; // Return the user's name
    }

    public int get_age() {
        return this.age; // Return the user's age
    }

    // Method to check if a provided password matches the user's password
    public boolean match_password(String password) {
        return this.password.equals(password); // Return true if the passwords match
    }

    // Private method to validate the username
    private void validate_username(String username) {
        // Check if the username is at least 5 characters long
        if (username.length() < 5) {
            throw new IllegalArgumentException("Username must be at least 5 characters long");
        }
        // Check if the username contains any spaces
        if (username.contains(" ")) {
            throw new IllegalArgumentException("Username cannot contain spaces");
        }
        // Check if the username starts with a number
        if (Character.isDigit(username.charAt(0))) {
            throw new IllegalArgumentException("Username cannot start with a number");
        }
    }

    // Private method to validate the email address
    private void validate_email(String email) {
        // Check if the email contains an '@' symbol
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        // Check if the email contains any spaces
        if (email.contains(" ")) {
            throw new IllegalArgumentException("Email address cannot contain spaces");
        }
    }

    // Private method to validate the user's name
    private void validate_name(String name) {
        // Iterate through each character in the name
        for (char c : name.toCharArray()) {
            // Check if the character is not a letter or a space
            if (!Character.isLetter(c) && c != ' ') {
                throw new IllegalArgumentException("Name can only contain alphabets");
            }
        }
    }

    // Private method to validate the user's age
    private void validate_age(int age) {
        // Check if the age is less than or equal to zero
        if (age <= 0) {
            throw new IllegalArgumentException("Age cannot be negative or zero");
        }
        // Check if the age is less than 18
        if (age < 18) {
            throw new IllegalArgumentException("You must be 18 years or older to use this app");
        }
    }
}