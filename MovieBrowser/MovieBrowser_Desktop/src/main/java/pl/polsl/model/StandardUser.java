/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a standard user in the application.
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public class StandardUser implements User {
    
    /**
     * The username of the standard user.
     */
    final private String userName;
    
    /**
     * The password of the standard user.
     */
    private String password;
    
    /**
     * The balance of the standard user.
     */
    private double balance;
    
    /**
     * The database handler used for database operations.
     */
    final private DatabaseControler databaseHandler;
    
    /**
     * The rank of the standard user.
     */
    private int userRank = 0;
    
    /**
     * The amount of rented movies
     */
    private int rentedMovies = 0;
    
    /**
     * Constructs a new standard user with the specified database handler, username, and password.
     *
     * @param databaseHandler The database handler to use for operations.
     * @param userName The username of the standard user.
     * @param password The password of the standard user.
     * @param balance The balance of the standard user.
     */
    public StandardUser(DatabaseControler databaseHandler, String userName, String password, double balance){
        this.databaseHandler = databaseHandler;
        this.userName = userName;
        this.password = password;
        this.balance = balance;
    }
    
    /**
     * Get the username of the standard user.
     *
     * @return The username of the standard user.
     */
    @Override
    public String getUsername(){
        return userName;
    }
    
    /**
     * Get the password of the standard user.
     *
     * @return The password of the standard user.
     */
    @Override
    public String getPassword(){
        return password;
    }
    
    /**
     * Get the rank of the standard user.
     *
     * @return The rank of the standard user.
     */
    @Override
    public int getRank(){
        return userRank;
    }
    
    /**
     * Get the balance of the standard user.
     *
     * @return The balance of the standard user.
     */
    @Override
    public double getBalance(){
        return balance;
    }
    
    /**
     * Add funds to the user's account balance.
     *
     * @param funds The amount of funds to be added to the account balance.
     */
    @Override
    public void addFunds(double funds){
        balance += funds;
    }
    
    /**
     * Change the rank of the standard user.
     *
     * @param rank The new rank to assign to the standard user.
     * @throws ModelException If the standard user already has the specified rank, an exception is thrown.
     */
    @Override
    public void changeRank(int rank) throws ModelException{
        if(userRank == rank) throw new ModelException("User already has that rank.", 16);
        if((balance - 100) < 0){
            throw new ModelException("Insufficient funds in the account.",21);
        }else{
            balance-=100;
        }
        userRank = rank;
    }
    
    /**
     * Change the password of the user.
     *
     * @param password The new rank to assign to the user.
     */
    @Override
    public void changePassword(String password){
        this.password = password;
    }
    
    /**
     * Get the rank name of the standard user.
     * 
     * @return The rank name of the standard user;
     */
    @Override
    public String getRankName(){
        switch(userRank){
            case 0: 
                return "Standard";
            case 1:
                return "Premium";
            default:
                return "Incorrect rank";
        }   
    }
    
    /**
     * Get the amount of standard user rented movies.
     * 
     * @return The amount of rented movies;
     */
    @Override
    public int getRentedMoviesAmount(){
        return rentedMovies;
    }
    
    /**
     * Increments a rented movies amount.
     */
    @Override
    public void incrementRentedMovies(){
        rentedMovies++;
    }
    
    /**
     * Decrements a rented movies amount.
     */
    @Override
    public void decrementRentedMovies(){
        rentedMovies--;
    }
    
    /**
     * Set the amount of rented movies
     * 
     * @param amount The amount of rented movies to set
     */
    @Override
    public void setRentedMoviesAmount(int amount){
        rentedMovies = amount;
    }
    
    /**
     * Inserting a new user is not allowed for standard users.
     *
     * @param userName The username of the new user.
     * @param password The password of the new user.
     * @param rank The rank of the new user.
     * @throws ModelException Access is denied for this operation.
     */
    @Override
    public void insertUser(String userName, String password, int rank) throws ModelException{
        throw new ModelException("Access denied.", 13);
    }
    
    /**
     * Updates user information in the database.
     *
     * @throws ModelException If there are issues with the update process, an exception may be thrown.
     */
    @Override
    public void updateUser() throws ModelException{
        databaseHandler.updateUser(this);
    }
    
    /**
     * Inserting a new movie is not allowed for standard users.
     *
     * @param movie The movie object to insert.
     * @throws ModelException Access is denied for this operation.
     */
    @Override
    public void insertMovie(Movie movie) throws ModelException{
        throw new ModelException("Access denied.", 13);
    }
    
    /**
     * Updating movie information is not allowed for standard users.
     *
     * @param movie The movie object containing updated information.
     * @throws ModelException Access is denied for this operation.
     */
    @Override
    public void updateMovie(Movie movie) throws ModelException{
        throw new ModelException("Access denied.", 13);
    }
    
    /**
     * Returns a list of movies based on the specified search criteria.
     *
     * This method retrieves a list of movies that match the provided search criteria.
     *
     * @param criteria A map of search criteria, where keys are search terms (e.g., "author," "title," "genre," "year") and values are corresponding SearchCriterion enum values.
     * @return An ArrayList of Movie objects that match the search criteria.
     * @throws ModelException If there is an issue with the data model or data retrieval.
     */
    @Override
    public ArrayList<Movie> getMovies(Map<SearchCriterion, String> criteria) throws ModelException{
        return databaseHandler.getMovies(criteria);
    }
    
    /**
     * Deletion movie is not allowed for premium users.
     * 
     * @param movie The movie to delete.
     * @throws ModelException If an error occurs while removal of the movie.
     */
    @Override
    public void deleteMovie(Movie movie) throws ModelException{
        throw new ModelException("Access denied.", 13);
    }
    
    /**
     * Rent the specified movie for the given user and create an entry in the Borrowings table.
     *
     * @param movie The movie to be rented.
     * @throws ModelException If an error occurs while renting the movie.
     */
    @Override
    public void rentMovie(Movie movie) throws ModelException{
        double price = movie.getPrice();
        if((balance - price) < 0){
            throw new ModelException("Insufficient funds in the account.",21);
        }
        balance = (double)Math.round((balance - price)  * 100) / 100;
        databaseHandler.rentMovie(this, movie);
    }
    
    /**
     * Get a list of movies rented.
     *
     * @return An ArrayList of Movie objects representing the rented movies.
     * @throws ModelException If an error occurs while retrieving the rented movies.
     */
    @Override
    public ArrayList<Movie> getRentedMovies() throws ModelException{
        return databaseHandler.getRentedMovies(this);
    }
    
    /**
     * Retrieves the renting date of the specified movie.
     *
     * @param movie The movie for which the renting date is requested.
     * @param mode True for the start date, false for the return date.
     * @return The renting date in the specified mode.
     * @throws ModelException If there is an issue retrieving the renting date.
     */
    @Override
    public String getRentingDate(Movie movie, boolean mode) throws ModelException{
        return databaseHandler.getRentingDate(this, movie, mode);
    }
    
    /**
     * End the rental of the specified movie by the given user..
     *
     * @param movie The movie to be returned.
     * @throws ModelException If an error occurs while ending the rental.
     */
    @Override
    public void endRental(Movie movie) throws ModelException{
        databaseHandler.endRental(this,movie);
    }
    
    /**
     * Delete the user account.
     * 
     * @throws ModelException If an error occurs while deleting an account.
     */
    @Override
    public void deleteAccount() throws ModelException{
        databaseHandler.deleteAccount(this);
    }
}