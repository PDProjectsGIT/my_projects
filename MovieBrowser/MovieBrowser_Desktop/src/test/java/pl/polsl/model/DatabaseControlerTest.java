/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pl.polsl.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.polsl.model.AdminUser;
import pl.polsl.model.DatabaseControler;
import pl.polsl.model.ModelException;
import pl.polsl.model.Movie;
import pl.polsl.model.PremiumUser;
import pl.polsl.model.SearchCriterion;
import pl.polsl.model.StandardUser;
import pl.polsl.model.User;

/**
 * This class contains unit tests for the DatabaseController class.
 * It verifies the functionality of various methods within the DatabaseController class,
 * covering different scenarios and boundary conditions.
 *
 * @author Pawel Drzazga
 * @version 1.0
 */
public class DatabaseControlerTest {
    
    /**
     * Parameterized test method for testing record deletion from a database table.
     *
     *
     * @param table The name of the database table from which the record is to be deleted.
     * @param column The column in the specified table based on which the deletion condition is applied.
     * @param condition The value representing the condition for deletion.
     * @param expectedRowsDeleted The expected number of rows that should be deleted as a result of the operation.
     */
    @ParameterizedTest
    @CsvSource({
        // OK
        "Users, Username, test_user_delete, 1",
        // The table does not exists
        "NonExistentTable, Column, some_value, 0",
        // Condition error
        "Users, Username, , 0",
        // Delete from empty table
        "Users, Username, empty, 0"
    })
    public void testDelete(String table, String column, String condition, int expectedRowsDeleted) {
        DatabaseControler databaseControler;
        try {
            if(condition == null)condition="";
            databaseControler = new DatabaseControler("TestDB.db");
            if(!condition.equals("empty")){
                databaseControler.insertUser(condition, "Password123", 0);
            }
            int actualRowsDeleted = databaseControler.delete(table, column, condition);
            assertEquals(expectedRowsDeleted, actualRowsDeleted);
        } catch (ModelException e) {
            if (expectedRowsDeleted == 0) {
                if(condition.equals("")){
                    assertEquals(23, e.getErrorCode());
                } else{
                    if(e.getErrorCode() == 15){
                        assertEquals(15, e.getErrorCode());
                    }else{
                        assertEquals(4, e.getErrorCode());
                    }
                }
            }else{
                fail("Unexpected error code");
            }
        }
    }
    
    /**
     * Parameterized test method for testing user insertion into the database.
     *
     * This method tests the {@code insertUser} operation with different scenarios.
     *
     * @param userName The username of the user being inserted.
     * @param password The password of the user being inserted.
     * @param rank The rank of the user being inserted.
     * @param expectedErrorCode The expected error code for the insertion operation.
     */
    @ParameterizedTest
    @CsvSource({
        // OK
        "test1, Password1, 0, 0",
        // User already exists
        "existingUser, Password1, 0, 15",
        // Incorrect login 1
        "tes, Password1, 0, 23",
        // Incorrect login 2
        "test%, Password1, 0, 23",
        // Incorrect password 1
        "test2, password, 0,  22",
        // Incorrect password 2
        "test3, Pass, 0,  22",
        // Incorrect rank 
        "test4, Pass, 10,  7"
    })
    void testInsertUser(String userName, String password, int rank, int expectedErrorCode) {
        DatabaseControler databaseControler;
        //ModelException exception = null;
        try{
            databaseControler = new DatabaseControler("TestDB.db");
            if(expectedErrorCode == 0){
                assertDoesNotThrow(() -> databaseControler.insertUser(userName, password, rank));
                assertEquals(rank, databaseControler.getRank(userName,password));
                databaseControler.delete("Users", "Username", userName);
            }else{
                
                ModelException exception = assertThrows(ModelException.class, () -> {
                    databaseControler.insertUser(userName, password, rank);
                });
                if(exception == null){
                    fail("Expected error.");
                }
            }
        } catch (ModelException e) {
            fail("Error during DatabaseControler creation: " + e.getMessage());
        }
    }
    
