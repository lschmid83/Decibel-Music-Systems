package decibelrestjava.autocomplete;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Subclass of the JComboBox which provides autocomplete functionality.
 */
@SuppressWarnings("serial")
public class AutoCompleteField extends JComboBox<Object> implements KeyListener {

    /**
     * The data model which is populated with autocomplete suggestions.
     */
    private AutoCompleteModel mAutoCompleteModel;

    /**
     * AutoCompleteField constructor.
     */
    public AutoCompleteField() {

        // Make the combobox editable
        this.setEditable(true);
    }

    /**
     * Sets the name of the dictionary used by the AutoComplete model to request
     * autocomplete suggestions.
     *
     * @param dictionary The dictionary name.
     */
    @SuppressWarnings("unchecked")
	public void setDictionary(String dictionary) {
        // Create the model
        this.mAutoCompleteModel = new AutoCompleteModel(this, dictionary);

        // Set the model on the combobox
        this.setModel(mAutoCompleteModel);

        // Set the model as the item listener also
        this.addItemListener(mAutoCompleteModel);

        // Register the editor pane key listener to catch all key events for combo box
        // For an editable combo box, it's the editor component that gets the focus
        this.getEditor().getEditorComponent().addKeyListener(this);
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
        this.fireActionEvent();
    }

    /**
     * Returns the text contained in combo box editor component.
     *
     * @return The combo box text.
     */
    public String getText() {
        return ((JTextField)this.getEditor().getEditorComponent()).getText();
    }

    /**
     * Sets the text contained in combo box editor component.
     *
     * @return The combo box text.
     */
    public void setText(String text) {
        ((JTextField)this.getEditor().getEditorComponent()).setText(text);
    }
}
