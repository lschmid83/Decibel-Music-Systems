package decibelrestjava.tables;

/**
 * Creates the track appearances table model.
 */
@SuppressWarnings("serial")
public class TrackAppearancesTableModel extends ReadOnlyTableModel {
    
    public TrackAppearancesTableModel() {
        addColumn("Index");
        addColumn("Track");        
        addColumn("Artists");
        addColumn("Album");
        addColumn("Track #");
        addColumn("Length");
    }    
    
}