    /**
     * Parameterized test method for testing user information updates in the database.
     *
     * @param userName The username of the user to be updated.
     * @param newPassword The new password for the user update.
     * @param newRank The new rank for the user update.
     * @param expectedErrorCode The expected error code for the update operation.
     */
    @ParameterizedTest
    @CsvSource({
        // OK
        "existingUser, Password1, 0, 0",
        // User does not exists
        "test, newPassword1, 0, 15",
        // Incorrect login 1
        "new, Password1, 0, 23",
        // Incorrect login 2
        "newUserL^gin, Password1, 0, 23",
        // Incorrect password 1
        "existingUser, ne1, 0,  22",
        // Incorrect password 2
        "existingUser, newpassword, 0,  22"

    })
    void testUpdateUser(String userName, String newPassword, int newRank, int expectedErrorCode) {
        try{
            DatabaseControler databaseControler = new DatabaseControler("TestDB.db");
            if(expectedErrorCode == 0){
                switch(newRank){
                    case 0:
                        StandardUser userS = new StandardUser(databaseControler, userName, newPassword, 0.0);
                        assertDoesNotThrow(() -> databaseControler.updateUser(userS));
                        break;
                    case 1:
                        PremiumUser userP = new PremiumUser(databaseControler, userName, newPassword, 0.0);
                        assertDoesNotThrow(() -> databaseControler.updateUser(userP));
                        break;
                        
                    case 2:
                        AdminUser userA = new AdminUser(databaseControler, userName, newPassword, 0.0);
                        assertDoesNotThrow(() -> databaseControler.updateUser(userA));
                        break;
                }
                assertEquals(newRank, databaseControler.getRank(userName,newPassword));
            }else{
                ModelException exception = assertThrows(ModelException.class, () -> {
                    StandardUser userE = new StandardUser(databaseControler, userName, newPassword, 0.0);
                    databaseControler.updateUser(userE);
                });
                if(exception == null){
                    fail("Expected error.");
                }
            }
        } catch (ModelException e) {
            fail("Error during DatabaseControler creation: " + e.getMessage());
        }
    }
    
    /**
     * Parameterized test method for testing movie insertion into the database.
     *
     * @param title The title of the movie being inserted.
     * @param director The director of the movie being inserted.
     * @param genre The genre of the movie being inserted.
     * @param releaseYear The release year of the movie being inserted.
     * @param price The price of the movie being inserted.
     * @param expectedErrorCode The expected error code for the insertion operation.
     */
    @ParameterizedTest
    @CsvSource({
            // OK
            "TestMovie1, Director, Genre, 2022, 9.99, 0",
            // Movie already exists in daatabase
            "ExistingMovie, Director, Genre, 2023, 14.99, 14",
            // Incorrect data 1
            "TestMovie2, , Genre, 2022, 9.99, 24",
            // Incorrect data 2
            "TestMovie3, Director, , 2022, 9.99, 24",
            // Incorrect data 3
            "TestMovie4, Director, Genre, 1749, 9.99, 24",
            // Incorrect data 4
            "TestMovie5, Director, Genre, 2000, -9.99, 24",
            // Incorrect data 5
            ", Director, Genre, 2000, -9.99, 24"

    })
    void testInsertMovie(String title, String director, String genre, int releaseYear, double price, int expectedErrorCode) {
        DatabaseControler databaseControler;
        if(title == null) title ="";
        if(director == null) director ="";
        if(genre == null) genre ="";
        try {
            databaseControler = new DatabaseControler("TestDB.db");
            Movie movie = new Movie(title, director, genre, releaseYear, price);
            if(expectedErrorCode == 0){
               databaseControler.insertMovie(movie);
               databaseControler.delete("Movies", "Title", title);
            }else{
                databaseControler.insertMovie(movie);
                fail("Expected error.");
            }
        } catch (ModelException e) {
           assertEquals(expectedErrorCode, e.getErrorCode());
        }
    }
    
    
    /**
     * Parameterized test method for testing movie update in the database.
     *
     * @param title The title of the movie to be updated.
     * @param director The new director for the movie update.
     * @param genre The new genre for the movie update.
     * @param releaseYear The new release year for the movie update.
     * @param price The new price for the movie update.
      * @param expectedErrorCode The expected error code for the update operation.
      */
    @ParameterizedTest
    @CsvSource({
            // OK
            "ExistingMovie, NewDirector1, NewGenre1, 2023, 9.99, 0",
            // Movie not found
            "NonExistentTitle, NewDirector2, NewGenre2, 2022, 8.99, 18", 
    })
    void testUpdateMovie(String title, String director, String genre, int releaseYear, double price, int expectedErrorCode) {

        Movie movieToUpdate = new Movie(title, director, genre, releaseYear, price);
        DatabaseControler databaseController;

        try {
            databaseController = new DatabaseControler("TestDB.db");
            if (expectedErrorCode == 0) {
                databaseController.updateMovie(movieToUpdate);
            }else{
                databaseController.updateMovie(movieToUpdate);
                fail("Expected ModelException");
            }
        } catch (ModelException e) {
            assertEquals(expectedErrorCode, e.getErrorCode());
        }
    }
    
