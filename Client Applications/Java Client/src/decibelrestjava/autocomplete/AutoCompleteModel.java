package decibelrestjava.autocomplete;

import decibelrestjava.webclient.DownloadStringCompletedEvent;
import decibelrestjava.webclient.WebClient;
import decibelrestjava.webclient.WebClientListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import queryresultholder.Suggestion;
import queryresultholder.SuggestionQueryResult;

/**
 * A data model for a JComboBox component which is populated with autocomplete
 * suggestions requested from the Decibel REST Web Service.
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class AutoCompleteModel extends AbstractListModel
        implements ComboBoxModel, KeyListener, ItemListener {

    /**
     * The JComboBox component which the model is attached to.
     */
    private JComboBox mComboBox;
    /**
     * The editor component used for JComboBox component.
     */
    private ComboBoxEditor mComboBoxEditor;
    /**
     * The autocomplete dictionary.
     */
    private String mDictionary;
    /**
     * The list of autocomplete suggestions.
     */
    private ArrayList<String> mData;
    /**
     * The selected item.
     */
    private String mSelectedItem;

    /**
     * Creates new AutoCompleteModel.
     *
     * @param comboBox The JComboBox component which the model is attached to.
     * @param dictionary The autocomplete dictionary.
     */
    public AutoCompleteModel(JComboBox comboBox, String dictionary) {

        mData = new ArrayList<>();
        mDictionary = dictionary;
        mComboBox = comboBox;
        mComboBoxEditor = comboBox.getEditor();
        initComboBoxEditor();

        // Initialize the autocomplete model with default values
        new Thread(
                new Runnable() {
            @Override
            public void run() {
                try {
                    if (mDictionary.equals("Barcodes")) {
                        requestAutocompleteSuggestions("0");
                    } else if (mDictionary.equals("Publishers") || mDictionary.equals("MusicCatalogues")) {
                        requestAutocompleteSuggestions("B");
                    } else {
                        requestAutocompleteSuggestions("A");
                    }
                } catch (UnsupportedEncodingException e) {
                }
            }
        }).start();
    }

    /**
     * Initialize the ComboBox editor.
     */
    private void initComboBoxEditor() {
        // Add the key listener to the text field that the combobox is wrapped around
        mComboBoxEditor.getEditorComponent().addKeyListener(this);
    }

    /**
     * Requests autocomplete suggestions from the Decibel Web Service and
     * populates the data model.
     *
     * @param text The text entered into the JComboBox component.
     */
    public void requestAutocompleteSuggestions(String text) throws UnsupportedEncodingException {

        // Construct the WebClient and set the request headers
        WebClient client = new WebClient();
        client.Headers.put("DecibelAppID", "5589c9d1");
        client.Headers.put("DecibelAppKey", "4154f53630c5cffd106cbe3ba0bd1eff");
        client.Headers.put("DecibelTimestamp",
                new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));

        // Set the callback function which is invoked when the download of the resource completes
        client.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                if (evt.Result != null) {

                    // Deserialize the response 
                    SuggestionQueryResult suggestionQueryResult;
                    try {
                        suggestionQueryResult = deserializeXmlString(
                                SuggestionQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        return;
                    }

                    // Clear the data list
                    mData.clear();

                    // Add the suggestion results to the data list
                    switch (mDictionary) {
                        case "Albums":
                            for (Suggestion suggestion : suggestionQueryResult.getAlbums().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Barcodes":
                            for (Suggestion suggestion : suggestionQueryResult.getBarcodes().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Participants":
                            for (Suggestion suggestion : suggestionQueryResult.getParticipants().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "MusicGenres":
                            for (Suggestion suggestion : suggestionQueryResult.getMusicGenres().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Publishers":
                            for (Suggestion suggestion : suggestionQueryResult.getPublishers().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Activities":
                            for (Suggestion suggestion : suggestionQueryResult.getActivities().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Tracks":
                            for (Suggestion suggestion : suggestionQueryResult.getTracks().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "GeoEntities":
                            for (Suggestion suggestion : suggestionQueryResult.getGeoEntities().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "Works":
                            for (Suggestion suggestion : suggestionQueryResult.getWorks().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "MusicCatalogues":
                            for (Suggestion suggestion : suggestionQueryResult.getMusicCatalogues().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                        case "WorksComposers":
                            for (Suggestion suggestion : suggestionQueryResult.getWorksComposers().getSuggestion()) {
                                mData.add(suggestion.getSuggestionValue());
                            }
                            break;
                    }
                }
            }
        });
        client.downloadStringAsync("http://decibel-rest-jazz.cloudapp.net/v1/AutoComplete?text="
                + URLEncoder.encode(text, "UTF-8") + "&dictionary=" + mDictionary + "&limit=5");
    }

    /**
     * Deserializes an XML string into an object of type T.
     *
     * @param <T> The type of object.
     * @param resultClass The result class.
     * @param str The XML string.
     * @return The object representation of the XML.
     */
    @SuppressWarnings("unchecked")
	public <T> T deserializeXmlString(Class<T> resultClass, String str) throws JAXBException {

        // Convert result string to InputSource
        InputSource is = new InputSource(new StringReader(str));

        // Create a JAXB context and unmarshaller
        JAXBContext context = JAXBContext.newInstance(resultClass);
        Unmarshaller handler = context.createUnmarshaller();

        // Deserialize the XML stream 
        return (T) handler.unmarshal(is);
    }

    /**
     * Updates the data the data model.
     *
     * @param text The text entered into the JComboBox component.
     */
    public void updateModel(String text) {

        // Request the autocomplete suggestions
        try {
            requestAutocompleteSuggestions(text);
        } catch (UnsupportedEncodingException e) {
        }

        // AbstractListModel subclasses must call this method after one or 
        // more elements of the list change
        super.fireContentsChanged(this, 0, mData.size());

        // Hide the popup list if the text box is empty
        if (text.length() == 0) {
            mComboBox.hidePopup();
        }
    }

    /**
     * Returns the number of items in the list.
     *
     * @return An integer equal to the number of items in the list.
     */
    @Override
    public int getSize() {
        return mData.size();
    }

    /**
     * Returns the list item at the specified index.
     *
     * @param index An integer indicating the list position, where the first
     * item starts at zero
     * @return The Object at that list position.
     */
    @Override
    public Object getElementAt(int index) {
        return mData.get(index);
    }

    /**
     * Sets the selected item in the combo box display area to the object in the
     * argument.
     *
     * @param anItem The list object to select; use null to clear the selection
     */
    @Override
    public void setSelectedItem(Object anItem) {
        mSelectedItem = (String) anItem;
    }

    /**
     * Returns the current selected item.
     *
     * @return The current selected Object.
     */
    @Override
    public Object getSelectedItem() {
        return mSelectedItem;
    }

    /**
     * Invoked when a key has been typed.
     *
     * @param e An event which indicates that a keystroke occurred in a
     * component.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Invoked when a key has been pressed.
     *
     * @param e An event which indicates that a keystroke occurred in a
     * component.
     */
    @Override
    public void keyPressed(KeyEvent e) {
    }

    /**
     * Invoked when a key has been released.
     *
     * @param e An event which indicates that a keystroke occurred in a
     * component.
     */
    @Override
    public void keyReleased(KeyEvent e) {

        String str = mComboBoxEditor.getItem().toString();
        JTextField jtf = (JTextField) mComboBoxEditor.getEditorComponent();
        if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
            updateModel(mComboBox.getEditor().getItem().toString());
            mComboBoxEditor.setItem(str);
            jtf.setCaretPosition(jtf.getCaretPosition());
        }
    }

    /**
     * Invoked when an item has been selected or deselected by the user.
     *
     * @param e A semantic event which indicates that an item was selected or
     * deselected
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        mComboBoxEditor.setItem(e.getItem().toString());
    }
}