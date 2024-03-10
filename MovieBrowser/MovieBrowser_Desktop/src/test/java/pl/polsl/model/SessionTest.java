/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package pl.polsl.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.ArrayList;
import pl.polsl.model.ModelException;
import pl.polsl.model.Session;

/**
 * Test class for the Session class.
 *
 * This class contains parameterized test methods for various functionalities in the Session class.
 *
 * @author Pawel Drzazga
 * @version 1.0
 */
public class SessionTest {
    
    /**
     * Parameterized test method for testing the retrieval of user information from the session.
     *
     * @param login The username for session login.
     * @param password The password for session login.
     * @param expectedErrorCode The expected error code for the getUser operation.
     */
    @ParameterizedTest
    @CsvSource({
        // OK
        "testlogin,Password1,0",
        // NOT OK
        "notexistinguser,Password1,10",
    })
    public void getUserTest(String login, String password, int expectedErrorCode){
        Session session;
        try{
            session = new Session("TestDB.db");
            session.login(login,password);
        }catch(ModelException e){
            if(e.getErrorCode()==6){ 
                try{
                    session = new Session("TestDB.db");
                    session.getUser();
                }catch(ModelException me){
                    assertEquals(expectedErrorCode, me.getErrorCode());
                }
            }
        }
    }
    
    /**
     * Parameterized test method for testing user login functionality in the session.
     *
     * @param login The username for session login.
     * @param password The password for session login.
     * @param isPreviousSessionActive Indicates whether a previous session is active.
     * @param expectedErrorCode The expected error code for the login operation.
     */
    @ParameterizedTest
    @CsvSource({
        //OK
        "testlogin,Password1,false, 0",
        //NOT OK
        "testlogin,Password1,true, 9",
        //NOT OK
        "notexistinguser, Password1, false ,6"
    })
    public void loginTest(String login, String password, boolean isPreviousSessionActive, int expectedErrorCode){
        Session session;
        try{
            session = new Session("TestDB.db");
            session.login(login,password); 
            if(isPreviousSessionActive){
                session.login(login,password);
                //fail("Expected error");
            }
        }catch(ModelException e){
            assertEquals(expectedErrorCode, e.getErrorCode());
        }
    }
    
    /**
     * Parameterized test method for testing user signup functionality in the session.
     *
     * @param login The username for user signup.
     * @param password The password for user signup.
     * @param expectedErrorCode The expected error code for the signup operation.
     */
    @ParameterizedTest
    @CsvSource({
        //OK
        "notexistinguser,Password1, 15",
    })
    public void signupTest(String login, String password, int expectedErrorCode){
        Session session;
        try{
            session = new Session("TestDB.db");
            session.signup(login,password); 
        }catch(ModelException e){
            assertEquals(expectedErrorCode, e.getErrorCode());
        }
    }
    
    /**
     * Parameterized test method for testing user logout functionality in the session.
     *
     * @param login The username for session login.
     * @param password The password for session login.
     * @param previousSession Indicates whether a previous session is active.
     * @param expectedErrorCode The expected error code for the logout operation.
     */
    @ParameterizedTest
    @CsvSource({
        //OK
        "testlogin, Password1, true, 0",
        "testlogin, Password1, false, 8"
    })
    public void logoutTest(String login, String password, boolean previousSession, int expectedErrorCode){
        Session session;
        try{
            session = new Session("TestDB.db");
            if(previousSession){
                session.login(login, password);
            }
            session.logout();
        }catch(ModelException e){
            assertEquals(expectedErrorCode, e.getErrorCode());
        }
    }
}
