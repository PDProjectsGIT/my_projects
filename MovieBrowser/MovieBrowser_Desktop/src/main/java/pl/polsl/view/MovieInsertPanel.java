/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * MovieInsertPanel class represents the panel for inserting movie information in the Movie Browser application.
 * It includes text fields for title, director, genre, and release year, along with a button to initiate the insertion process.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class MovieInsertPanel extends GUI{
    
    /**
     * The title field.
     */
    final private JTextField titleField;
    
    /**
     * The director field.
     */
    final private JTextField directorField;
    
    /**
     * The genre field.
     */
    final private JTextField genreField;
    
    /**
     * The release year field.
     */
    final private JTextField releaseYearField;
    
    /**
     * The browse button.
     */
    final private JButton browseButton;
    
    /**
     * Constructor for MovieInsertPanel.
     *
     * @param dimension The preferred dimension of the panel.
     */
    MovieInsertPanel(Dimension dimension){
        super(new GridLayout(2, 5, 10, 0));
        setBorder(emptyBorder);
        setPreferredSize(dimension);
        
        JLabel titleLabel = textLabel("Title", 12, SwingConstants.CENTER, SwingConstants.TOP, Font.BOLD);
        JLabel directorLabel = textLabel("Director", 12, SwingConstants.CENTER, SwingConstants.TOP, Font.BOLD);
        JLabel genreLabel = textLabel("Genre", 12, SwingConstants.CENTER, SwingConstants.TOP, Font.BOLD);
        JLabel releaseYearLabel = textLabel("Release Year", 12, SwingConstants.CENTER, SwingConstants.TOP, Font.BOLD);
        JLabel fillGap = textLabel("", 12, SwingConstants.CENTER, SwingConstants.TOP, Font.PLAIN);
        
        titleField = new JTextField(15);
        titleField.setToolTipText("Type title.");
        directorField = new JTextField(15);
        directorField.setToolTipText("Type director.");
        genreField = new JTextField(15);
        genreField.setToolTipText("Type genre.");
        releaseYearField = new JTextField(5);
        releaseYearField.setToolTipText("Type release year.");
        
        browseButton = new JButton("Ok");
        browseButton.setToolTipText("Click to browse or Alt + B.");
        browseButton.setMnemonic(KeyEvent.VK_B);
        
        this.add(titleLabel);
        this.add(directorLabel);      
        this.add(genreLabel);      
        this.add(releaseYearLabel);     
        this.add(fillGap);
        this.add(titleField);
        this.add(directorField);
        this.add(genreField);
        this.add(releaseYearField);   
        this.add(browseButton);
    }
    
    /**
     * Retrieves the typed title from the title field.
     *
     * @return The typed title.
     */
    String getTitle(){
        return titleField.getText();
    }
    
    /**
     * Retrieves the typed genre from the genre field.
     *
     * @return The typed genre.
     */
    String getGenre(){
        return genreField.getText();
    }
    
    /**
     * Retrieves the typed release year from the release year field.
     *
     * @return The typed release year.
     */
    String getReleaseYear(){
        return releaseYearField.getText();
    }
    
    /**
     * Retrieves the typed director from the director field.
     *
     * @return The typed director.
     */
    String getDirector(){
        return directorField.getText();
    }
    
    /**
     * Retrieves the browse button.
     *
     * @return The browse button.
     */
    JButton getButton(){
        return browseButton;
    }
}
