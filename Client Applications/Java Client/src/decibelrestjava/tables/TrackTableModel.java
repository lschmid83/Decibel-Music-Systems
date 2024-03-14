package decibelrestjava.tables;

/**
 * Creates the album table model.
 */
@SuppressWarnings("serial")
public class TrackTableModel extends ReadOnlyTableModel {

    public TrackTableModel() {
        addColumn("Index");
        addColumn("Name");
        addColumn("Artists");
        addColumn("Track #");
        addColumn("Track Length");
    }
}
