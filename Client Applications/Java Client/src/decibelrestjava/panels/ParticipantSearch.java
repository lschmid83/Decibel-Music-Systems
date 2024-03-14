package decibelrestjava.panels;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import decibelrestjava.autocomplete.AutoCompleteField;
import decibelrestjava.frames.SearchDepth;
import java.awt.Color;
import java.util.List;
import queryresultholder.Participant;
import queryresultholder.ParticipantAssociate;
import decibelrestjava.DecibelUrlBuilder;
import decibelrestjava.DecibelUrlBuilder.ParticipantSearchParam;
import decibelrestjava.webclient.WebClient;
import decibelrestjava.webclient.DownloadStringCompletedEvent;
import decibelrestjava.webclient.WebClientListener;
import decibelrestjava.tables.ParticipantTableModel;
import decibelrestjava.tables.ParticipantTrackAppearancesTableModel;
import decibelrestjava.xmlviewer.DOMUtil;
import decibelrestjava.xmlviewer.XMLTreeModel;
import java.awt.Dimension;
import java.io.StringReader;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import org.xml.sax.InputSource;
import queryresultholder.*;

/**
 * Searches for participants using the Decibel REST API.
 */
@SuppressWarnings("serial")
public class ParticipantSearch extends javax.swing.JPanel implements ISearchPanel, PageChangedCallback {

    /**
     * The collection of participants returned from the search.
     */
    private List<Participant> mParticipants;
    /**
     * The collection of participant associations returned from the search.
     */
    private List<ParticipantAssociate> mParticipantAssociations;
    /**
     * The participant search parameters.
     */
    private ParticipantSearchParam mSearchParam;
    /**
     * Stores the object which implements the search callback function.
     */
    private SearchCallback mSearchCallback;
    /**
     * The response string.
     */
    private String mResponse;
    /**
     * The participant search client.
     */
    private WebClient mSearchClient;
    /**
     * The participant associations client.
     */
    private WebClient mAssociatesClient;
    /**
     * The participant track appearances client.
     */
    private WebClient mAppearancesClient;
    /**
     * The participant search query result ID.
     */
    private String mQueryResultID;
    /**
     * The participant information search depth.
     */
    private String mParticipantDepth;

    /**
     * Creates new form ParticipantSearch.
     */
    public ParticipantSearch() {
        initComponents();
        initPanel();
    }

    /**
     * Initializes the panel.
     */
    @Override
    public void initPanel() {

        // Initialize the search parameters
        mSearchParam = new ParticipantSearchParam();
        mSearchParam.Depth = "Names;";

        // Clear the results
        initResultsTable();
        lstParticipantAssociates.setModel(new DefaultListModel<String>());
        initTrackAppearancesTable();
        trvParticipantResults.setModel(null);
        jTabbedPane3.setTitleAt(0, "Track Appearances");

        // Initialize the autocomplete fields
        initAutoComplete();

        // Initialize the list listeners
        initListListeners();

        // Initialize the search depth text field
        mParticipantDepth = "Relationships;Names;";
        txtSearchDepth.setText(Util.getSearchDepthTruncated(mParticipantDepth));
        txtSearchDepth.setEditable(false);

        // Initialize the search URL text field 
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
        txtSearchUrl.setLineWrap(true);
        txtSearchUrl.setEditable(false);
        txtSearchUrl.setBackground(Color.WHITE);

        // Initialize the date fields
        txtSearchDateBorn.setPreferredSize(new Dimension(10, 20));
        txtSearchDateBorn.setText(null);
        mSearchParam.DateBorn = txtSearchDateBorn.getText();

        txtSearchDateDied.setPreferredSize(new Dimension(10, 20));
        txtSearchDateDied.setText(null);
        mSearchParam.DateDied = txtSearchDateBorn.getText();
        
        // Register the TabbedPane event listeners
        ((TabbedPane)jTabbedPane1).initMouseListeners();
        ((TabbedPane)jTabbedPane1).initPageSelectedCallback(this);      
    }

