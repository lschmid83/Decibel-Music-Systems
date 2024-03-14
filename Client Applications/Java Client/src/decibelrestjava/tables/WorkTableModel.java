package decibelrestjava.tables;

/**
 * Creates the work table model.
 */
@SuppressWarnings("serial")
public class WorkTableModel extends ReadOnlyTableModel {

    public WorkTableModel() {
        addColumn("Index");
        addColumn("Name");
        addColumn("Composers");
    }  
}
