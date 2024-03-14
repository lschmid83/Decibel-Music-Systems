package decibelrestjava.tables;

/**
 * Creates the track participants table model.
 */
@SuppressWarnings("serial")
public class TrackParticipantsTableModel extends ReadOnlyTableModel {
     
    public TrackParticipantsTableModel() {
        addColumn("Index");
        addColumn("Name");
        addColumn("Involvement");
    }   
}