    /**
     * Initializes the autocomplete fields.
     */
    @Override
    public void initAutoComplete() {

        ((AutoCompleteField)cmbSearchName).setDictionary("Participants");
        ((AutoCompleteField)cmbSearchActivity).setDictionary("Activities");
    }

    /**
     * Initializes the results table model.
     */
    @Override
    public void initResultsTable() {

        // Set the result table model
        tblParticipantResults.setModel(new ParticipantTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblParticipantResults, 0, 57);
        Util.setTableColumnWidth(tblParticipantResults, 1, 510);
        Util.setTableColumnWidth(tblParticipantResults, 2, 130);
        Util.setTableColumnWidth(tblParticipantResults, 3, 130);
        Util.setTableColumnWidth(tblParticipantResults, 4, 130);
        
        tblParticipantResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblParticipantResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the track appearances result table.
     */
    private void initTrackAppearancesTable() {
        // Set the track appearances table model
        tblTrackAppearances.setModel(new ParticipantTrackAppearancesTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblTrackAppearances, 0, 57);
        Util.setTableColumnWidth(tblTrackAppearances, 1, 200);
        Util.setTableColumnWidth(tblTrackAppearances, 2, 200);
        Util.setTableColumnWidth(tblTrackAppearances, 3, 200);
        
        tblTrackAppearances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblTrackAppearances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the list selection changed event listeners for the results
     * table and participant associations list box.
     */
    private void initListListeners() {

        // Register the result table selection changed listener
        tblParticipantResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * Occurs when the result table row selection changes.
             *
             * @param e Event arguments.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int sel = tblParticipantResults.getSelectedRow();
                if (sel > -1 && mParticipants != null && sel < mParticipants.size()) {
                    Participant participant = mParticipants.get(sel);
                    jTabbedPane3.setTitleAt(0, "Track Appearances [" + participant.getName() + "]");
                    jTabbedPane3.updateUI();
                    refreshParticipantAssociates(participant);
                    refreshParticipantTrackAppearances(participant);
                }
            }
        });

