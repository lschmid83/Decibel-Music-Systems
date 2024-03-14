package decibelrestjava.panels;

/**
 * Interface which defines the DiscChangedCallback functions.
 */
public interface DiscChangedCallback {

    /**
     * Occurs when a disc is selected.
     * 
     * @param discNumber The disc number.
     */
    void discChanged(String discNumber);
}
