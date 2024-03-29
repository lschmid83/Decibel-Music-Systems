package decibelrestjava.frames;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog box for selecting the retrieval depth.
 */
@SuppressWarnings("serial")
public class SearchDepth extends javax.swing.JDialog {

    /**
     * The selected option 0=OK, 1=Cancel.
     */
    private int mReturnValue;

    /**
     * Creates new form SearchDepth.
     */
    public SearchDepth(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        mReturnValue = 1;
    }

    /**
     * Shows the select search depth dialog.
     *
     * @param suggestions The depth suggestions.
     * @param selections The selected depths.
     * @return The selected option 0=OK, 1=Cancel.
     */
    public int showDialog(List<String> suggestions, List<String> selections) {

        // Add the suggestions to the list and set the checked state
        for (String suggestion : suggestions) {
            if (selections.contains(suggestion)) {
                lstDepth.add(suggestion, true);
            } else {
                lstDepth.add(suggestion, false);
            }
        }
        this.setVisible(true);

        return mReturnValue;
    }

    /**
     * Gets a string containing the selected depth values.
     *
     * @return The selected depth values.
     */
    public String getSelectedItems() {
        // Add the selected items to the return string
        String ret = "";
        for (String selection : lstDepth.SelectedItems()) {
            ret += selection + ";";
        }
        return ret;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblDescription = new javax.swing.JLabel();
        lstDepth = new creator.components.CheckBoxList();
        jPanel3 = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select Depth");
        setResizable(false);

        lblDescription.setText("Please select the depth of information to bring back.");

        jPanel3.setLayout(new java.awt.GridBagLayout());

        btnOK.setText("OK");
        btnOK.setPreferredSize(new java.awt.Dimension(73, 23));
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel3.add(btnOK, gridBagConstraints);

        btnCancel.setText("Cancel");
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel3.add(btnCancel, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDescription)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lstDepth, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDescription)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lstDepth, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(502, 374));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Occurs when the OK button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        mReturnValue = 0;
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    /**
     * Occurs when the cancel button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        mReturnValue = 1;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                SearchDepth dialog = new SearchDepth(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    /**
     * Gets a list of the album depth parameters.
     *
     * @return The list of album depth parameters.
     */
    public static List<String> getAlbumDepthParameters() {
        List<String> albumDepth = new ArrayList<>();
        albumDepth.add("Tracks");
        albumDepth.add("Names");
        albumDepth.add("ExternalIdentifiers");
        albumDepth.add("Publications");
        albumDepth.add("Genres");
        albumDepth.add("Media");
        albumDepth.add("ImageThumbnail");
        return albumDepth;
    }

    /**
     * Gets a list of the participant depth parameters.
     *
     * @return The list of participant depth parameters.
     */
    public static List<String> getParticipantDepthParameters() {
        List<String> participantDepth = new ArrayList<>();
        participantDepth.add("Names");
        participantDepth.add("GroupMembers");
        participantDepth.add("Nationalities");
        participantDepth.add("GeographicAreas");
        participantDepth.add("Annotations");
        participantDepth.add("ChartsAwards");
        participantDepth.add("Relationships");
        return participantDepth;
    }

    /**
     * Gets a list of the track depth parameters.
     *
     * @return The list of track depth parameters.
     */
    public static List<String> getTrackDepthParameters() {
        List<String> trackDepth = new ArrayList<>();
        trackDepth.add("Names");
        trackDepth.add("Participations");
        trackDepth.add("ExternalIdentifiers");
        trackDepth.add("Genres");
        trackDepth.add("Performances");
        return trackDepth;
    }
    
     /**
     * Gets a list of the work depth parameters.
     *
     * @return The list of work depth parameters.
     */
    public static List<String> getWorkDepthParameters() {
        List<String> trackDepth = new ArrayList<>();
        trackDepth.add("Annotations");
        trackDepth.add("ChartsAwards");
        trackDepth.add("Genres");
        trackDepth.add("Movements");
        trackDepth.add("Names");
        trackDepth.add("Nationalities");
        trackDepth.add("Publications");
        trackDepth.add("Publishers");
        return trackDepth;
    }   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblDescription;
    private creator.components.CheckBoxList lstDepth;
    // End of variables declaration//GEN-END:variables
}
