package decibelrestjava.tables;

/**
 * Creates the participant track appearances table model.
 */
@SuppressWarnings("serial")
public class ParticipantTrackAppearancesTableModel extends ReadOnlyTableModel {
   
	public ParticipantTrackAppearancesTableModel() {
        addColumn("Index");
        addColumn("Track Name");
        addColumn("Album Name");
        addColumn("Activity Name");        
    }     
}
