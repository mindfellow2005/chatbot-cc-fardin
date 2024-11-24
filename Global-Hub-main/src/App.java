// Importing necessary classes from chathub and GUI packages
import chathub.*;
import GUI.*;

public class App {
    // Declaring a static variable 'state' to keep track of the current state of the application
    public static String state = "welcome";

    // Main method: the entry point of the application
    public static void main(String[] args) {
        // Creating an instance of Hub, which likely manages chat functionalities
        Hub GlobalHub = new Hub();
        
        // Creating an instance of Gui, which likely manages the user interface
        Gui gui = new Gui();

        // Infinite loop to keep the application running and processing user interactions
        while(true) {
            // Using a switch statement to handle different application states
            switch (state) {
                case "welcome":
                    // If the state is "welcome", display the welcome view and update the state based on user input
                    state = gui.welcome_view(GlobalHub);
                    break;

                case "login":
                    // If the state is "login", display the login view and update the state based on user input
                    state = gui.login_user(GlobalHub);
                    break;
                
                case "hub":
                    // If the state is "hub", display the main hub view and update the state based on user input
                    state = gui.hub_view(GlobalHub);
                    break;

                case "register":
                    // If the state is "register", display the registration view and update the state based on user input
                    state = gui.register_view(GlobalHub);
                    break;

                case "userlist":
                    // If the state is "userlist", display the user list view
                    gui.userlist_view(GlobalHub);
                    // After displaying the user list, reset the state back to "welcome"
                    state = "welcome";
                    break;
            
                default:
                    // Default case to handle any unexpected states; currently does nothing
                    break;
            }
        }
    }
}