/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pl.polsl.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface representing a user in the application.
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public interface User {
    
    /**
     * Get the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername();
    
    /**
     * Get the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword();
    
    /**
     * Get the rank of the user.
     *
     * @return The rank of the user.
     */
    public int getRank();
    
    /**
     * Get the balance of the user.
     *
     * @return The balance of the user.
     */
    public double getBalance();
    
    /**
     * Add funds to the user's account balance.
     *
     * @param funds The amount of funds to be added to the account balance.
     */
    public void addFunds(double funds);
    
    /**
     * Change the rank of the user.
     *
     * @param rank The new rank to assign to the user.
     * @throws ModelException If there are issues with changing the rank, an exception may be thrown.
     */
    public void changeRank(int rank) throws ModelException;
    
    /**
     * Change the password of the user.
     *
     * @param password The new rank to assign to the user.
     */
    public void changePassword(String password);
    
    /**
     * Get the rank name of the user.
     * 
     * @return The rank name of the user;
     */
    public String getRankName();
    
    /**
     * Get the amount of user rented movies.
     * 
     * @return The amount of rented movies.
     */
    public int getRentedMoviesAmount();
    
    /**
     * Increments a rented movies amount.
     */
    public void incrementRentedMovies();
    
    /**
     * Decrements a rented movies amount.
     */
    public void decrementRentedMovies();
    
    /**
     * Set the amount of rented movies
     * 
     * @param amount The amount of rented movies to set
     */
    public void setRentedMoviesAmount(int amount);
    
    /**
     * Inserts a new user into the database.
     *
     * @param userName The username of the new user.
     * @param password The password of the new user.
     * @param rank The rank of the new user.
     * @throws ModelException If there are issues with the insertion process, an exception may be thrown.
     */
    public void insertUser(String userName, String password, int rank) throws ModelException;
    
    /**
     * Updates user information in the database.
     *
     * @throws ModelException If there are issues with the update process, an exception may be thrown.
     */
    public void updateUser() throws ModelException;
    
    /**
     * Inserts a new movie into the database.
     *
     * @param movie The movie object to insert.
     * @throws ModelException If there are issues with the insertion process, an exception may be thrown.
     */
    public void insertMovie(Movie movie) throws ModelException;
    
    /**
     * Updates movie information in the database.
     *
     * @param movie The movie object containing updated information.
     * @throws ModelException If there are issues with the update process, an exception may be thrown.
     */
    public void updateMovie(Movie movie) throws ModelException;
    
    /**
     * Returns a list of movies based on the specified search criteria.
     *
     * This method retrieves a list of movies that match the provided search criteria.
     *
     * @param criteria A map of search criteria, where keys are search terms (e.g., "author," "title," "genre," "year") and values are corresponding SearchCriterion enum values.
     * @return An ArrayList of Movie objects that match the search criteria.
     * @throws ModelException If there is an issue with the data model or data retrieval.
     */
    public ArrayList<Movie> getMovies(Map<SearchCriterion, String> criteria) throws ModelException;
    
    /**
     * Deletes movie from the database.
     * 
     * @param movie The movie to delete.
     * @throws ModelException If an error occurs while removal of the movie.
     */
    public void deleteMovie(Movie movie) throws ModelException;
    
    /**
     * Rent the specified movie and create an entry in the Borrowings table.
     *
     * @param movie The movie to be rented.
     * @throws ModelException If an error occurs while renting the movie.
     */
    public void rentMovie(Movie movie) throws ModelException;
    
    /**
     * Get a list of movies rented.
     *
     * @return An ArrayList of Movie objects representing the rented movies.
     * @throws ModelException If an error occurs while retrieving the rented movies.
     */
    public ArrayList<Movie> getRentedMovies() throws ModelException;
    
    /**
     * Retrieves the renting date of the specified movie.
     *
     * @param movie The movie for which the renting date is requested.
     * @param mode True for the start date, false for the return date.
     * @return The renting date in the specified mode.
     * @throws ModelException If there is an issue retrieving the renting date.
     */
    public String getRentingDate(Movie movie, boolean mode) throws ModelException;
    
    /**
     * End the rental of the specified movie by the given user.
     *
     * @param movie The movie to be returned.
     * @throws ModelException If an error occurs while ending the rental.
     */
    public void endRental(Movie movie) throws ModelException;
    
    /**
     * Delete the user account.
     * 
     * @throws ModelException If an error occurs while deleting an account.
     */
    public void deleteAccount() throws ModelException;
}
