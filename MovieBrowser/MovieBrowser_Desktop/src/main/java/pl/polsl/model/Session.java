/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

/**
 * Represents a user session in the application.
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public class Session {
    
    /**
     * The currently logged-in user.
     */
    private User user;
    
    /**
     * A flag indicating whether a user is logged in.
     */
    private boolean isLogged = false;
    
    /**
     * The administrator user associated with the session.
     */
    final private AdminUser sessionAdmin; 
    
    /**
     * The database controller used for database operations.
     */
    final private DatabaseControler dataBaseControler;
    
    /**
     * Constructs a new user session with a database controller and admin user.
     *
     * @throws ModelException If there's an issue during session initialization, an exception may be thrown.
     * @param databaseName The name of data base file.
     */
    public Session(String databaseName) throws ModelException{
        dataBaseControler = new DatabaseControler(databaseName);
        sessionAdmin = new AdminUser(dataBaseControler, "MovieBrowser", "javawinternecie", 0.0);
    }
    
    /**
     * Gets the currently logged-in user.
     *
     * @return The currently logged-in user.
     * @throws ModelException If there is no user logged in, an exception with error code 10 is thrown.
     */
    public User getUser() throws ModelException{
        if(!isLogged) throw new ModelException("There is no user logged in", 10);
        return this.user;
    }
    
    /**
     * Gets the administrator user associated with the session.
     *
     * @return The administrator user.
     */
    public AdminUser getSessionAdmin(){
        return this.sessionAdmin;
    }
    
    /**
     * Logs out the currently logged-in user.
     *
     * @throws ModelException If there is no active session to log out of, an exception with error code 8 is thrown.
     */
    public void logout() throws ModelException{
        if(!isLogged) throw new ModelException("Log out: There is no active session.", 8);
        isLogged = false;
    }
    
    /**
     * Logs in a user with the provided login and password.
     *
     * @param login The user's login.
     * @param password The user's password.
     * @throws ModelException If a previous session is still active or there are issues with the login process, an exception may be thrown.
     */
    public void login(String login, String password)throws ModelException{
         if(isLogged) throw new ModelException("Log in: Previous session is still active.", 9);
         makeUser(login, password);
         isLogged = true;
    }
    
    /**
     * Signs up a new user with the provided login and password.
     *
     * @param login The new user's login.
     * @param password The new user's password.
     * @throws ModelException If a previous session is still active or there are issues with the signup process, an exception may be thrown.
     */
    public void signup(String login, String password) throws ModelException{
        if(isLogged) throw new ModelException("Sign up: Previous session is still active.", 9);
        sessionAdmin.insertUser(login, password, 0);
        makeUser(login, password);
        isLogged = true;
    }
    
    /**
     * Checks if a user is currently logged in.
     *
     * @return `true` if a user is logged in, `false` otherwise.
     */
    public boolean isLogged(){
        return isLogged;
    }
    
    /**
     * Creates a user object based on the provided login and password and assigns it to the user field.
     *
     * @param userName The user's login.
     * @param password The user's password.
     * @throws ModelException If there are issues during user creation or verification, an exception may be thrown.
     */
    private void makeUser(String userName, String password)throws ModelException{
        double balance = dataBaseControler.getBalance(userName);
        switch(dataBaseControler.getRank(userName, password)){
            case 0:
                user = new StandardUser(dataBaseControler, userName, password, balance);
                break;
            case 1:
                user = new PremiumUser(dataBaseControler, userName, password, balance);
                break;
            case 2:
                user = new AdminUser(dataBaseControler, userName, password, balance);
                break;
            default:
                int deletedRecords = dataBaseControler.delete("Users", "Username",userName);
                throw new ModelException("Incorrect rank token. " + deletedRecords + " record(s) deleted.", 7);
        }
        user.setRentedMoviesAmount(user.getRentedMovies().size());
    }
}
