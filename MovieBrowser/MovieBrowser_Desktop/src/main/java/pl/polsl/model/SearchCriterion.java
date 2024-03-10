/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

/**
 * Represents different criteria for searching movies.
 * 
 * This enum defines various search criteria, including TITLE, DIRECTOR, GENRE, and RELEASE_YEAR,
 * which can be used to specify how movies should be searched.
 *
 * @author Pawel Drzazga
 * @version 1.0
 */
public enum SearchCriterion {
    /**
     * Search criterion for querying movies based on their titles.
     */
    TITLE, 
    
    /**
     * Search criterion for querying movies based on their directors.
     */
    DIRECTOR, 
    
    /**
     * Search criterion for querying movies based on their genres.
     */
    GENRE,  
    
    /**
     * Search criterion for querying movies based on their release years.
     */
    RELEASE_YEAR 
}



