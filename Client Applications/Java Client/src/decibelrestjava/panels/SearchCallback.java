package decibelrestjava.panels;

/**
 * Interface which defines the SearchCallback functions.
 */
public interface SearchCallback {

    /**
     * Occurs when a search begins.
     *
     * @param url The REST request URL.
     */
    void beginSearch(String url);

    /**
     * Occurs when a search is complete.
     *
     * @param resultCount The number of results returned from the search.
     */
    void endSearch(int resultCount);

    /**
     * Occurs when the status text should be updated.
     *
     * @param text The status message.
     */
    void updateStatus(String text);
}
