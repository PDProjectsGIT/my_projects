/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pl.polsl.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel class extends AbstractTableModel to provide a custom table model for Swing JTable.
 * It handles data manipulation and retrieval for the GUI table.
 * 
 * @author Pawel Drzazga
 * @version 1.0
 */
public class TableModel extends AbstractTableModel {

    /**
     * Array containing column names for the table.
     */
    private String[] columnNames;

    /**
     * 2D array containing the table data.
     */
    private Object[][] data = {};

    /**
     * Sets the column names for the table.
     *
     * @param colNames An array of column names.
     */
    public void setColumnNames(String[] colNames){
        columnNames = colNames;
    }
    
    /**
     * Gets the number of columns in the table.
     *
     * @return The number of columns.
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Gets the number of rows in the table.
     *
     * @return The number of rows.
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * Gets the name of the specified column.
     *
     * @param col The index of the column.
     * @return The name of the column.
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Gets the value at the specified row and column in the table.
     *
     * @param row The index of the row.
     * @param col The index of the column.
     * @return The value at the specified position.
     */
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /**
     * Gets the class type of the specified column.
     *
     * @param c The index of the column.
     * @return The class type of the column.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /**
     * Checks if a cell at the specified row and column is editable.
     *
     * @param row The index of the row.
     * @param col The index of the column.
     * @return True if the cell is editable, false otherwise.
     */
    public boolean isCellEditable(int row, int col) {
        if (col != columnNames.length-1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets the value at the specified row and column in the table.
     *
     * @param value The new value.
     * @param row   The index of the row.
     * @param col   The index of the column.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
    
    /**
     * Adds a new row to the table.
     *
     * @param rowData The data for the new row.
     * @throws ModelException If the length of the data array does not match the number of columns.
     */
    public void addRow(Object[] rowData) throws ModelException{
        if (rowData.length != columnNames.length) {
            throw new ModelException("GUI table update error", 26);
        }
        Object[][] newData = new Object[data.length + 1][columnNames.length];
        for (int i = 0; i < data.length; i++) {
            System.arraycopy(data[i], 0, newData[i], 0, columnNames.length);
        }
        System.arraycopy(rowData, 0, newData[data.length], 0, columnNames.length);
        data = newData;
        fireTableDataChanged();
    }
    
    /**
     * Clears all data from the table.
     */
    public void clearTable() {
        Object[][] newData = new Object[0][columnNames.length];
        data = newData;
        fireTableDataChanged();
    }
    
    /**
     * Gets a list of selected rows in the table.
     *
     * @return An ArrayList of selected rows.
     */
    public ArrayList<Object[]> getSelectedRows() {
        ArrayList<Object[]> selectedRows = new ArrayList<>();

        for (int i = 0; i < data.length; i++) {
            Boolean isChecked = (Boolean) data[i][columnNames.length-1]; // 5 to numer kolumny z checkboxem

            if (isChecked != null && isChecked) {
                selectedRows.add(data[i]);
            }
        }

        return selectedRows;
    }
}
