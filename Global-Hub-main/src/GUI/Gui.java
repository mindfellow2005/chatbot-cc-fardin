package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import chathub.*;

public class Gui{
    String state = "welcome";
    
    public String login_user(Hub hub) {
        state = "login";
        JDialog loginDialog = new JDialog((Frame) null, "Global Hub | Login", true);
        loginDialog.setSize(500,300);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginDialog.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        loginDialog.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginDialog.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginDialog.add(passwordField, gbc);

        
        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton,gbc);
        JButton loginButton = new JButton("Login");
        
        buttonPanel.add(loginButton);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginDialog.add(buttonPanel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login action
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                try{
                    User user = hub.authenticate(username, new String(password));
                    hub.set_currentUser(user);
                    if(user != null) {
                        state = "hub";
                        loginDialog.dispose();
                    }

                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(loginDialog, ex.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "welcome";
                loginDialog.setVisible(false);
            }
        });

        close_window_listener(loginDialog);

        loginDialog.setVisible(true);
        return state;
    }

    public String hub_view(Hub hub) {
        state = "hub";
        JDialog hubDialog = new JDialog((Frame) null, "Global Hub | Welcome " + hub.get_currentUser().get_username(), true);
        hubDialog.setSize(500,300);
        hubDialog.setLocationRelativeTo(null);

        // Initialize components
        JTextArea messageArea = new JTextArea();
        messageArea.setEditable(false);
        JTextField inputField = new JTextField(20);
        JButton sendButton = new JButton("Send");

        JButton logoutButton = new JButton("Logout");


    
        for (Message m : hub.get_messages()) {
            String username = m.user.get_username().equals(hub.get_currentUser().get_username()) ? "You " : m.user.get_username();
            messageArea.append(username + " : " + m.message + "\n");
            messageArea.append("\n");            
        }

        messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Scroll to the bottom
        messageArea.setLineWrap(true); //wraps message to next line if they exceed width of text area

        //a thread to continuously update the message area
        new Thread(() -> {
            int lastMessageCount = hub.get_messages().size();
            while (true) {
                hub.load();
                int currentMessageCount = hub.get_messages().size();
                
                if (currentMessageCount > lastMessageCount) {
                    for (int i = lastMessageCount; i < currentMessageCount; i++) {
                        Message m = hub.get_messages().get(i);
                        String username = m.user.get_username().equals(hub.get_currentUser().get_username()) ? "You " : m.user.get_username();
                        messageArea.append(username + " : " + m.message + "\n");
                        messageArea.append("\n");
                    }
                    lastMessageCount = currentMessageCount;
                    messageArea.setCaretPosition(messageArea.getDocument().getLength()); // Scroll to the bottom
                }

                try {
                    Thread.sleep(1000); // Update every second
                } 
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
        
        
        // Add action listener to the send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    try{
                        hub.create_message(hub.get_currentUser(), message);
                    }
                    catch (Exception ex){
                        JOptionPane.showMessageDialog(hubDialog, ex.getMessage(), "Message Failed", JOptionPane.ERROR_MESSAGE);
                    }
                    inputField.setText("");
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Perform logout actions here
                System.out.println("User logged out");
                hub.set_currentUser(null);
                state = "welcome";
                JOptionPane.showMessageDialog(hubDialog, "You have been logged out", "Logout", JOptionPane.INFORMATION_MESSAGE);
                hubDialog.dispose(); // Close the dialog
            }
        });

        
        // Layout components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
        
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(inputField);
        inputPanel.add(sendButton);
        
        panel.add(inputPanel, BorderLayout.SOUTH);

        inputPanel.add(logoutButton); // Add the logout button to the input panel

        close_window_listener(hubDialog);

        hubDialog.add(panel);
        hubDialog.setVisible(true);
        return state;
        
    }
    

    public String register_view(Hub hub) {
        JDialog registerDialog = new JDialog((Frame) null, "Global Hub | Register", true);
        registerDialog.setSize(500,300);
        registerDialog.setLocationRelativeTo(null);
        registerDialog.setLayout(new GridBagLayout());
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerDialog.add(usernameLabel, gbc);
    
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        registerDialog.add(usernameField, gbc);
    
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerDialog.add(passwordLabel, gbc);
    
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        registerDialog.add(passwordField, gbc);
    
        JLabel fullNameLabel = new JLabel("Full Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerDialog.add(fullNameLabel, gbc);
    
        JTextField fullNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        registerDialog.add(fullNameField, gbc);
    
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerDialog.add(emailLabel, gbc);
    
        JTextField emailField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        registerDialog.add(emailField, gbc);
    
        JLabel ageLabel = new JLabel("Age:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerDialog.add(ageLabel, gbc);
    
        JTextField ageField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        registerDialog.add(ageField, gbc);

        JPanel buttonPanel = new JPanel();


        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");
        
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 1;
        gbc.gridy = 5;
        registerDialog.add(buttonPanel, gbc);

        

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            state = "welcome";
            registerDialog.setVisible(false);
            }
        });
    
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();
                String fullName = fullNameField.getText();
                String email = emailField.getText();
                String age = ageField.getText();
    
                try {
                    hub.create_user(username, new String(password), fullName, email, Integer.parseInt(age));
                    JOptionPane.showMessageDialog(registerDialog, "Successfully Registered. Please Login.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.setVisible(false);
                    state = "login";
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(registerDialog, ex.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        close_window_listener(registerDialog);
    
        registerDialog.setVisible(true);
        return state;
    }

    public String welcome_view(Hub hub) {
        JDialog welcomeDialog = new JDialog((Frame) null, "Global Chat | Welcome", true);
        welcomeDialog.setSize(500,300);
        welcomeDialog.setLocationRelativeTo(null);
        welcomeDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel welcomeLabel = new JLabel("Welcome to GlobalHub");
        gbc.gridx = 0;
        gbc.gridy = 0;
        welcomeDialog.add(welcomeLabel,gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0;
        gbc.gridy = 1;

        welcomeDialog.add(buttonPanel,gbc);
        
        
        JLabel userCountLabel = new JLabel("Total Users: " + hub.get_users().size());
        gbc.gridx = 0;
        gbc.gridy = 2;
        welcomeDialog.add(userCountLabel,gbc);
        
        
        JButton seeUserListButton = new JButton("User List");
        gbc.gridx = 0;
        gbc.gridy = 3;
        welcomeDialog.add(seeUserListButton,gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "login";
                welcomeDialog.setVisible(false);
                
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "register";
                welcomeDialog.setVisible(false);
            }
        });

        seeUserListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                state = "userlist";
                welcomeDialog.setVisible(false);
            }
        });
        
        close_window_listener(welcomeDialog);
        welcomeDialog.setVisible(true);
        return state;
    }

    public void userlist_view(Hub hub) {
        JDialog userListDialog = new JDialog((Frame) null, "User List", true);
        userListDialog.setSize(500, 300);
        userListDialog.setLocationRelativeTo(null);
    
        String[] columnNames = {"Username", "Full Name", "Email", "Age"};
        ArrayList<User> users = hub.get_users();
        String[][] data = new String[users.size()][4];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.get_username();
            data[i][1] = user.get_name();
            data[i][2] = user.get_email();
            data[i][3] = String.valueOf(user.get_age());
        }
    
        JTable userTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(userTable);
        userListDialog.add(scrollPane, BorderLayout.CENTER);
    
        JButton closeButton = new JButton("Close");
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(closeButton);
    
        userListDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userListDialog.dispose();
            }
        });
    
        close_window_listener(userListDialog);
    
        userListDialog.setVisible(true);
    }
        

    private void close_window_listener(JDialog jdiag){
        jdiag.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("closed the window");
                System.exit(0);
            }
        });
    }
}