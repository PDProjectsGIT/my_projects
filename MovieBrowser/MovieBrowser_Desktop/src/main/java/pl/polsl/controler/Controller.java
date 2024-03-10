/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.controler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import pl.polsl.model.*;
import pl.polsl.view.*;


/**
 * The class representing the controller
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public class Controller {
    
    /**
     * Private field representing a session.
     * This field stores information about a user's session.
     */
    private Session session;
    
    /**
     * Private error handling field.
     */
    final private GUI errorControlGUI;
    
    /**
     * Constructor for the Controller class.
     * Initializes the user interface components and stores command-line arguments.
     */
    public Controller(){
        errorControlGUI = new GUI();
    }
    
    /**
     * Handles the login action initiated by the login button in the LoginGUI.
     *
     * @param loginGUI The LoginGUI instance associated with the login action.
     */
    private void handleLoginAction(LoginGUI loginGUI){
        String login = loginGUI.getLogin();
        String password = new String(loginGUI.getPassword());
        try{
            session = new Session("MovieBrowserDB.db");
            if (login.isEmpty() || password.isEmpty()) {
            loginGUI.showErrorDialog("Please fill in the username and password fields.");
            return;
            }
            session.login(login, password); 
            loginGUI.showSuccessDialog("Welcome back " + session.getUser().getUsername() +"!");
        }catch(ModelException e){
            loginGUI.showErrorDialog("pl.polsl.model.ModelException: " + e.getMessage() + " Code: " + e.getErrorCode());
        }finally{
            if(session != null && session.isLogged()){
                loginGUI.dispose(); 
                mainSession();
            }
        }
    }
    
    /**
     * Handles the signup action initiated by the signup button in the LoginGUI.
     *
     * @param loginGUI The LoginGUI instance associated with the signup action.
     */
    private void handleSignupAction(LoginGUI loginGUI){
        String login = loginGUI.getLogin();
        String password = new String(loginGUI.getPassword());
        try{
            session = new Session("MovieBrowserDB.db");
            if (login.isEmpty() || password.isEmpty()) {
            loginGUI.showErrorDialog("Please fill in the username and password fields.");
            return;
            }
            session.signup(login,password);
            loginGUI.showSuccessDialog("Welcome " + session.getUser().getUsername() +"!");
        }catch(ModelException e){
            loginGUI.showErrorDialog("pl.polsl.model.ModelException: " + e.getMessage() + " Code: " + e.getErrorCode());
        }finally{
            if(session != null && session.isLogged()){
                loginGUI.dispose(); 
                mainSession();
            }
        }
    }
    
    /**
     * Logs the user out of the current session.
     */
    private void logout(){
        try{
            session.logout();
        }catch(ModelException e){
            errorControlGUI.showErrorDialog("pl.polsl.model.ModelException: " + e.getMessage() + " Code: " + e.getErrorCode());
        }
    }
    
    /**
     * Initiates the login process.
     */
    public void login(){
        LoginGUI loginUserGUI = new LoginGUI(); 
        loginUserGUI.addLoginButtonListener(e -> {
            handleLoginAction(loginUserGUI);
            
        });
        loginUserGUI.addSignupButtonListener(e -> {
            handleSignupAction(loginUserGUI);
        });
    }
    
    /**
     * Updates the given table model with the provided list of movies.
     *
     * @param model The table model to update.
     * @param movies The list of movies to populate the table with.
     * @throws ModelException If there is an issue updating the table model.
     */
    private void updateGUIMovieTable(TableModel model, ArrayList<Movie> movies) throws ModelException{
        model.clearTable();
        for(Movie m : movies){
            Object[] row = {m.getTitle(), m.getDirector(), m.getGenre(), m.getReleaseYear(), m.getPrice(), false};
            model.addRow(row);
        }
    }
    
    /**
     * Updates the rented movies table in the main GUI with the provided list of rented movies.
     *
     * @param mainGUI The main graphical user interface.
     * @param rentedMovies The list of rented movies to populate the table with.
     * @throws ModelException If there is an issue updating the rented movies table.
     */
    private void updateGUIRentedMoviesTable(MainGUI mainGUI, ArrayList<Movie> rentedMovies) throws ModelException{
        mainGUI.updateRentedMoviesTable().clearTable();
        for(Movie m : rentedMovies){
            Object[] row = {m.getTitle(), session.getUser().getRentingDate(m, true), session.getUser().getRentingDate(m, false), false};
            mainGUI.updateRentedMoviesTable().addRow(row);
        }
    }
    
    /**
     * Handles the user session, displaying appropriate actions based on the user's login status and rank.
     * 
     * If the session is not initialized or the user is not logged in, the method returns.
     */
    private void mainSession(){
        try{ //GUI 
            User user = session.getUser();
            
            Map<SearchCriterion, String> criteria = new HashMap<>();
            
            MainGUI mainGUI = new MainGUI(user);

            ArrayList<Movie>  allMovies = user.getMovies(criteria);
            
            updateGUIMovieTable(mainGUI.updateMoviesTable(), allMovies);
            updateGUIMovieTable(mainGUI.updateMoviesToDeleteTable(), allMovies);
            mainGUI.updateMainGUI(allMovies.size());
            
            updateGUIRentedMoviesTable(mainGUI, user.getRentedMovies());
            
            mainGUI.addExitMenuBarListener(e -> {
                mainGUI.dispose();
            });
            
            mainGUI.addLogoutMenuBarListener(e -> {
                mainGUI.dispose();
                logout();
                login();
            });
            
            mainGUI.addButtonSearchMovieListener(e -> {
                criteria.clear();
                String title = mainGUI.getTypedTitle();
                if(title.length() != 0) criteria.put(SearchCriterion.TITLE, title);
                String director = mainGUI.getTypedDirector();
                if(director.length() != 0) criteria.put(SearchCriterion.DIRECTOR, director);
                String releaseYear = mainGUI.getTypedReleaseYear();
                if(releaseYear.length() != 0) criteria.put(SearchCriterion.RELEASE_YEAR, releaseYear);
                String genre = mainGUI.getTypedGenre();
                if(genre.length() != 0) criteria.put(SearchCriterion.GENRE, genre);
                try{  
                    ArrayList<Movie>  tempMovies = user.getMovies(criteria);
                    updateGUIMovieTable(mainGUI.updateMoviesToDeleteTable(), tempMovies);
                    updateGUIMovieTable(mainGUI.updateMoviesTable(), tempMovies);
                    updateGUIRentedMoviesTable(mainGUI, user.getRentedMovies());
                    mainGUI.updateMainGUI(tempMovies.size());
                }catch(ModelException ex){
                    mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                }
            });
            
            mainGUI.addButtonRentMovieListener(e -> {
                ArrayList<Object[]> rows = mainGUI.getSelectedMoviesToRent();
                for (Object[] o : rows) {
                    String title = o[0].toString();
                    String director = o[1].toString();
                    String genre = o[2].toString();
                    int releaseYear = Integer.parseInt(o[3].toString());
                    double price = Double.parseDouble(o[4].toString());
                    Movie m = new Movie(title, director, genre, releaseYear, price);
                    try{
                        user.rentMovie(m);
                        user.updateUser();
                        ArrayList<Movie>  tempMovies = user.getMovies(criteria);
                        updateGUIMovieTable(mainGUI.updateMoviesTable(), tempMovies);
                        mainGUI.updateMainGUI(tempMovies.size());
                        updateGUIRentedMoviesTable(mainGUI, user.getRentedMovies());
                        mainGUI.showSuccessDialog("You rented a movie "+m.getTitle()+".");
                    }catch(ModelException ex){
                        mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    }
                    
                }
            });
            
            mainGUI.addButtonEndRentalMovieListener(e -> {
                ArrayList<Object[]> rows = mainGUI.getSelectedMoviesToEndRental();
                for (Object[] o : rows) {
                    String title = o[0].toString();
                    Movie m = new Movie(title, "", "", 0, 0);
                    try{
                        user.endRental(m);
                        user.updateUser();
                        updateGUIRentedMoviesTable(mainGUI, user.getRentedMovies());
                        mainGUI.updateMainGUI(allMovies.size());
                        mainGUI.showSuccessDialog("You have finished renting the movie "+m.getTitle()+".");
                    }catch(ModelException ex){
                        mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    }
                }
            });
            
            mainGUI.addButtonChangePasswordListener(e -> {
                String newPassword = mainGUI.getTypedNewPassword();
                String password = user.getPassword();
                try{
                    user.changePassword(newPassword);
                    user.updateUser();
                    mainGUI.updateMainGUI(allMovies.size());
                    mainGUI.showSuccessDialog("You changed your password to "+newPassword+".");
                }catch(ModelException ex){
                    mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    user.changePassword(password);
                }
            });
            
            mainGUI.addButtonAddFoundsListener(e -> {
                try{
                    double foundsToAdd = Double.parseDouble(mainGUI.getTypedBalance());
                    try{
                        user.addFunds(foundsToAdd);
                        user.updateUser();
                        mainGUI.updateMainGUI(allMovies.size());
                        mainGUI.showSuccessDialog("You have added funds to your account:  $"+foundsToAdd+".");
                    }catch(ModelException ex){
                        user.addFunds(-foundsToAdd);
                        mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    }
                }catch(NumberFormatException ex){
                    mainGUI.showErrorDialog(ex.getMessage());
                }
            }); 
            
            mainGUI.addButtonUpgradeMovieListener(e -> {
                if(user.getRank() == 2){
                    mainGUI.showErrorDialog("Admin user cannot upgrade the account.");
                }else{
                    try{
                        user.changeRank(1);
                        user.updateUser();
                        mainGUI.updateMainGUI(allMovies.size());
                    }catch(ModelException ex){
                        mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    }
                }
            });
            
            mainGUI.addButtonDeleteAccountListener(e -> {
                int result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete account", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try{
                        user.deleteAccount();
                        mainGUI.dispose();
                        logout();
                        login();
                    }catch(ModelException ex){
                        mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                    }
                }
            });
            
            mainGUI.addButtonDeleteMoviesListener(e -> {
                ArrayList<Object[]> rows = mainGUI.getSelectedMoviesToDelete();
                for (Object[] o : rows) {
                    String title = o[0].toString();
                    int result = JOptionPane.showConfirmDialog(null, "Are you sure to delete "+title+"?", "Delete account", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        Movie m = new Movie(title, "", "", 0, 0);
                        try{
                            user.deleteMovie(m);
                            user.updateUser();
                            mainGUI.clickSearchButton();
                            mainGUI.showSuccessDialog("You have deleted movie from the database:  "+m.getTitle()+".");
                        }catch(ModelException ex){
                            mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                        }
                    }
                }
            });
            
            mainGUI.addButtonInsertMovieListener(e -> {
                String title = mainGUI.getTypedInsertTitle();
                String director = mainGUI.getTypedInsertDirector();
                String releaseYear = mainGUI.getTypedInsertReleaseYear();
                String genre = mainGUI.getTypedInsertGenre();
                if(title.length() != 0 && director.length() != 0 && releaseYear.length() != 0 && genre.length() != 0){
                    try{
                        String moviePrice = JOptionPane.showInputDialog(null, "Type price:", "Set movie price", JOptionPane.PLAIN_MESSAGE);
                        Movie movie = new Movie(title, director, genre, Integer.parseInt(releaseYear), Double.parseDouble(moviePrice));
                        try{
                            user.insertMovie(movie);
                            mainGUI.clickSearchButton();
                        }catch(ModelException ex){
                            mainGUI.showErrorDialog("pl.polsl.model.ModelException: " + ex.getMessage() + " Code: " + ex.getErrorCode());
                        }
                    }catch(NumberFormatException ex){
                        mainGUI.showErrorDialog(ex.getMessage());
                    } 
                }else{
                    mainGUI.showErrorDialog("All fields must be filled.");
                }
            });
        }catch(ModelException e){
            errorControlGUI.showErrorDialog("pl.polsl.model.ModelException: " + e.getMessage() + " Code: " + e.getErrorCode());
        }
    }
}