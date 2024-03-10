/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import pl.polsl.model.TableModel;
import pl.polsl.model.User;

/**
 * MainGUI class represents the main graphical user interface for the Movie Browser application.
 * It includes panels for movie browsing, user settings, and admin settings.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class MainGUI extends GUI{
    
    /**
     * The movie browser panel.
     */
    final private MovieBrowserPanel movieBrowserPanel;
    
    /**
     * The user settings panel.
     */
    final private UserSettingsPanel userSettingsPanel;
    
    /**
     * The admin settings panel.
     */
    final private AdminSettingsPanel adminSettingsPanel;
    
    /**
     * The menu exit item.
     */
    private JMenuItem menuExitItem;
    
    /**
     * The menu logout item.
     */
    private JMenuItem menuLogoutItem;
    
    /**
     * The logged-in user.
     */
    final private User loggedUser;
    
    /**
     * Constructor for MainGUI.
     *
     * @param user The logged-in user.
     */
    public MainGUI(User user){
        super(new GridLayout(1,1));
 
        loggedUser = user;
        
        movieBrowserPanel = new MovieBrowserPanel();
        userSettingsPanel = new UserSettingsPanel();  
        adminSettingsPanel = new AdminSettingsPanel();
        
        frame.setJMenuBar(createMenuBar());
        
        JTabbedPane tabbedPane = createTabbedPane();
        add(tabbedPane);

        showGUI();
        
    }
    
    /**
     * Creates the menu bar for the MainGUI.
     *
     * @return The created menu bar.
     */
    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program");
        menuBar.add(menu);
        
        menuLogoutItem = new JMenuItem("Logout");
        menu.add(menuLogoutItem);
        

        menuExitItem = new JMenuItem("Exit");
        menu.add(menuExitItem);

        return menuBar;
    }
    
     /**
     * Creates the tabbed pane for different panels.
     *
     * @return The created tabbed pane.
     */
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add tabs to the tabbed pane
        tabbedPane.addTab("Movie browser panel: "+ loggedUser.getUsername(), null, movieBrowserPanel, "Panel for searching");
        tabbedPane.addTab("Settings panel: " + loggedUser.getUsername(), null, userSettingsPanel, "Panel for user information");
        if(loggedUser.getRank() == 2){
            tabbedPane.addTab("Admin panel: " + loggedUser.getUsername(), null, adminSettingsPanel, "Panel for administration");
        }
        return tabbedPane;
    }
    
    /**
     * Simulates a button search click
     */
    public void clickSearchButton(){
        movieBrowserPanel.getBrowserButton().doClick();
    }

    /**
     * Retrieves the typed title from the movie browser panel.
     *
     * @return The typed title.
     */
    public String getTypedTitle(){
        return movieBrowserPanel.getTypedTitle();
    }
    
    /**
     * Retrieves the typed director from the movie browser panel.
     *
     * @return The typed director.
     */
    public String getTypedDirector(){
        return movieBrowserPanel.getTypedDirector();
    }
    
    /**
     * Retrieves the typed genre from the movie browser panel.
     *
     * @return The typed genre.
     */
    public String getTypedGenre(){
        return movieBrowserPanel.getTypedGenre();
    }
    
    /**
     * Retrieves the typed release year from the movie browser panel.
     *
     * @return The typed release year.
     */
    public String getTypedReleaseYear(){
        return movieBrowserPanel.getTypedReleaseYear();
    }
    
    /**
     * Retrieves the typed new password from the user settings panel.
     *
     * @return The typed new password.
     */
    public String getTypedNewPassword(){
        return userSettingsPanel.getTypedNewPassword();
    }
    
    /**
     * Retrieves the typed balance from the user settings panel.
     *
     * @return The typed balance.
     */
    public String getTypedBalance(){
        return userSettingsPanel.getTypedBalance();
    }
    
    /**
     * Retrieves the typed title for insertion from the admin settings panel.
     *
     * @return The typed title for insertion.
     */
    public String getTypedInsertTitle(){
        return adminSettingsPanel.getTypedTitle();
    }
    
    /**
     * Retrieves the typed director for insertion from the admin settings panel.
     *
     * @return The typed director for insertion.
     */
    public String getTypedInsertDirector(){
        return adminSettingsPanel.getTypedDirector();
    }
    
    /**
     * Retrieves the typed genre for insertion from the admin settings panel.
     *
     * @return The typed genre for insertion.
     */
    public String getTypedInsertGenre(){
        return adminSettingsPanel.getTypedGenre();
    }
    
    /**
     * Retrieves the typed release year for insertion from the admin settings panel.
     *
     * @return The typed release year for insertion.
     */
    public String getTypedInsertReleaseYear(){
        return adminSettingsPanel.getTypedReleaseYear();
    }
    
    /**
     * Updates the main GUI with the current movies amount and user settings.
     *
     * @param moviesAmount The current number of movies.
     */
    public void updateMainGUI(int moviesAmount){
        userSettingsPanel.updateMovieSettingsPanel(loggedUser);
        movieBrowserPanel.updateMovieBrowserPanel(loggedUser, moviesAmount);
    }
    
     /**
     * Retrieves the movies table from the movie browser panel.
     *
     * @return The movies table model.
     */
    public TableModel updateMoviesTable(){
         return movieBrowserPanel.getMoviesTable();
    }
    
    /**
     * Retrieves the rented movies table from the user settings panel.
     *
     * @return The rented movies table model.
     */
    public TableModel updateRentedMoviesTable(){
        return userSettingsPanel.getRentedMoviesTable();
    }
    
    /**
     * Retrieves the movies to delete table from the admin settings panel.
     *
     * @return The movies to delete table model.
     */
    public TableModel updateMoviesToDeleteTable(){
        return adminSettingsPanel.getMoviesToDeleteTable();
    }
    
     /**
     * Retrieves the selected movies to rent from the movie browser panel.
     *
     * @return The selected movies to rent.
     */
    public ArrayList<Object[]> getSelectedMoviesToRent(){
        return movieBrowserPanel.getSelectedMoviesToRent();
    }
    
    /**
     * Retrieves the selected movies to end rental from the user settings panel.
     *
     * @return The selected movies to end rental.
     */
    public ArrayList<Object[]> getSelectedMoviesToEndRental(){
        return userSettingsPanel.getSelectedMoviesToRent();
    }
    
    /**
     * Retrieves the selected movies to delete from the admin settings panel.
     *
     * @return The selected movies to delete.
     */
    public ArrayList<Object[]> getSelectedMoviesToDelete(){
        return adminSettingsPanel.getSelectedMoviesToDelete();
    }
    
    /**
     * Adds an action listener to the search movie button in the movie browser panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonSearchMovieListener(ActionListener listener){
        movieBrowserPanel.addButtonSearchMovieListener(listener);
    }
    
    /**
     * Adds an action listener to the rent movie button in the movie browser panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonRentMovieListener(ActionListener listener){
        movieBrowserPanel.addButtonRentMovieListener(listener);
    }
    
    /**
     * Adds an action listener to the end rental button in the user settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonEndRentalMovieListener(ActionListener listener){
        userSettingsPanel.addButtonEndRentalMovieListener(listener);
    }
    
    /**
     * Adds an action listener to the change password button in the user settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonChangePasswordListener(ActionListener listener){
        userSettingsPanel.addButtonChangePasswordListener(listener);
    }
    
    /**
     * Adds an action listener to the add funds button in the user settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonAddFoundsListener(ActionListener listener){
        userSettingsPanel.addButtonAddFoundsListener(listener);
    }
    
    /**
     * Adds an action listener to the upgrade movie button in the user settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonUpgradeMovieListener(ActionListener listener){
        userSettingsPanel.addButtonUpgradeMovieListener(listener);
    }
    
    /**
     * Adds an action listener to the delete account button in the user settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonDeleteAccountListener(ActionListener listener){
        userSettingsPanel.addButtonDeleteAccountListener(listener);
    }
    
    /**
     * Adds an action listener to the delete movies button in the admin settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonDeleteMoviesListener(ActionListener listener){
        adminSettingsPanel.addButtonDeleteMoviesListener(listener);
    }
    
    /**
     * Adds an action listener to the exit menu item in the menu bar.
     *
     * @param listener The action listener to be added.
     */
    public void addExitMenuBarListener(ActionListener listener){
        menuExitItem.addActionListener(listener);
    }
    
    /**
     * Adds an action listener to the logout menu item in the menu bar.
     *
     * @param listener The action listener to be added.
     */
    public void addLogoutMenuBarListener(ActionListener listener){
        menuLogoutItem.addActionListener(listener);
    }
    
    /**
     * Adds an action listener to the insert movie button in the admin settings panel.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonInsertMovieListener(ActionListener listener){
        adminSettingsPanel.addButtonInsertMovieListener(listener);
    }
}
