/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import pl.polsl.model.TableModel;
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
import javax.swing.SwingConstants;
import pl.polsl.model.User;

/**
 * MovieBrowserPanel class represents the panel for browsing movies in the Movie Browser application.
 * It includes user information, movie information, a search panel, a table of movies, and a button to rent selected movies.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
class MovieBrowserPanel extends GUI{
    
    /**
     * The user info panel.
     */
    final private UserInfoPanel userInfoPanel;
    
    /**
     * The movie insert panel.
     */
    final private MovieInsertPanel movieSearchPanel;
    
    /**
     * The movie amount label.
     */
    final private JLabel movieAmountContent;
    
    /**
     * The rented movie amount content label.
     */
    final private JLabel movieRentedAmountContent;
    
    /**
     * The rent button.
     */
    final private JButton rentButton;
    
    /**
     * The movies table model.
     */
    final private TableModel moviesTableModel;
    
    /**
     * Constructor for MovieBrowserPanel.
     */
    MovieBrowserPanel(){
        super(new GridBagLayout());

        JLabel userInfo = textLabel("User Info:", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel movieInfo = textLabel("Movies Info: ", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel searchMovie = textLabel("MovieBrowser", 18, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        
        //UserInfoPanel
        userInfoPanel = new UserInfoPanel(new Dimension(400, 90));
        
        //MovieInfoPanel   
        JPanel movieInfoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        movieInfoPanel.setPreferredSize(new Dimension(400, 90));
        movieInfoPanel.setBorder(compoundBorder);
        
        JLabel movieAmount = textLabel("Movies showed: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        movieAmountContent = textLabel("!To be set!", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.PLAIN);
        JLabel movieRentedAmount = textLabel("Movies rented: ", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        movieRentedAmountContent = textLabel("!To be set!", 12, SwingConstants.LEFT, SwingConstants.TOP, Font.PLAIN);
        
        movieInfoPanel.add(movieAmount );
        movieInfoPanel.add(movieAmountContent);
        movieInfoPanel.add(movieRentedAmount);
        movieInfoPanel.add(movieRentedAmountContent);
        
        movieSearchPanel = new MovieInsertPanel(new Dimension(815, 60));
        
        //Table model
        String[] columnNames = {
            "Tile", 
            "Director", 
            "Genre", 
            "Release Year", 
            "Price", 
            "Rent"
        };
        moviesTableModel = new TableModel();
        
        moviesTableModel.setColumnNames(columnNames);
        
        JTable movieTable = new JTable(moviesTableModel);
        JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
        movieTableScrollPane.setPreferredSize(new Dimension(815, 300));
        
        rentButton = new JButton("Rent selected movies");
        rentButton.setToolTipText("Click to rent a movie or Alt + M.");
        rentButton.setMnemonic(KeyEvent.VK_M);
        
        //Add to panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,5,5,10);
        gbc.anchor = GridBagConstraints.WEST;
        
        this.add(userInfo, gbc);
        gbc.gridx = 1;
        this.add(movieInfo, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(userInfoPanel, gbc);
        gbc.gridx = 1;
        this.add(movieInfoPanel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(searchMovie, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 3;
        this.add(movieSearchPanel, gbc);
        gbc.gridy = 4;
        this.add(movieTableScrollPane , gbc);
        gbc.gridy = 5;
        this.add(rentButton, gbc);
    }
    
    /**
     * Retrieves the typed title from the movie search panel.
     *
     * @return The typed title.
     */
    String getTypedTitle(){
        return movieSearchPanel.getTitle();
    }
    
    /**
     * Retrieves the typed director from the movie search panel.
     *
     * @return The typed director.
     */
    String getTypedDirector(){
        return movieSearchPanel.getDirector();
    }
    
    /**
     * Retrieves the typed genre from the movie search panel.
     *
     * @return The typed genre.
     */
    String getTypedGenre(){
        return movieSearchPanel.getGenre();
    }
    
    /**
     * Retrieves the typed release year from the movie search panel.
     *
     * @return The typed release year.
     */
    String getTypedReleaseYear(){
        return movieSearchPanel.getReleaseYear();
    }
    
    /**
     * Updates the Movie Browser Panel with user information and movie counts.
     *
     * @param loggedUser   The logged-in user.
     * @param moviesAmount The current number of movies.
     */
    void updateMovieBrowserPanel(User loggedUser, int moviesAmount){
        movieAmountContent.setText(""+moviesAmount);
        userInfoPanel.updateUserInfoPanel(loggedUser);  
        movieRentedAmountContent.setText(""+loggedUser.getRentedMoviesAmount());  
    }
    
    /**
     * Retrieves the movies table model.
     *
     * @return The movies table model.
     */
    TableModel getMoviesTable(){
        return moviesTableModel;
    }
    
    /**
     * Retrieves the selected movies to rent from the movies table.
     *
     * @return The selected movies to rent.
     */
    ArrayList<Object[]> getSelectedMoviesToRent(){
        return moviesTableModel.getSelectedRows();
    }
      
    /**
     * Adds an action listener to the search movie button in the movie search panel.
     *
     * @param listener The action listener to be added.
     */
    void addButtonSearchMovieListener(ActionListener listener){
        movieSearchPanel.getButton().addActionListener(listener);
    }
    
    /**
     * Retrieves the search movie button.
     *
     * @return The search movie button.
     */
    JButton getBrowserButton(){
        return movieSearchPanel.getButton();
    }
    
    /**
     * Adds an action listener to the rent movie button.
     *
     * @param listener The action listener to be added.
     */
    public void addButtonRentMovieListener(ActionListener listener){
        rentButton.addActionListener(listener);
    }
}
