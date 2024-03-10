/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import pl.polsl.model.TableModel;

/**
 * AdminSettingsPanel class extends GUI and represents the panel for managing movies.
 * It includes options to insert and delete movies from the database.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class AdminSettingsPanel extends GUI{
    
    /**
     * Movie insert panel component.
     */
    final private MovieInsertPanel movieInsertPanel;
    
    /**
     * Table model component.
     */
    final private TableModel moviesToDeleteModel;
    
    /**
     * Delete movies button component.
     */
    final private JButton  deleteMoviesButton;
    
    /**
     * Constructor to initialize the AdminSettingsPanel.
     */
    AdminSettingsPanel(){
        super(new GridBagLayout());
        
        JLabel manageMovies = textLabel("Manage movies", 18, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel insertMovie = textLabel("Insert movie to database.", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        JLabel deleteMovies = textLabel("Delete movies from database.", 14, SwingConstants.LEFT, SwingConstants.TOP, Font.BOLD);
        
        movieInsertPanel = new MovieInsertPanel(new Dimension(815, 60));
        
        //Movies Table
        String[] columnMoviesNames = {
            "Tile", 
            "Director", 
            "Genre", 
            "Release Year", 
            "Price", 
            "Drop"
        };
        
        moviesToDeleteModel = new TableModel();
        moviesToDeleteModel.setColumnNames(columnMoviesNames);
        
        JTable movieTable = new JTable(moviesToDeleteModel);
        JScrollPane movieTableScrollPane = new JScrollPane(movieTable);
        movieTableScrollPane.setPreferredSize(new Dimension(815, 300));
        
        deleteMoviesButton = new JButton("Delete movies");
        deleteMoviesButton.setToolTipText("Click to delete selected movies or Alt + X.");
        
        //Add to panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10,5,5,10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        
        add(manageMovies,gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        add(insertMovie, gbc);
        gbc.gridy = 2;
        
        add(movieInsertPanel, gbc);
        gbc.gridy = 3;
        add(deleteMovies, gbc);
        
        gbc.gridy = 4;
        add(movieTableScrollPane, gbc);
        
        gbc.gridy = 5;
        add(deleteMoviesButton, gbc);
    }
    
    /**
     * Retrieves the selected movies to delete from the table.
     *
     * @return An ArrayList of selected movie rows.
     */
    ArrayList<Object[]> getSelectedMoviesToDelete(){
        return moviesToDeleteModel.getSelectedRows();
    }
    
    /**
     * Retrieves the table model for movies to delete.
     *
     * @return The TableModel for movies to delete.
     */
    TableModel getMoviesToDeleteTable(){
        return moviesToDeleteModel;
    }
    
    /**
     * Retrieves the title typed in the movie insertion panel.
     *
     * @return The typed title.
     */
    String getTypedTitle(){
        return movieInsertPanel.getTitle();
    }
    
    /**
     * Retrieves the director typed in the movie insertion panel.
     *
     * @return The typed director.
     */
    String getTypedDirector(){
        return movieInsertPanel.getDirector();
    }
    
    /**
     * Retrieves the genre typed in the movie insertion panel.
     *
     * @return The typed genre.
     */
    String getTypedGenre(){
        return movieInsertPanel.getGenre();
    }
    
    /**
     * Retrieves the release year typed in the movie insertion panel.
     *
     * @return The typed release year.
     */
    String getTypedReleaseYear(){
        return movieInsertPanel.getReleaseYear();
    }
    
    /**
     * Adds an ActionListener to the delete movies button.
     *
     * @param listener The ActionListener to be added.
     */
    void addButtonDeleteMoviesListener(ActionListener listener){
        deleteMoviesButton.addActionListener(listener);
    }
    
    /**
     * Adds an ActionListener to the insert movie button in the insertion panel.
     *
     * @param listener The ActionListener to be added.
     */
    void addButtonInsertMovieListener(ActionListener listener){
        movieInsertPanel.getButton().addActionListener(listener);
    }
}