    /**
     * Parameterized test method for testing movie retrieval from the database based on search criteria.
     *
     * @param sc The search criterion for retrieving movies.
     * @param criterio The specific criteria value for the search.
     * @param expectedNum The expected number of movies to be retrieved based on the given criteria.
     */
    @ParameterizedTest
    @CsvSource({
            // OK
            "TITLE, getmovie1, 1",
            // OK
            "DIRECTOR, d1, 3",
            // OK
            "GENRE, g1, 2",
            // OK
            "RELEASE_YEAR, 2000, 2",
            // NOT OK
            "DIRECTOR, d3, 0" 
    })
    void testGetMovie(SearchCriterion sc, String criterio, int expectedNum){
        DatabaseControler databaseController;
        Map<SearchCriterion, String> criteria = new HashMap<>();
        List<Movie> movies;
        criteria.put(sc, criterio);
        try{
            databaseController = new DatabaseControler("TestDB.db");
            
            movies = databaseController.getMovies(criteria);
            
            assertEquals(expectedNum, movies.size());
            
        }catch(ModelException e) {
            fail("No error expected.");
        }
    }
    
    /**
     * Parameterized test method for testing the movie rental functionality.
     *
     * @param userName The username of the user attempting to rent movies.
     * @param password The password of the user attempting to rent movies.
     * @param sc The search criterion for retrieving movies to rent.
     * @param criterio The specific criteria value for the search.
     * @param amount The expected number of movies to be rented based on the given criteria.
     * @param success Indicates whether the rental operation is expected to succeed.
     */
    @ParameterizedTest
    @CsvSource({
            // OK
            "existingUser, Password1, TITLE, getmovie1, 1, true",
            // NOT OK
            "existingUser, Password1, TITLE, getmovie1, 0, false",
            // OK
            "existingUser, Password1, GENRE, g1, 2, true",
            // OK
            "existingUser, Password1, RELEASE_YEAR, 2000, 2, true",
            // OK
            "existingUser, Password1, DIRECTOR, d10, 0, true" 
    })
    void testRentMovie(String userName, String password, SearchCriterion sc, String criterio, int amount, boolean success){
        DatabaseControler databaseController;
        User user;
        Map<SearchCriterion, String> criteria = new HashMap<>();
        criteria.put(sc, criterio);
        
        List<Movie> movies;
        try{
            databaseController = new DatabaseControler("TestDB.db");
            
            databaseController.delete("Borrowings", "User_ID", 1);
            databaseController.delete("Borrowings", "User_ID", 2);
            databaseController.delete("Borrowings", "User_ID", 3);
            databaseController.delete("Borrowings", "User_ID", 4);
            databaseController.delete("Borrowings", "User_ID", 5);
            
            int rank = databaseController.getRank(userName,password);
            double balance = 1000;
            switch(rank){
            case 0:
                user = new StandardUser(databaseController, userName, password, balance);
                break;
            case 1:
                user = new PremiumUser(databaseController, userName, password, balance);
                break;
            case 2:
                user = new AdminUser(databaseController, userName, password, balance);
                break;
            default:
                int deletedRecords = databaseController.delete("Users", "Username",userName);
                throw new ModelException("Incorrect rank token. " + deletedRecords + " record(s) deleted.", 7);
                
            }
            movies = databaseController.getMovies(criteria);
            for(Movie m : movies){
                databaseController.rentMovie(user, m);
            }
            movies = user.getRentedMovies();
            if(success){
                assertEquals(amount, movies.size());
            }else{
                for(Movie m : movies){
                    databaseController.rentMovie(user, m);
                }
                fail("Error expected.");
            }
        }catch(ModelException e) {
            if(e.getErrorCode() != 20){
                fail("No error expected.");
            }
        }
    }
    
