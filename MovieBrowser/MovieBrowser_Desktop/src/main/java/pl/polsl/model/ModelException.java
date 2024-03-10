/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

/**
 * Custom exception class for handling model-related errors in the application.
 *
 * This exception is used to represent errors specific to the model layer of the application.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class ModelException extends Exception{
    
    /**
     * A readable error message that provides details about the exception.
     */
    final private String message;
    
    /**
     * An error code associated with the exception, allowing for more specific error identification.
     */
    final private int errorCode;
     
    /**
     * Constructs a new ModelException with a provided error message and error code.
     *
     * @param message The readable error message.
     * @param errorCode An error code associated with the exception.
     */
    public ModelException(String message, int errorCode){
        this.message = message;
        this.errorCode = errorCode;
    }
    
    /**
     * Get the readable error message associated with this exception.
     *
     * @return The error message.
     */
    @Override
    public String getMessage(){
        return message;
    }
    
    /**
     * Get the error code associated with this exception.
     *
     * @return The error code.
     */
    public int getErrorCode(){
        return errorCode;
    }
}
