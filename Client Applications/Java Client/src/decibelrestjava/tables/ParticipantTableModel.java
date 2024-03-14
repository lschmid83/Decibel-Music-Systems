package decibelrestjava.tables;

/**
 * Creates the participant table model.
 */
@SuppressWarnings("serial")
public class ParticipantTableModel extends ReadOnlyTableModel {

	public ParticipantTableModel() {
        addColumn("Index");
        addColumn("Name");
        addColumn("Gender");
        addColumn("Date Born");
        addColumn("Date Died");
    }
}