    /**
     * Parameterized test method for testing the end of movie rentals.
     *
     * @param userName The username of the user ending movie rentals.
     * @param password The password of the user ending movie rentals.
     * @param sc The search criterion for retrieving movies to end rentals.
     * @param criterio The specific criteria value for the search.
     * @param expected The expected number of movie rentals to be ended based on the given criteria.
     */
    @ParameterizedTest
    @CsvSource({
            // OK
            "existingUser, Password1, TITLE, getmovie1, 1",
            // NOT OK
            "existingUser, Password1, TITLE, getmovie10, 0",
    })
    void testEndRental(String userName, String password, SearchCriterion sc, String criterio, int expected){
        DatabaseControler databaseController;
        User user;
        Map<SearchCriterion, String> criteria = new HashMap<>();
        criteria.put(sc, criterio);
        List<Movie> movies;
        try{
            databaseController = new DatabaseControler("TestDB.db");
            int rank = databaseController.getRank(userName,password);
            double balance = 1000;
            switch(rank){
            case 0:
                user = new StandardUser(databaseController, userName, password, balance);
                break;
            case 1:
                user = new PremiumUser(databaseController, userName, password, balance);
                break;
            case 2:
                user = new AdminUser(databaseController, userName, password, balance);
                break;
            default:
                int deletedRecords = databaseController.delete("Users", "Username",userName);
                throw new ModelException("Incorrect rank token. " + deletedRecords + " record(s) deleted.", 7);
                
            }
            movies = databaseController.getMovies(criteria);
            for(Movie m : movies){
                databaseController.rentMovie(user, m);
            }
            movies = user.getRentedMovies();

            int endedRentals = 0;
            
            for(Movie m : movies){
                endedRentals+=databaseController.endRental(user, m);
            }
            databaseController.delete("Borrowings", "User_ID", 1);
            assertEquals(expected,endedRentals);
        }catch(ModelException e) {
            if(e.getErrorCode() != 4){
                fail("No error expected.");
            }
        }
    }
    
    /**
     * Parameterized test method for testing the retrieval of user balance from the database.
     *
     * @param userName The username of the user for whom the balance is retrieved.
     * @param balance The expected balance of the user.
     * @param success Indicates whether the retrieval operation is expected to succeed.
     */
    @ParameterizedTest
    @CsvSource({
            // OK
            "testbalance, 1000.0, true",
            // NOT OK
            "nonExistingUser, 1000.0, false",
            // NOT OK
            "testbalance, 100.0, false"
            
    })
    void testGetBalance(String userName, double balance, boolean success){
        DatabaseControler databaseController;
        try{
            databaseController = new DatabaseControler("TestDB.db");
            
            if(!success){
                if(balance == databaseController.getBalance(userName))
                fail("Expected error 6.");
            }else{
                assertEquals(balance, databaseController.getBalance(userName));
            }
        }catch(ModelException e) {
            if(e.getErrorCode() != 4 && e.getErrorCode() != 6){
                fail("No error expected.");
            }
        }
    }   
}