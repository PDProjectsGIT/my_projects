/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import pl.polsl.model.User;

/**
 * UserInfoPanel class represents the panel displaying user information in the Movie Browser application.
 * It includes labels for the username, balance, and user rank.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class UserInfoPanel extends GUI{
    
    /**
     * The username label.
     */
    final private JLabel usernameLabelContent;
    
    /**
     * The balance label.
     */
    final private JLabel balanceLabelContent;
    
    /**
     * The rank label.
     */
    final private JLabel rankLabelContent;
    
    /**
     * Constructor for UserInfoPanel.
     *
     * @param dimension The preferred dimension of the panel.
     */
    UserInfoPanel(Dimension dimension){
        super(new GridLayout(3, 2, 10, 10));
        this.setBorder(compoundBorder);
        this.setPreferredSize(dimension);
        
        JLabel usernameLabel = textLabel("Usarname: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        usernameLabelContent = textLabel("!To be set!", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.PLAIN);
        JLabel balanceLabel = textLabel("Balance $: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        balanceLabelContent = textLabel("!To be set!", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.PLAIN);
        JLabel rankLabel = textLabel("Rank: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        rankLabelContent = textLabel("!To be set!", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.PLAIN);
        
        this.add(usernameLabel);
        this.add(usernameLabelContent);
        
        this.add(balanceLabel);
        this.add(balanceLabelContent);
        
        this.add(rankLabel);
        this.add(rankLabelContent);
    }
    
    /**
     * Updates the user information panel with the provided user data.
     *
     * @param user The user whose information will be displayed.
     */
    void updateUserInfoPanel(User user){
        usernameLabelContent.setText(user.getUsername());
        balanceLabelContent.setText(""+user.getBalance());
        rankLabelContent.setText(user.getRankName());
    }
}
