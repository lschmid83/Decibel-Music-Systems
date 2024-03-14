package decibelrestjava.tables;

/**
 * Creates the album table model.
 */
@SuppressWarnings("serial")
public class AlbumTableModel extends ReadOnlyTableModel {

	public AlbumTableModel() {
        addColumn("Index");
        addColumn("Name");
        addColumn("Artists");
        addColumn("Label");
        addColumn("Length");
        addColumn("Track Count");
    }
}
