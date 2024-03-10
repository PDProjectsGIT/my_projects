/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import pl.polsl.model.TableModel;
import pl.polsl.model.User;

/**
 * UserSettingsPanel class represents the panel for user settings and rented movies in the Movie Browser application.
 * It includes options for changing the password, adding funds, upgrading the account, and deleting the account.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class UserSettingsPanel extends GUI {
    
    /**
     * The table model of rented movies.
     */
    final private TableModel moviesRentedTableModel;
    
    /**
     * The user info panel.
     */
    final private UserInfoPanel userInfoPanel;
    
    /**
     * The change password field.
     */
    final private JTextField changePasswordField;
    
    /**
     * The add founds field.
     */
    final private JTextField addFoundsField;
    
    /**
     * The change password button.
     */
    final private JButton changePasswordButton;
    
    /**
     * The add founds button.
     */
    final private JButton addFoundsButton;
    
    /**
     * The upgrade rank button.
     */
    final private JButton upgradeButton;
    
    /**
     * The delete account button.
     */
    final private JButton deleteAccoundButton;
    
    /**
     * The end rental button.
     */
    final private JButton endRentalButton;
    
    /**
     * Constructor for UserSettingsPanel.
     */
    UserSettingsPanel(){
        super(new GridBagLayout());
        
        JLabel userInfo = textLabel("User Info:", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel userSettings = textLabel("User Settings: ", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel rentedMovies = textLabel("Rented Movies", 18, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        
        //User Info Panel
        userInfoPanel = new UserInfoPanel(new Dimension(400, 170));
        
        //User changePanle
        JPanel userChangePanel = new JPanel(new GridLayout(4,3,10,10));
        userChangePanel.setBorder(compoundBorder);
        userChangePanel.setPreferredSize(new Dimension(400, 170)); //TO SET
        
        JLabel changePassword = textLabel("Change password: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        changePasswordField = new JTextField(15);
        changePasswordField.setToolTipText("Type your new password.");
        
        changePasswordButton = new JButton("Change");
        changePasswordButton.setToolTipText("Click to change password or Alt + P.");
        changePasswordButton.setMnemonic(KeyEvent.VK_P);
        
        JLabel addFounds = textLabel("Add founds: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        addFoundsField = new JTextField(15);
        addFoundsField.setToolTipText("Type founds to add.");
        addFoundsButton = new JButton("Add");
        addFoundsButton.setToolTipText("Click to add founds or Alt + F.");
        addFoundsButton.setMnemonic(KeyEvent.VK_F);
        
        JLabel upgradeField = textLabel("Upgrade account: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        upgradeButton = new JButton("$100");
        upgradeButton.setToolTipText("Click upgrade to premium or Alt + U.");
        upgradeButton.setMnemonic(KeyEvent.VK_U);
        JLabel emptyField = new JLabel("");
        
        JLabel deleteAccountField = textLabel("Delete account: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        deleteAccoundButton = new JButton("Delete");
        deleteAccoundButton.setToolTipText("Click to delete your account or Alt + D.");
        deleteAccoundButton.setMnemonic(KeyEvent.VK_D);
        
        userChangePanel.add(changePassword);
        userChangePanel.add(changePasswordField);
        userChangePanel.add(changePasswordButton);
        userChangePanel.add(addFounds);
        userChangePanel.add(addFoundsField);
        userChangePanel.add(addFoundsButton);
        userChangePanel.add(upgradeField);
        userChangePanel.add(upgradeButton);
        userChangePanel.add(emptyField);
        userChangePanel.add(deleteAccountField);
        userChangePanel.add(deleteAccoundButton);
        
        //user rented movies panel
        
        String[] columnNames = {
            "Tile", 
            "Rent date",
            "Expire date",
            "End"
        };
        moviesRentedTableModel = new TableModel();
        moviesRentedTableModel.setColumnNames(columnNames);
        JTable movieTable = new JTable(moviesRentedTableModel);
        JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
        movieTableScrollPane.setPreferredSize(new Dimension(815, 300));
        
        endRentalButton = new JButton("End rental");
        endRentalButton.setToolTipText("Click to end rental of selected movies or Alt + E.");
        endRentalButton.setMnemonic(KeyEvent.VK_E);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,5,5,10);
        gbc.anchor = GridBagConstraints.WEST;
        
        this.add(userInfo, gbc);
        gbc.gridx = 1;
        this.add(userSettings, gbc);
        gbc.gridy = 1;
        gbc.gridx = 0;
        this.add(userInfoPanel, gbc);
        gbc.gridx = 1;
        this.add(userChangePanel, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(rentedMovies , gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 3;
        this.add(movieTableScrollPane , gbc);
        gbc.gridy = 4;
        this.add(endRentalButton, gbc);
    }
    
    /**
     * Updates the user settings panel with the provided user data.
     *
     * @param user The user whose information will be displayed.
     */
    void updateMovieSettingsPanel(User user){
        userInfoPanel.updateUserInfoPanel(user);
    }
    
    /**
     * Gets the table model for rented movies.
     *
     * @return The table model for rented movies.
     */
    TableModel getRentedMoviesTable(){
        return moviesRentedTableModel;
    }
    
    /**
     * Gets the list of selected movies to rent.
     *
     * @return The list of selected movies to rent.
     */
    ArrayList<Object[]> getSelectedMoviesToRent(){
        return moviesRentedTableModel.getSelectedRows();
    }
    
    /**
     * Gets the typed new password.
     *
     * @return The typed new password.
     */
    String getTypedNewPassword(){
        return changePasswordField.getText();
    }
    
    /**
     * Gets the typed balance to add.
     *
     * @return The typed balance to add.
     */
    String getTypedBalance(){
        return addFoundsField.getText();
    }
    
    /**
     * Adds a listener for the end rental button.
     *
     * @param listener The listener to add.
     */
    void addButtonEndRentalMovieListener(ActionListener listener){
        endRentalButton.addActionListener(listener);
    }
    
    /**
     * Adds a listener for the change password button.
     *
     * @param listener The listener to add.
     */
    void addButtonChangePasswordListener(ActionListener listener){
        changePasswordButton.addActionListener(listener);
    }
    
    /**
     * Adds a listener for the add funds button.
     *
     * @param listener The listener to add.
     */
    void addButtonAddFoundsListener(ActionListener listener){
        addFoundsButton.addActionListener(listener);
    }
    
    /**
     * Adds a listener for the upgrade button.
     *
     * @param listener The listener to add.
     */
    void addButtonUpgradeMovieListener(ActionListener listener){
        upgradeButton.addActionListener(listener);
    }
    
    /**
     * Adds a listener for the delete account button.
     *
     * @param listener The listener to add.
     */
    void addButtonDeleteAccountListener(ActionListener listener){
        deleteAccoundButton.addActionListener(listener);
    } 
}
