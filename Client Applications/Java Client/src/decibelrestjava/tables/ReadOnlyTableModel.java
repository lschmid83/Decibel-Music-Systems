package decibelrestjava.tables;

import javax.swing.table.DefaultTableModel;

/**
 * This class extends 'DefaultTableModel' and overrides the isCellEditable() 
 * method to return false in all cases.
 */
@SuppressWarnings("serial")
public class ReadOnlyTableModel extends DefaultTableModel {

	@Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
