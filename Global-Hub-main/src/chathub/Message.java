package chathub; // Declaring the package for this class

import java.io.Serializable; // Importing Serializable interface for object serialization

// The Message class represents a message sent by a user in the chat application
public class Message implements Serializable {
    // Instance variable to hold the User object associated with the message
    public User user;
    
    // Instance variable to hold the content of the message
    public String message;

    // Constructor for the Message class
    public Message(User user, String message) {
        this.user = user; // Assign the provided User object to the instance variable
        this.message = message; // Assign the provided message content to the instance variable
    }
}