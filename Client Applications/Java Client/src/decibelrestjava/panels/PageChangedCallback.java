package decibelrestjava.panels;

/**
 * Interface which defines the PageChangedCallback functions.
 */
public interface PageChangedCallback {

    /**
     * Occurs when a page is selected.
     * 
     * @param pageNumber The selected page number.
     */
    void pageChanged(int pageNumber);
}
