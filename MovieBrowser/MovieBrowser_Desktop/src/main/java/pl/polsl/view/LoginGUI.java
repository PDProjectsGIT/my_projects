/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * The LoginGUI class represents a graphical user interface (GUI) for user authentication.
 * It extends the base GUI class and provides components for entering user credentials.
 * Users can log in or navigate to the sign-up screen through this interface.
 *
 * @author Pawel Drzazga
 * @version 3.0
 */
public class LoginGUI extends GUI{
    
    /**
     * Represents the login button in the LoginGUI.
     */
    final private JButton loginButton;
    
    /**
     * Represents the sign-up button in the LoginGUI.
     */
    final private JButton signupButton;
    
    /**
     * Represents the text field for entering the user's login.
     */
    final private JTextField userTextField;
    
    /**
     * Represents the password field for entering the user's password.
     */
    final private JPasswordField passwordTextField;

    /**
     * Constructs a new LoginGUI instance. Initializes and arranges the graphical components.
     */         
public LoginGUI() {
    super(new GridLayout(8, 1, 0, 10));
    setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Dodanie odstępu dla całego panelu
    
    JLabel titleLabel = textLabel("Movie Browser", 20, SwingConstants.CENTER, SwingConstants.CENTER, Font.BOLD);
    JLabel userLabel = textLabel("User", 12, SwingConstants.LEFT, SwingConstants.CENTER, Font.BOLD);
    JLabel passwordLabel = textLabel("Password", 12, SwingConstants.LEFT, SwingConstants.CENTER, Font.BOLD);
    JLabel accountLabel = textLabel("Don't have an account?", 12, SwingConstants.CENTER, SwingConstants.CENTER, Font.PLAIN);
    
    userTextField = new JTextField(20);
    userTextField.setToolTipText("Enter your login.");
    passwordTextField = new JPasswordField(20);
    passwordTextField.setToolTipText("Enter your password.");

    loginButton = new JButton("Log in");
    loginButton.setToolTipText("Click to login or Alt + L.");
    loginButton.setMnemonic(KeyEvent.VK_L);

    signupButton = new JButton("Sign in");
    signupButton.setToolTipText("Click to signin or Alt + S.");
    signupButton.setMnemonic(KeyEvent.VK_S);

    add(titleLabel);
    add(userLabel);
    add(userTextField);
    add(passwordLabel);
    add(passwordTextField);
    add(loginButton);
    add(accountLabel);
    add(signupButton);

    showGUI();
}
    
    /**
     * Retrieves the entered login from the user interface.
     *
     * @return The entered login.
     */
    public String getLogin(){
        return userTextField.getText();
    }
    
    /**
     * Retrieves the entered password from the user interface.
     *
     * @return The entered password.
     */
    public char[] getPassword(){
        return passwordTextField.getPassword();
    }
    
    /**
     * Adds an ActionListener to the login button, allowing the registration
     * of an object to be notified when the login button is clicked.
     *
     * @param listener The ActionListener to be added to the login button.
     */
    public void addLoginButtonListener(ActionListener listener){
        loginButton.addActionListener(listener);
    }
    
    /**
     * Adds an ActionListener to the signup button, allowing the registration
     * of an object to be notified when the signup button is clicked.
     *
     * @param listener The ActionListener to be added to the signup button.
     */
    public void addSignupButtonListener(ActionListener listener){
        signupButton.addActionListener(listener);
    }
    
}