        // Register the participant associations list box selection changed listener
        lstParticipantAssociates.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * Occurs when the participant association selection changes.
             *
             * @param e Event arguments.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int sel = lstParticipantAssociates.getSelectedIndex();
                if (sel >= 0 && sel < mParticipantAssociations.size()) {
                    Participant participant = mParticipantAssociations.get(sel).getParticipant();
                    jTabbedPane3.setTitleAt(0, "Track Appearances [" + participant.getName() + "]");
                    jTabbedPane3.updateUI();
                    refreshParticipantTrackAppearances(participant);
                }
            }
        });
    }

    /**
     * Initializes the search callback function.
     *
     * @param callback The object which implements the search callback
     * functions.
     */
    @Override
    public void initSearchCallback(SearchCallback callback) {
        mSearchCallback = callback;
    }

    /**
     * Gets the participant search URL.
     *
     * @param searchParam The participant search parameters.
     * @return The participant search URL.
     */
    private String getParticipantSearchUrl(ParticipantSearchParam searchParam) {
        DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
        return urlBuilder.getParticipantSearchUrl(searchParam);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        lblSearchAlbumTitle = new javax.swing.JLabel();
        lblSearchBarcode = new javax.swing.JLabel();
        lblSearchActivity = new javax.swing.JLabel();
        lblSearchDateDied = new javax.swing.JLabel();
        lblSearchUrl = new javax.swing.JLabel();
        lblSearchParameters = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        lblSpacer = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSearchUrl = new javax.swing.JTextArea();
        cmbSearchName = new AutoCompleteField();
        cmbSearchActivity = new AutoCompleteField();
        lblSearchDepth = new javax.swing.JLabel();
        txtSearchDepth = new javax.swing.JTextField();
        btnSelectDepth = new javax.swing.JButton();
        txtSearchDateBorn = new datechooser.beans.DateChooserCombo();
        txtSearchDateDied = new datechooser.beans.DateChooserCombo();
        jTabbedPane1 = new TabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblParticipantResults = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstParticipantAssociates = new JList<String>();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblTrackAppearances = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        trvParticipantResults = new javax.swing.JTree();

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblSearchAlbumTitle.setText("Name");
        lblSearchAlbumTitle.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchAlbumTitle, gridBagConstraints);

        lblSearchBarcode.setText("Date Born");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchBarcode, gridBagConstraints);

        lblSearchActivity.setText("Activity");
        lblSearchActivity.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchActivity, gridBagConstraints);

        lblSearchDateDied.setText("Date Died");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchDateDied, gridBagConstraints);

        lblSearchUrl.setText("URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchUrl, gridBagConstraints);

        lblSearchParameters.setText("Search Parameters");
        lblSearchParameters.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel1.add(lblSearchParameters, gridBagConstraints);

        btnSearch.setText("Search");
        btnSearch.setPreferredSize(new java.awt.Dimension(80, 23));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(btnSearch, gridBagConstraints);

        lblSpacer.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 100, 0, 0);
        jPanel1.add(lblSpacer, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(60, 40));

        txtSearchUrl.setColumns(20);
        txtSearchUrl.setRows(5);
        jScrollPane4.setViewportView(txtSearchUrl);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(jScrollPane4, gridBagConstraints);

        cmbSearchName.setEditable(true);
        cmbSearchName.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchName.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchName.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchName, gridBagConstraints);

        cmbSearchActivity.setEditable(true);
        cmbSearchActivity.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchActivity.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchActivity.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchActivityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(cmbSearchActivity, gridBagConstraints);

        lblSearchDepth.setText("Depth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchDepth, gridBagConstraints);

        txtSearchDepth.setEditable(false);
        txtSearchDepth.setPreferredSize(new java.awt.Dimension(10, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(txtSearchDepth, gridBagConstraints);

        btnSelectDepth.setText("...");
        btnSelectDepth.setPreferredSize(new java.awt.Dimension(25, 20));
        btnSelectDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDepthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(btnSelectDepth, gridBagConstraints);

        txtSearchDateBorn.setCurrentView(new datechooser.view.appearance.AppearancesList("Swing",
            new datechooser.view.appearance.ViewAppearance("custom",
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    true,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 255),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(128, 128, 128),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(0, 0, 255),
                    false,
                    true,
                    new datechooser.view.appearance.swing.LabelPainter()),
                new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                    new java.awt.Color(0, 0, 0),
                    new java.awt.Color(255, 0, 0),
                    false,
                    false,
                    new datechooser.view.appearance.swing.ButtonPainter()),
                (datechooser.view.BackRenderer)null,
                false,
                true)));
    txtSearchDateBorn.setFormat(2);
    txtSearchDateBorn.setWeekStyle(datechooser.view.WeekDaysStyle.SHORT);
    try {
        txtSearchDateBorn.setDefaultPeriods(new datechooser.model.multiple.PeriodSet(new datechooser.model.multiple.Period(new java.util.GregorianCalendar(2013, 3, 17),
            new java.util.GregorianCalendar(2013, 3, 17))));
} catch (datechooser.model.exeptions.IncompatibleDataExeption e1) {
    e1.printStackTrace();
    }
    txtSearchDateBorn.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    txtSearchDateBorn.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            txtSearchDateBornOnCommit(evt);
        }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
    jPanel1.add(txtSearchDateBorn, gridBagConstraints);

    txtSearchDateDied.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    txtSearchDateDied.addCommitListener(new datechooser.events.CommitListener() {
        public void onCommit(datechooser.events.CommitEvent evt) {
            txtSearchDateDiedOnCommit(evt);
        }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
    jPanel1.add(txtSearchDateDied, gridBagConstraints);

    jSplitPane1.setDividerLocation(210);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setResizeWeight(1.0);

    tblParticipantResults.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {},
            {},
            {},
            {}
        },
        new String [] {

        }
    ));
    jScrollPane1.setViewportView(tblParticipantResults);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
    );
    jPanel4Layout.setVerticalGroup(
        jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
    );

    jSplitPane1.setTopComponent(jPanel4);

    jSplitPane2.setDividerLocation(300);
    jSplitPane2.setResizeWeight(0.5);

    jScrollPane3.setViewportView(lstParticipantAssociates);

    javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
    jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
    );
    jPanel6Layout.setVerticalGroup(
        jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
    );

    jTabbedPane2.addTab("Collaborations", jPanel6);

    jSplitPane2.setLeftComponent(jTabbedPane2);

    tblTrackAppearances.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null},
            {null, null, null, null}
        },
        new String [] {
            "Title 1", "Title 2", "Title 3", "Title 4"
        }
    ));
    jScrollPane6.setViewportView(tblTrackAppearances);

    javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
    jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
    );
    jPanel7Layout.setVerticalGroup(
        jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
    );

    jTabbedPane3.addTab("Track Appearances", jPanel7);

    jSplitPane2.setRightComponent(jTabbedPane3);

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jSplitPane2)
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jSplitPane2)
    );

    jSplitPane1.setRightComponent(jPanel5);

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jSplitPane1)
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jSplitPane1)
    );

    jTabbedPane1.addTab("Results", jPanel2);

    javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
    trvParticipantResults.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
    jScrollPane5.setViewportView(trvParticipantResults);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
    );

    jTabbedPane1.addTab("XML", jPanel3);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jTabbedPane1)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jTabbedPane1))
    );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Occurs when the search button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        search(getParticipantSearchUrl(mSearchParam));
        ((TabbedPane)jTabbedPane1).setPageNumber(1);
    }//GEN-LAST:event_btnSearchActionPerformed

    /**
     * Begins the search.
     *
     * @param url The REST request URL.
     */
    @Override
    public void search(String url) {

        initResultsTable();
        initTrackAppearancesTable();
        lstParticipantAssociates.setModel(new DefaultListModel<String>());

        mResponse = "";
        mParticipants = null;

        mSearchCallback.beginSearch(url);

        if (mSearchClient != null) {
            mSearchClient.stop();
        }
        mSearchClient = new WebClient();
        Util.setDecibelRequestHeaders(mSearchClient);
        ((TabbedPane)jTabbedPane1).setPageCount(1);

        // Register the WebClient downloadStringCompleted event     
        mSearchClient.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                int count = 0;
                mResponse = evt.Result;

                if (evt.Error == null) {
                    
                    // Deserialize the response
                    ParticipantQueryResult participantQueryResult;
                    try {
                        participantQueryResult = Util.<ParticipantQueryResult>deserializeXmlString(ParticipantQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        tblParticipantResults.setModel(null);
                        return;
                    }
                    mParticipants = participantQueryResult.getResultSet().getParticipant();
              
                    // Store the query result ID for paged results
                    if (((TabbedPane)jTabbedPane1).getCurrentPage() == 1) {
                        mQueryResultID = participantQueryResult.getQueryResultID();
                    }

                    // Initialize the page navigator
                    if (participantQueryResult.getPages() > 1) {
                        ((TabbedPane)jTabbedPane1).setPageCount(participantQueryResult.getPages());
                    }

                    // Initialize the results table
                    DefaultTableModel model = (DefaultTableModel) tblParticipantResults.getModel();
                    model.getDataVector().removeAllElements();

                    for (Participant participant : mParticipants) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = participant.getName();
                        row[2] = participant.getGender();
                        row[3] = participant.getDateBorn().getName();
                        row[4] = participant.getDateDied().getName();
                        model.addRow(row);
                        count++;
                    }
                    tblParticipantResults.setModel(model);

                    // Initialize the XML results tree view
                    try {
                        InputSource source = new InputSource(new StringReader(evt.Result));
                        XMLTreeModel xmlTree = new XMLTreeModel(DOMUtil.createDocument(source));
                        trvParticipantResults.setModel(xmlTree);
                        trvParticipantResults.expandRow(0);
                        trvParticipantResults.expandRow(1);
                        trvParticipantResults.expandRow(9);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Unable to initialize XML result tree view." 
                                + System.lineSeparator() + e.toString());
                        trvParticipantResults.setModel(null);
                    }

                    tblParticipantResults.setRowSelectionInterval(0, 0);
                    tblParticipantResults.requestFocus();
                } else {
                    mSearchCallback.endSearch(count);
                    jTabbedPane3.setTitleAt(0, "Track Appearances");
                    jTabbedPane3.updateUI();
                }

            }
        });
        mSearchClient.downloadStringAsync(url);
    }

    /**
     * Occurs when a page is selected from the result set.
     *
     * @param pageNumber The page number.
     */
    @Override
    public void pageChanged(int pageNumber) {
        if (mQueryResultID != null) {
            String url = Settings.ApiAddress + "Participants/Pages/" + mQueryResultID + "?pageNumber=" + (pageNumber - 1);
            search(url);
        }
    }

    /**
     * Refreshes the participant associates information.
     *
     * @param index The Participant object.
     */
    private void refreshParticipantAssociates(Participant participant) {

        lstParticipantAssociates.setModel(new DefaultListModel<String>());

        String url = Settings.ApiAddress + "Participants/" + participant.getID() + "/Associates";
        mSearchCallback.beginSearch(url);

        if (mAssociatesClient != null) {
            mAssociatesClient.stop();
        }
        mAssociatesClient = new WebClient();
        Util.setDecibelRequestHeaders(mAssociatesClient);

        // Register the WebClient downloadStringCompleted event
        mAssociatesClient.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                if (evt.Error == null) {
                    
                    // Deserialize the response
                    ParticipantAssociateQueryResult participantAssociateQueryResult;
                    try {
                        participantAssociateQueryResult = Util.<ParticipantAssociateQueryResult>deserializeXmlString(ParticipantAssociateQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }                  
                    
                    DefaultListModel<String> list = new DefaultListModel<>();
                    mParticipantAssociations = participantAssociateQueryResult.getResultSet().getParticipantAssociate();
                    for (ParticipantAssociate participantAssociate : mParticipantAssociations) {
                        Participant participant = participantAssociate.getParticipant();
                        if (!participant.getName().isEmpty()) {
                            list.addElement(participant.getName());
                        }
                    }
                    lstParticipantAssociates.setModel(list);
                }
            }
        });
        mAssociatesClient.downloadStringAsync(url);
    }

    /**
     * Refreshes the participant track appearances.
     *
     * @param index The participant object.
     */
    private void refreshParticipantTrackAppearances(Participant participant) {

        initTrackAppearancesTable();

        String url = Settings.ApiAddress + "Participants/" + participant.getID() + "/Tracks";

        mSearchCallback.beginSearch(url);

        if (mAppearancesClient != null) {
            mAppearancesClient.stop();
        }
        mAppearancesClient = new WebClient();
        Util.setDecibelRequestHeaders(mAppearancesClient);

        // Register the WebClient downloadStringCompleted event
        mAppearancesClient.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                int count = 0;
                mResponse = evt.Result;

                if (evt.Error == null) {

                    // Deserialize the response
                    ParticipantTrackAppearanceQueryResult participantTrackAppearanceQueryResult;
                    try {
                        participantTrackAppearanceQueryResult = Util.<ParticipantTrackAppearanceQueryResult>deserializeXmlString(ParticipantTrackAppearanceQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }   

                    // Initialise the results table
                    DefaultTableModel model = (DefaultTableModel) tblTrackAppearances.getModel();
                    model.getDataVector().removeAllElements();

                    for (TrackAppearance trackAppearance : participantTrackAppearanceQueryResult.getResultSet().getTrackAppearance()) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = trackAppearance.getTrackName();
                        row[2] = trackAppearance.getAlbumName();
                        row[3] = trackAppearance.getActivityName();
                        model.addRow(row);
                        count++;
                    }
                    tblTrackAppearances.setModel(model);
                }
                mSearchCallback.endSearch(count);
                btnSearch.setEnabled(true);
            }
        });
        mAppearancesClient.downloadStringAsync(url);
    }

    /**
     * Gets the default button on the panel.
     *
     * @return The default button.
     */
    @Override
    public JButton getDefaultButton() {
        return btnSearch;
    }

    /**
     * Refreshes the search URL.
     */
    public void refreshSearchUrl() {
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
    }

    /**
     * Gets the response string.
     *
     * @return The response string.
     */
    @Override
    public String getResponse() {
        return mResponse;
    }

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchNameActionPerformed
        mSearchParam.Name = ((AutoCompleteField)cmbSearchName).getText();
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchNameActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchActivityActionPerformed
        mSearchParam.Activity = ((AutoCompleteField)cmbSearchActivity).getText();
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchActivityActionPerformed

    /**
     * Occurs when the select depth button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnSelectDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDepthActionPerformed
        List<String> selections = Arrays.asList(mParticipantDepth.split(";"));
        List<String> suggestions = SearchDepth.getParticipantDepthParameters();
        SearchDepth searchDepth = new SearchDepth((JFrame)SwingUtilities.getWindowAncestor(this), true);
        int ret = searchDepth.showDialog(suggestions, selections);
        if (ret == 0) {
            mParticipantDepth = searchDepth.getSelectedItems();
            txtSearchDepth.setText(Util.getSearchDepthTruncated(mParticipantDepth));
        }      
    }//GEN-LAST:event_btnSelectDepthActionPerformed

    /**
     * Occurs when a date is selected.
     * 
     * @param evt Event arguments.
     */
    private void txtSearchDateBornOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_txtSearchDateBornOnCommit
        mSearchParam.DateBorn = txtSearchDateBorn.getText();
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
    }//GEN-LAST:event_txtSearchDateBornOnCommit

    /**
     * Occurs when a date is selected.
     * 
     * @param evt Event arguments.
     */
    private void txtSearchDateDiedOnCommit(datechooser.events.CommitEvent evt) {//GEN-FIRST:event_txtSearchDateDiedOnCommit
        mSearchParam.DateDied = txtSearchDateDied.getText();
        txtSearchUrl.setText(getParticipantSearchUrl(mSearchParam));
    }//GEN-LAST:event_txtSearchDateDiedOnCommit
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelectDepth;
    private javax.swing.JComboBox<Object> cmbSearchActivity;
    private javax.swing.JComboBox<Object> cmbSearchName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblSearchActivity;
    private javax.swing.JLabel lblSearchAlbumTitle;
    private javax.swing.JLabel lblSearchBarcode;
    private javax.swing.JLabel lblSearchDateDied;
    private javax.swing.JLabel lblSearchDepth;
    private javax.swing.JLabel lblSearchParameters;
    private javax.swing.JLabel lblSearchUrl;
    private javax.swing.JLabel lblSpacer;
    private javax.swing.JList<String> lstParticipantAssociates;
    private javax.swing.JTable tblParticipantResults;
    private javax.swing.JTable tblTrackAppearances;
    private javax.swing.JTree trvParticipantResults;
    private datechooser.beans.DateChooserCombo txtSearchDateBorn;
    private datechooser.beans.DateChooserCombo txtSearchDateDied;
    private javax.swing.JTextField txtSearchDepth;
    private javax.swing.JTextArea txtSearchUrl;
    // End of variables declaration//GEN-END:variables
}
