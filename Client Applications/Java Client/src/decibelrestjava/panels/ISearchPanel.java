package decibelrestjava.panels;

import javax.swing.JButton;

/**
 * Interface for search panels.
 */
public interface ISearchPanel {
    
    /**
     * Initializes the panel.
     */
    void initPanel();
    
    /**
     * Initializes the autocomplete fields.
     */
    void initAutoComplete();
    
    /**
     * Initializes the results table model.
     */
    void initResultsTable();
 
    /**
     * Initializes the search callback function.
     *
     * @param callback The object which implements the search callback functions.
     */
    void initSearchCallback(SearchCallback callback);
    
    /**
     * Begins the search.
     *
     * @param url The REST request URL.
     */
    void search(String url); 
    
    /**
     * Occurs when a page is selected from the result set.
     *
     * @param pageNumber The page number.
     */
    void pageChanged(int pageNumber);  
    
    /**
     * Gets the default button on the panel.
     *
     * @return The default button.
     */
    JButton getDefaultButton(); 
    
    /**
     * Gets the response string.
     *
     * @return The response string.
     */
    String getResponse();
    
}
