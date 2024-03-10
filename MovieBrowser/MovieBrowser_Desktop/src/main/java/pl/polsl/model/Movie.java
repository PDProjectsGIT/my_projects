/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

/**
 * Represents a movie with its associated details.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class Movie {
    
    /**
     * The title of the movie.
     */
    final private String title;
    
    /**
     * The director of the movie.
     */
    final private String director;
    
    /**
     * The genre of the movie.
     */
    final private String genre;
    
    /**
     * The year of release of the movie.
     */
    final private int releaseYear;
    
    /**
     * The price of the movie.
     */
    private double price;
    
    /**
     * Constructs a new Movie object with the provided details.
     *
     * @param title The title of the movie.
     * @param director The director of the movie.
     * @param genre The genre of the movie.
     * @param releaseYear The year of release of the movie.
     * @param price The price of the movie.
     */
    public Movie(String title, String director, String genre, int releaseYear, double price){
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
    }
    
    /**
     * Get the title of the movie.
     *
     * @return The title of the movie.
     */
    public String getTitle(){
        return title;
    }
    
    /**
     * Get the director of the movie.
     *
     * @return The director of the movie.
     */
    public String getDirector(){
        return director;
    }
    
    /**
     * Get the genre of the movie.
     *
     * @return The genre of the movie.
     */
    public String getGenre(){
        return genre;
    }
    
    /**
     * Get the release year of the movie.
     *
     * @return The year of release of the movie.
     */
    public int getReleaseYear(){
        return releaseYear;
    }
    
    /**
     * Get the price of the movie.
     *
     * @return The price of the movie.
     */
    public double getPrice(){
        return price;
    }
    
    /**
     * Set the price of the movie.
     *
     * @param price The new price of the movie.
     */
    public void setPrice(Double price){
        this.price = price;
    }
}
