/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.view;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * Represents a Graphical User Interface (GUI) for the Movie Browser application.
 * 
 * @author Pawel Drzazga
 * @version 3.0
 */
public class GUI extends JPanel{
    
    /**
     * The main frame for the GUI.
     */
    final protected JFrame frame;
    
    /**
     * The line border component.
     */
    protected Border lineBorder;
    
    /**
     * The empty border component.
     */
    protected Border emptyBorder;
    
    /**
     * The compound border component.
     */
    protected Border compoundBorder;
 
    
    /**
     * Constructs a new GUI and sets up the main frame.
     * 
     * @param gridLayout The GridLayout to be applied to the GUI.
     */
    public GUI(GridLayout gridLayout){
        super(gridLayout);
               
        // Borders
        lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        
        // Frame
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Movie Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    } 
    
    /**
     * Constructor with a specified layout.
     *
     * @param gridBagLayout The layout to be used for the GUI.
     */
    public GUI(GridBagLayout gridBagLayout){
        super(gridBagLayout);
        
        //Borders
        lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        compoundBorder = new CompoundBorder(lineBorder, emptyBorder);
        
        // Frame
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Movie Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }
    
    /**
     * Default constructor for GUI without a specified layout.
     */
    public GUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Movie Browser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    }
    
    /**
     * Display an error dialog with the provided error message.
     *
     * @param message The error message to display.
     */
    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Display a success dialog with the provided message.
     *
     * @param message The success message to display.
     */
    public void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Create a JLabel with specified properties.
     *
     * @param title The text to be displayed on the label.
     * @param size The font size of the label.
     * @param swingConstantsX The horizontal alignment of the label.
     * @param swingConstantsY The vertical alignment of the label.
     * @param style The font style of the label.
     * @return JLabel with the specified properties.
     */
    protected JLabel textLabel(String title, int size, int swingConstantsX, int swingConstantsY, int style){
        JLabel textLabel = new JLabel(title);
        textLabel.setFont(new Font("SansSerif", style, size));
        textLabel.setHorizontalAlignment(swingConstantsX);
        textLabel.setVerticalAlignment(swingConstantsY);
        return textLabel;
    }
    
    /**
     * Dispose of the GUI panel and frame.
     */
    public void dispose(){
        frame.dispose();
    }
    
    /**
     * Remove the GUI panel from the frame.
     */
    public void removePanel(){
        frame.remove(this);
    }
    
    /**
     * Retrieves the JFrame associated with this GUI component.
     *
     * @return The JFrame instance associated with this GUI.
     */
    protected JFrame getFrame(){
        return frame;
    }
    
    /**
     * Displays the graphical user interface (GUI).
     */
    protected void showGUI(){
        frame.getContentPane().add(this);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
