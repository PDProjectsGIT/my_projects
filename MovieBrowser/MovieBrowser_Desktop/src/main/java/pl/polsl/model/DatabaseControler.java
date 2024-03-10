/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;



/**
 * A class for controlling the database operations.
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public class DatabaseControler {
    
    /**
     * Private constant representing the database connection URL.
     */
    final private String dbURL;
    
    /**
     * Private field representing a database connection.
     */
    private Connection connection;
    
    /**
     * Constructor for the DatabaseController class.
     * Initializes the database URL based on the provided database name.
     *
     * @param dataBaseName The name of the database file.
     * @throws ModelException If the database file is not found, an exception is thrown with error code 1.
     */
    public DatabaseControler(String dataBaseName) throws ModelException{
        if(!dbExists(dataBaseName)) throw new ModelException("Database file not found.", 1);
        dbURL = "jdbc:sqlite:" + dataBaseName;
    }
    
    /**
     * Establishes a connection to the database using the provided URL.
     *
     * @throws ModelException If a SQL exception occurs during the connection, an exception with error code 2 is thrown.
     */
    private void connect() throws ModelException{
        try{
            connection = DriverManager.getConnection(dbURL);
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 2);
        }
    }
    
    /**
     * Closes the connection to the database.
     *
     * @throws ModelException If a SQL exception occurs during the disconnection, an exception with error code 3 is thrown.
     */
    private void disconnect() throws ModelException{
        try{
            connection.close();
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 3);
        }
    }
 
    /**
     * Executes a SELECT query on the database and returns a ResultSet.
     *
     * @param query The SQL SELECT query to execute.
     * @return A ResultSet containing the results of the query.
     * @throws ModelException If a SQL exception occurs during the query, an exception with error code 4 is thrown.
     */
    private ResultSet select(String query) throws ModelException{
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            return result;
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Executes a SELECT query with a condition on the database and returns a ResultSet.
     *
     * @param query      The SQL SELECT query to execute.
     * @param condition  The condition to apply in the query.
     * @return A ResultSet containing the results of the query.
     * @throws ModelException If a SQL exception occurs during the query, an exception with error code 4 is thrown.
     */
    private ResultSet select(String query, String condition) throws ModelException{
       try{
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setString(1, condition);
           ResultSet result = preparedStatement.executeQuery();
           return result;
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Deletes rows from a table in the database based on a String condition.
     *
     * @param table     The name of the table from which to delete rows.
     * @param collumn   The column in the table to use in the condition.
     * @param condition The condition to apply for deletion.
     * @return The number of rows deleted.
     * @throws ModelException If a SQL exception occurs during deletion, an exception with error code 4 is thrown.
     */
    int delete(String table, String collumn, String condition) throws ModelException{
        try{
            connect();
            String deleteQuery = "DELETE FROM "+table+" WHERE "+collumn+" = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, condition);
            int deleteRows = preparedStatement.executeUpdate();
            disconnect();
            return deleteRows;
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Deletes the specified user account from the database.
     *
     * @param user The user account to be deleted.
     * @throws ModelException If there is an issue deleting the user account.
     */
    public void deleteAccount(User user) throws ModelException{
        delete("Users", "Username", user.getUsername());
    }
    
    /**
     * Deletes rows from a table in the database based on a int condition.
     *
     * @param table     The name of the table from which to delete rows.
     * @param collumn   The column in the table to use in the condition.
     * @param condition The condition to apply for deletion.
     * @return The number of rows deleted.
     * @throws ModelException If a SQL exception occurs during deletion, an exception with error code 4 is thrown.
     */
    public int delete(String table, String collumn, int condition) throws ModelException{
        try{
            connect();
            String deleteQuery = "DELETE FROM "+table+" WHERE "+collumn+" = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, condition);
            int deleteRows = preparedStatement.executeUpdate();
            disconnect();
            return deleteRows;
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Validates a password for compliance with specified criteria.
     *
     * This method checks whether a password meets the following criteria:
     * - It must have a minimum length of 8 characters.
     * - It must contain at least one uppercase letter.
     *
     * @param password The password to be validated.
     * @return True if the password meets the criteria, false otherwise.
     */
    private boolean validatePassword(String password) {
        if (password.length() < 8) return false;   
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Validates a user login according to specified criteria.
     *
     * This method checks whether a user login meets the following criteria:
     * - It must have a minimum length of 6 characters.
     * - It should only contain letters (both uppercase and lowercase) and digits.
     * - It should not contain any spaces.
     *
     * @param login The user login to be validated.
     * @return True if the login meets the criteria, false otherwise.
     */
    private boolean validateUserName(String userName) {
        if (userName.length() < 4) return false;

        if (!userName.matches("^[a-zA-Z0-9_]+$")) return false;

        if (userName.contains(" ")) return false;

        return true;
    }   
    
    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param userName The username of the user to be checked for existence in the database.
     * @return true if a user with the given username exists, false otherwise.
     * @throws ModelException If a SQL error occurs during the database query.
     */
    private boolean checkUser(String userName) throws ModelException{
        try{
            if(select("SELECT Username FROM Users WHERE Username = ?;", userName).next()){
                return true;
            }else return false;
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Checks if a movie with the given title exists in the database.
     *
     * @param title The title of the movie to be checked for existence in the database.
     * @return true if a movie with the given title exists, false otherwise.
     * @throws ModelException If a SQL error occurs during the database query.
     */
    private boolean checkMovie(String title) throws ModelException{
        try{
            if(select("SELECT Title FROM Movies WHERE Title = ?;", title).next()){
                return true;
            }else return false;
        }catch(SQLException e){
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Inserts a new user into the Users table in the database.
     *
     * @param userName The username of the user to insert.
     * @param password The user's password.
     * @param rank     The user's rank.
     * @throws ModelException If a SQL exception occurs during insertion, an exception with error code 4 is thrown.
     */
    public void insertUser(String userName, String password, int rank) throws ModelException{
        if(rank < 0 || rank > 3) throw new ModelException("Incorrect rank token. ", 7);
        if(!validatePassword(password)) throw new ModelException("The password is too short or missing a capital letter.", 22);
        if(!validateUserName(userName)) throw new ModelException("The username contains illegal characters or is too short.", 23);
        try{
            connect();
            if(checkUser(userName)){
                disconnect();
                throw new ModelException("User " + userName + " is already in database.", 15);
            }
            String insertQuery = "INSERT INTO USERS(username, password, rank, balance) VALUES(?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, rank);
            preparedStatement.setDouble(4, 0.0);
            preparedStatement.executeUpdate();
            disconnect();
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Updates user information in the Users table in the database.
     *
     * @param user The user object containing updated information.
     * @throws ModelException If a SQL exception occurs during the update, an exception with error code 4 is thrown.
     */
    public void updateUser(User user) throws ModelException{
        String userName = user.getUsername();
        String password = user.getPassword();
        int rank = user.getRank();
        if(rank < 0 || rank > 3) throw new ModelException("Incorrect rank token. ", 7);
        if(!validatePassword(password)) throw new ModelException("The password is too short or missing a capital letter.", 22);
        if(!validateUserName(userName)) throw new ModelException("The username contains illegal characters or is too short.", 23);
        try{ 
            connect();
            if(!checkUser(userName)){
                disconnect();
                throw new ModelException("User " + userName + " is not in database.", 17);
            }
            String updateQuery = "UPDATE Users SET Password = ?, Rank = ?, Balance = ? WHERE Username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, password);
            preparedStatement.setInt(2, rank);
            preparedStatement.setDouble(3, user.getBalance());
            preparedStatement.setString(4, userName);
            preparedStatement.executeUpdate();
            disconnect();
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Inserts a new movie into the Movies table in the database.
     *
     * @param movie The movie object to insert.
     * @throws ModelException If a SQL exception occurs during insertion, an exception with error code 4 is thrown.
     */
    public void insertMovie(Movie movie) throws ModelException{
        String title = movie.getTitle();
        String director = movie.getDirector();
        String genre = movie.getGenre();
        int releaseYear = movie.getReleaseYear();
        double price = movie.getPrice();
        if(title.equals("") || director.equals("") || genre.equals("") || releaseYear < 1750 || price < 0) 
            throw new ModelException("Incorect movie data.", 24);
        try{
            connect();         
            if(checkMovie(title)) {
                disconnect();
                throw new ModelException("Movie "+title+" is already in database.", 14);
            }
            String insertQuery = "INSERT INTO MOVIES(Title, Director, Genre, Release_Year, Price) VALUES(?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, director);
            preparedStatement.setString(3, genre);
            preparedStatement.setInt(4, releaseYear);
            preparedStatement.setDouble(5, price);
            preparedStatement.executeUpdate();
            disconnect();
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    } 
    
    /**
     * Updates movie information in the Movies table in the database.
     *
     * @param movie The movie object containing updated information.
     * @throws ModelException If a SQL exception occurs during the update, an exception with error code 4 is thrown.
     */
    public void updateMovie(Movie movie) throws ModelException{
        try{
            connect();
            String title = movie.getTitle();
            if(!checkMovie(title)){
                disconnect();
                throw new ModelException("Movie "+title+" is not in database.", 18);
            }
            String updateQuery = "UPDATE Movies SET Title = ?, Director = ?, Genre = ?, Release_Year = ?, Price = ? WHERE Title = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, movie.getDirector());
            preparedStatement.setString(3, movie.getGenre());
            preparedStatement.setInt(4, movie.getReleaseYear());
            preparedStatement.setDouble(5, movie.getPrice());
            preparedStatement.setString(6, title);
            preparedStatement.executeUpdate();
            disconnect();
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
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
    public ArrayList<Movie> getMovies(Map<SearchCriterion, String> criteria) throws ModelException{
        try{
            connect();
            if (criteria.size() > 4) {
                disconnect();
                throw new ModelException("Invalid number of arguments. Expected pairs of movie search criterion and value.",19);
            }
            ArrayList<Movie> movies = new ArrayList<>();
            String selectQuery = "SELECT Title, Director, Genre, Release_Year, Price FROM Movies";
            int criteriaIndex = 0;
            for (SearchCriterion key : criteria.keySet()) {
                if(criteriaIndex==0){
                    selectQuery+=" WHERE ";
                }else{
                    selectQuery+=" AND ";
                }
                if(key == SearchCriterion.RELEASE_YEAR){
                    selectQuery+=key.toString()+" = "+criteria.get(key);
                }else{
                    selectQuery+=key.toString()+" = '"+criteria.get(key)+"'";
                }
                
                criteriaIndex++;
            }
            selectQuery += ";";
            ResultSet resultMovies = select(selectQuery);
            while(resultMovies.next()){
                String title = resultMovies.getString("Title");
                String director = resultMovies.getString("Director");
                String genre = resultMovies.getString("Genre");
                int releaseYear = resultMovies.getInt("Release_Year");
                double price = resultMovies.getDouble("Price");
                Movie movie = new Movie(title, director, genre, releaseYear, price);
                movies.add(movie);
            }
            disconnect();
            return movies;
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Rent the specified movie for the given user and create an entry in the Borrowings table.
     *
     * @param user The user renting the movie.
     * @param movie The movie to be rented.
     * @throws ModelException If an error occurs while renting the movie.
     */
    public void rentMovie(User user, Movie movie) throws ModelException{
        try{
            connect();
            
            int userID;
            int movieID;
            
            Calendar calendar = Calendar.getInstance();
            
            java.util.Date currentDate = calendar.getTime();                         
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            String rentDate = sdf.format(currentDate);
            
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            
            java.util.Date nextDate = calendar.getTime();
            
            String expireDate = sdf.format(nextDate);
            
            //check if user exists
            String userName = user.getUsername();
            ResultSet resultSetUser = select("SELECT UserID FROM users WHERE username = ?;", userName);
            if (resultSetUser.next()) {
                userID = resultSetUser.getInt("UserID");
            } else {
                disconnect();
                throw new ModelException("User " + userName + " is not in the database.", 17);
            }
            
            
            //check if movie exists
            String title = movie.getTitle();
            ResultSet resultSetMovie = select("SELECT MovieID FROM Movies WHERE Title = ?;", title);
            if (resultSetMovie.next()) {
                movieID = resultSetMovie.getInt("MovieID");
            } else {
                disconnect();
                throw new ModelException("Movie " + title + " is not in the database.", 18);
            }
            
            //check if rent exists
            if(select("SELECT * from Borrowings WHERE Movie_ID ="+movieID+" AND User_ID ="+userID+";").next()){
                disconnect();
                throw new ModelException("User " + userName + " has already rented the movie "+title+".", 20);
            }
            
            String insertQuery = "INSERT INTO Borrowings('Movie_ID', 'User_ID', 'Borrowing_Date', 'Expires') VALUES(?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, movieID);
            preparedStatement.setInt(2, userID);
            preparedStatement.setString(3, rentDate);
            preparedStatement.setString(4, expireDate);
            int amount = preparedStatement.executeUpdate();
            if(amount > 0){
                user.incrementRentedMovies();
            }
            disconnect();
            updateUser(user);
            
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * End the rental of the specified movie by the given user.
     *
     * @param user The user ending the movie rental.
     * @param movie The movie to be returned.
     * @throws ModelException If an error occurs while ending the rental.
     * @return Amount of ended rentals.
     */
    public int endRental(User user, Movie movie) throws ModelException{
        try{
            connect();
            String userName = user.getUsername();
            String title = movie.getTitle();
            
            String deleteQuery = "DELETE FROM Borrowings "
                    + "WHERE User_ID = (SELECT UserID FROM Users WHERE Username = ?) "
                    + "AND MOVIE_ID = (SELECT MovieID FROM Movies WHERE Title = ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, title);
            int updateRows = preparedStatement.executeUpdate();
            disconnect();
            if(updateRows > 0){
                user.decrementRentedMovies();
            }
            return updateRows;
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Get a list of movies rented by the specified user.
     *
     * @param user The user for whom to retrieve rented movies.
     * @return An ArrayList of Movie objects representing the rented movies.
     * @throws ModelException If an error occurs while retrieving the rented movies.
     */
    public ArrayList<Movie> getRentedMovies(User user) throws ModelException{
        try{
            connect();
            
            ArrayList<Movie> movies = new ArrayList<>();
            String userName = user.getUsername();
            
            if(!checkUser(userName)){
                disconnect();
                throw new ModelException("User " + userName + " is not in database.", 17);
            }
            
            String selectQuery = "SELECT Title, Director, Genre, Release_Year, Price FROM Movies m "
                    + "INNER JOIN Borrowings b ON b.Movie_ID = m.MovieID "
                    + "INNER JOIN Users u ON u.UserID = b.User_ID "
                    + "WHERE u.Username = ?";
            
            ResultSet resultMovies = select(selectQuery, userName);
            
            while(resultMovies.next()){
                String title = resultMovies.getString("Title");
                String director = resultMovies.getString("Director");
                String genre = resultMovies.getString("Genre");
                int releaseYear = resultMovies.getInt("Release_Year");
                double price = resultMovies.getDouble("Price");
                movies.add(new Movie(title, director, genre, releaseYear, price));
            }
            disconnect();
            
            return movies;
            
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Gets the renting date information for a specific movie and user.
     *
     * @param user The user for whom the renting date is queried.
     * @param movie The movie for which the renting date is queried.
     * @param mode If true, returns the borrowing date; if false, returns the expiration date.
     * @return The renting date information.
     * @throws ModelException If there is an issue retrieving the renting date information.
     */
    public String getRentingDate(User user, Movie movie, boolean mode) throws ModelException{
        try{
            connect();

            String title = movie.getTitle();
              
            String selectQuery = "SELECT Borrowing_Date, Expires FROM Borrowings b "
                    + "INNER JOIN Users u ON b.User_ID = u.UserID "
                    + "INNER JOIN Movies m ON b.Movie_ID = m.MovieID "
                    + "WHERE m.Title = ?;";
            
            ResultSet resultMovies = select(selectQuery, title);
            
            if(resultMovies.next()){
                String date;
                if(mode){
                    date = resultMovies.getString("Borrowing_Date");
                }else{
                    date = resultMovies.getString("Expires");
                }
                disconnect();
                return date;

            }else{
                disconnect();
                throw new ModelException(user.getUsername() + " has not rented movie "+title, 27);
            }      
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Checks a user's rentals and removes expired rentals from the database.
     *
     * @param user The user object whose rentals are being checked.
     * @throws ModelException If the user does not exist in the database or a SQL error occurs during database operations.
     * @return Deleted rows from Borrowing table.
     */
    public int checkRental(User user) throws ModelException{
        
        int deletedRows = 0;
                    
        Calendar calendar = Calendar.getInstance();
            
        java.util.Date currentDate = calendar.getTime();
        
        List<String> toDelete = new ArrayList<>();
               
        try{
            connect();
            String userName = user.getUsername();
            
            if(!checkUser(userName)){
                disconnect();
                throw new ModelException("User " + userName + " is not in database.", 17);
            }
            
            String selectQuery = "SELECT b.BorrowingID, b.Expires FROM Movies m "
                    + "INNER JOIN Borrowings b ON b.Movie_ID = m.MovieID "
                    + "INNER JOIN Users u ON u.UserID = b.User_ID "
                    + "WHERE u.Username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, userName);
            ResultSet results = preparedStatement.executeQuery();
            while(results.next()){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try{
                    java.util.Date expireDate;
                    expireDate = sdf.parse(results.getString("Expires"));
                    if(expireDate.after(currentDate)){
                        Integer result = results.getInt("BorrowingID");
                        toDelete.add(result.toString());
                    }
                }catch(ParseException p){
                     throw new ModelException(p.getMessage(), 4);
                }                      
            }           
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }finally{
            for(String id : toDelete){
                deletedRows += delete("Borrowings", "BorrowingID", id);  
            }
        }
        return deletedRows;
    } 
    
    /**
     * Get the balance for the specified user.
     *
     * @param userName The username of the user.
     * @return The balance of the user.
     * @throws ModelException If there is an error while retrieving the balance or if the user is not found.
     */
    public double getBalance(String userName) throws ModelException{
        double balance;
        try{
            connect();
            ResultSet result = select("SELECT Balance FROM Users WHERE Username = ?;", userName);
            if(result.next()){
                balance = result.getDouble("Balance");
                disconnect();
                return balance;
            }else {
                disconnect();
                throw new ModelException("User not found: '" +userName+"'.", 6);
            }
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
    
    /**
     * Checks a user's login credentials in the Users table in the database.
     *
     * @param userName The username to check.
     * @param password The user's password to validate.
     * @return The user's rank if the login is successful.
     * @throws ModelException If a SQL exception occurs during the check, an exception with error code 4 is thrown.
     */
    public int getRank(String userName, String password) throws ModelException{
        int rank;
        try{
            connect();
            ResultSet result = select("SELECT Password, Rank FROM Users WHERE Username = ?;", userName);
            if(result.next()){
                if(result.getString("Password").equals(password)){
                    rank = result.getInt("Rank");
                    disconnect();
                    return rank;
                }else { 
                    disconnect();
                    throw new ModelException("Wrong password. User: '"+userName+"'.", 5);
                }
            }else {
                disconnect();
                throw new ModelException("User not found: '" +userName+"'.", 6);
            }
        }catch(SQLException e){
            disconnect();
            throw new ModelException(e.getMessage(), 4);
        }
    }
     
    /**
     * Checks if a database file with the provided name exists.
     *
     * @param dataBaseName The name of the database file to check.
     * @return `true` if the database file exists, `false` otherwise.
     */
    private boolean dbExists(String dataBaseName){
        File dataBaseFile = new File(dataBaseName);
        return dataBaseFile.exists();
    }
}
