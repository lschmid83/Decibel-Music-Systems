package decibelrestjava.panels;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import decibelrestjava.autocomplete.AutoCompleteField;
import decibelrestjava.frames.SearchDepth;
import decibelrestjava.frames.TrackParticipants;
import decibelrestjava.DecibelUrlBuilder;
import decibelrestjava.DecibelUrlBuilder.TrackSearchParam;
import decibelrestjava.webclient.WebClient;
import decibelrestjava.webclient.DownloadStringCompletedEvent;
import decibelrestjava.webclient.WebClientListener;
import decibelrestjava.tables.TrackTableModel;
import decibelrestjava.xmlviewer.DOMUtil;
import decibelrestjava.xmlviewer.XMLTreeModel;
import java.awt.Color;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBException;
import org.xml.sax.InputSource;
import queryresultholder.*;

/**
 * Searches for tracks using the Decibel REST API.
 */
@SuppressWarnings("serial")
public class TrackSearch extends javax.swing.JPanel implements ISearchPanel, DiscChangedCallback, PageChangedCallback {

    /**
     * The collection of tracks returned from the search.
     */
    private List<Track> mTracks;
    /**
     * The selected album with specified retrieval depth.
     */
    private Album mAlbum;
    /**
     * The collection of tracks for the selected album.
     */
    private List<Track> mTrackList;
    /**
     * The track search parameters.
     */
    private TrackSearchParam mSearchParam;
    /**
     * Stores the object which implements the search callback functions.
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
     * The album information search client.
     */
    private WebClient mAlbumClient;
    /**
     * The track search query result ID.
     */
    private String mQueryResultID;
    /**
     * The track search depth.
     */
    private String mTrackDepth;

    /**
     * Creates new form TrackSearch.
     */
    public TrackSearch() {
        initComponents();
        initPanel();
    }

    /**
     * Initializes the panel.
     */
    @Override
    public void initPanel() {

        // Initialize the search parameters
        mSearchParam = new TrackSearchParam();
        mSearchParam.Depth = "Names;";

        // Clear the results
        initResultsTable();
        albumInformation.clearAlbumInformation();
        lstTracks.setModel(new DefaultListModel<String>());
        trvTrackResults.setModel(null);

        // Initialize the autocomplete fields
        initAutoComplete();

        // Initialize the list listeners
        initListListeners();

        // Initialize the search depth text field
        mTrackDepth = "Participations;ExternalIdentifiers;Genres;Names;Performances";
        txtSearchDepth.setText(Util.getSearchDepthTruncated(mTrackDepth));
        txtSearchDepth.setEditable(false);

        // Initialize the search URL text field 
        txtSearchUrl.setText(getTrackSearchUrl(mSearchParam));
        txtSearchUrl.setLineWrap(true);
        txtSearchUrl.setEditable(false);
        txtSearchUrl.setBackground(Color.WHITE);
       
        // Register the AlbumInformation event listeners
        ((TabbedPane)jTabbedPane1).initMouseListeners();
        albumInformation.initDiscChangedCallback(this);
        
        // Register the TabbedPane event listeners
        ((TabbedPane)jTabbedPane1).initMouseListeners();
        ((TabbedPane)jTabbedPane1).initPageSelectedCallback(this);      
    }

    /**
     * Initializes the autocomplete fields.
     */
    @Override
    public void initAutoComplete() {
        ((AutoCompleteField)cmbSearchTrackTitle).setDictionary("Tracks");
        ((AutoCompleteField)cmbSearchArtist).setDictionary("Participants");
        ((AutoCompleteField)cmbSearchAlbumTitle).setDictionary("Albums");
    }

    /**
     * Initializes the result table model.
     */
    @Override
    public void initResultsTable() {

        // Create the result table model
        tblTrackResults.setModel(new TrackTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblTrackResults, 0, 57);
        Util.setTableColumnWidth(tblTrackResults, 1, 330);
        Util.setTableColumnWidth(tblTrackResults, 2, 330);
        Util.setTableColumnWidth(tblTrackResults, 3, 125);
        Util.setTableColumnWidth(tblTrackResults, 4, 125);
        
        tblTrackResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblTrackResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the list selection changed event listeners for the result
     * table.
     */
    private void initListListeners() {

        // Register the result table selection changed listener
        tblTrackResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * Occurs when the result table row selection changes.
             *
             * @param e Event arguments.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int sel = tblTrackResults.getSelectedRow();
                if (sel > -1 && mTracks != null && sel < mTracks.size()) {
                    refreshAlbumInformation(mTracks.get(sel));
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
     * Gets the track search URL.
     *
     * @param searchParam The track search parameters.
     * @return The track search URL.
     */
    private String getTrackSearchUrl(TrackSearchParam searchParam) {
        DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
        return urlBuilder.getTrackSearchUrl(searchParam);
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
        lblSearchTrackTitle = new javax.swing.JLabel();
        lblSearchAlbumTitle = new javax.swing.JLabel();
        lblSearchArtist = new javax.swing.JLabel();
        lblSearchUrl = new javax.swing.JLabel();
        lblSearchParameters = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        lblSpacer = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSearchUrl = new javax.swing.JTextArea();
        cmbSearchTrackTitle = new AutoCompleteField();
        cmbSearchArtist = new AutoCompleteField();
        lblSearchDepth = new javax.swing.JLabel();
        txtSearchDepth = new javax.swing.JTextField();
        btnSelectDepth = new javax.swing.JButton();
        cmbSearchAlbumTitle = new AutoCompleteField();
        jTabbedPane1 = new TabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTrackResults = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        albumInformation = new decibelrestjava.panels.AlbumInformation();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstTracks = new JList<String>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        trvTrackResults = new javax.swing.JTree();

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblSearchTrackTitle.setText("Track Title");
        lblSearchTrackTitle.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchTrackTitle, gridBagConstraints);

        lblSearchAlbumTitle.setText("Album Title");
        lblSearchAlbumTitle.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchAlbumTitle, gridBagConstraints);

        lblSearchArtist.setText("Artist");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchArtist, gridBagConstraints);

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

        cmbSearchTrackTitle.setEditable(true);
        cmbSearchTrackTitle.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchTrackTitle.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchTrackTitle.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchTrackTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchTrackTitleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchTrackTitle, gridBagConstraints);

        cmbSearchArtist.setEditable(true);
        cmbSearchArtist.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchArtist.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchArtist.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchArtist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchArtistActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchArtist, gridBagConstraints);

        lblSearchDepth.setText("Depth");
        lblSearchDepth.setPreferredSize(new java.awt.Dimension(70, 14));
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

        cmbSearchAlbumTitle.setEditable(true);
        cmbSearchAlbumTitle.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchAlbumTitle.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchAlbumTitle.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchAlbumTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchAlbumTitleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchAlbumTitle, gridBagConstraints);

        jSplitPane1.setDividerLocation(210);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);

        tblTrackResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblTrackResults);

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

        jSplitPane2.setDividerLocation(500);
        jSplitPane2.setResizeWeight(0.5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(albumInformation, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(albumInformation, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 142, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Album Information", jPanel6);

        jSplitPane2.setLeftComponent(jTabbedPane2);

        lstTracks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstTracksMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lstTracksMouseExited(evt);
            }
        });
        lstTracks.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                lstTracksMouseMoved(evt);
            }
        });
        jScrollPane2.setViewportView(lstTracks);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
        );

        jTabbedPane3.addTab("Track Listing", jPanel7);

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
        trvTrackResults.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane5.setViewportView(trvTrackResults);

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
        search(getTrackSearchUrl(mSearchParam));
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
        albumInformation.clearAlbumInformation();
        lstTracks.setModel(new DefaultListModel<String>());

        mResponse = "";
        mTracks = null;

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
                    TrackQueryResult trackQueryResult;
                    try {
                        trackQueryResult = Util.<TrackQueryResult>deserializeXmlString(TrackQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }                     

                    mTracks = trackQueryResult.getResultSet().getTrack();

                    // Store the query result ID for paged results
                    if (((TabbedPane)jTabbedPane1).getCurrentPage() == 1) {
                        mQueryResultID = trackQueryResult.getQueryResultID();
                    }

                    // Initialize the page navigator
                    if (trackQueryResult.getPages() > 1) {
                        ((TabbedPane)jTabbedPane1).setPageCount(trackQueryResult.getPages());
                    }

                    // Initialise the results table
                    DefaultTableModel model = (DefaultTableModel) tblTrackResults.getModel();
                    model.getDataVector().removeAllElements();

                    for (Track track : mTracks) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = track.getName();
                        row[2] = track.getArtists();
                        row[3] = track.getSequenceNo();
                        row[4] = Util.formatTime(track.getTotalSeconds().intValue());
                        model.addRow(row);
                        count++;
                    }
                    tblTrackResults.setModel(model);

                    // Initialize the XML results tree view
                    try {
                        InputSource source = new InputSource(new StringReader(evt.Result));
                        XMLTreeModel xmlTree = new XMLTreeModel(DOMUtil.createDocument(source));
                        trvTrackResults.setModel(xmlTree);
                        trvTrackResults.expandRow(0);
                        trvTrackResults.expandRow(1);
                        trvTrackResults.expandRow(9);
                    } catch (Exception e) {
                         JOptionPane.showMessageDialog(null, "Unable to initialize XML result tree view." 
                                + System.lineSeparator() + e.toString());
                        trvTrackResults.setModel(null);
                    }

                    tblTrackResults.setRowSelectionInterval(0, 0);
                    tblTrackResults.requestFocus();
                } else {
                    mSearchCallback.endSearch(count);
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
            String url = Settings.ApiAddress + "Tracks/Pages/" + mQueryResultID + "?pageNumber=" + (pageNumber - 1);
            search(url);
        }
    }

    /**
     * Refresh the album information.
     *
     * @param index The Track object.
     */
    private void refreshAlbumInformation(Track track) {

        albumInformation.clearAlbumInformation();
        lstTracks.setModel(new DefaultListModel<String>());

        String url = Settings.ApiAddress + "Albums/" + "?depth=" + mTrackDepth + ";Tracks;Publications;Media;ImageThumbnail;&id=" + track.getAlbumID();
        mSearchCallback.beginSearch(url);

        if (mAlbumClient != null) {
            mAlbumClient.stop();
        }
        mAlbumClient = new WebClient();
        Util.setDecibelRequestHeaders(mAlbumClient);

        // Register the WebClient downloadStringCompleted event
        mAlbumClient.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                mResponse = evt.Result;

                if (evt.Error == null) {
                    
                    // Deserialize the response
                    AlbumQueryResult albumQueryResult;
                    try {
                        albumQueryResult = Util.<AlbumQueryResult>deserializeXmlString(AlbumQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }   
                    mAlbum = albumQueryResult.getResultSet().getAlbum().get(0);

                    albumInformation.setAlbumInformation(mAlbum);
                    jTabbedPane3.updateUI();                   
                }
                mSearchCallback.endSearch(1);
                btnSearch.setEnabled(true);
            }
        });

        mAlbumClient.downloadStringAsync(url);
    }

    /**
     * Refreshes the track information for the album and disc number.
     *
     * @param album The Album object.
     * @param discNumber The disc number.
     */
    private void refreshTrackInformation(Album album, String discNumber) {

        mTrackList = album.getTracks().getTrack();
        
        DefaultListModel<String> list = new DefaultListModel<>();
        for (Track track : album.getTracks().getTrack()) {
            if (track.getDiscNumber().equals(discNumber)) {
                String trackInfo = track.getSequenceNo() + ") " + track.getName();
                int duration = track.getTotalSeconds().intValue();
                if (duration > 0) {
                    trackInfo += " (" + Util.formatTime(duration) + ")";
                }
                list.addElement(trackInfo);
            }
        }
        lstTracks.setModel(list);
    }

    /**
     * Occurs when the album information disc selection changes.
     *
     * @param discNumber The disc number.
     */
    @Override
    public void discChanged(String discNumber) {
        refreshTrackInformation(mAlbum, discNumber);
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
        txtSearchUrl.setText(getTrackSearchUrl(mSearchParam));
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
    private void cmbSearchTrackTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchTrackTitleActionPerformed
        mSearchParam.TrackTitle = ((AutoCompleteField)cmbSearchTrackTitle).getText();
        txtSearchUrl.setText(getTrackSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchTrackTitleActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchArtistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchArtistActionPerformed
        mSearchParam.Artist = ((AutoCompleteField)cmbSearchArtist).getText();
        txtSearchUrl.setText(getTrackSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchArtistActionPerformed

    /**
     * Occurs when the select depth button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnSelectDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDepthActionPerformed
        List<String> selections = Arrays.asList(mTrackDepth.split(";"));
        List<String> suggestions = SearchDepth.getTrackDepthParameters();
        SearchDepth searchDepth = new SearchDepth((JFrame)SwingUtilities.getWindowAncestor(this), true);
        int ret = searchDepth.showDialog(suggestions, selections);
        if (ret == 0) {
            mTrackDepth = searchDepth.getSelectedItems();
            txtSearchDepth.setText(Util.getSearchDepthTruncated(mTrackDepth));
        }
    }//GEN-LAST:event_btnSelectDepthActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */   
    private void cmbSearchAlbumTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchAlbumTitleActionPerformed
        mSearchParam.AlbumTitle = ((AutoCompleteField)cmbSearchAlbumTitle).getText();
        txtSearchUrl.setText(getTrackSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchAlbumTitleActionPerformed

    /**
     * Occurs when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     *
     * @param e Event arguments.
     */   
    private void lstTracksMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstTracksMouseMoved
        if (lstTracks.getModel().getSize() > 0) {
            mSearchCallback.updateStatus("Double click on a track to view participants information");
        }
    }//GEN-LAST:event_lstTracksMouseMoved

    /**
     * Occurs when the mouse exits a component.
     *
     * @param e Event arguments.
     */   
    private void lstTracksMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstTracksMouseExited
        mSearchCallback.updateStatus("Ready");
    }//GEN-LAST:event_lstTracksMouseExited

    /**
     * Occurs when a track is clicked in the track list.
     *
     * @param evt Event arguments.
     */   
    private void lstTracksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstTracksMouseClicked
        
        if (lstTracks.getModel().getSize() > 0) {
            if (evt.getClickCount() == 2 && !evt.isConsumed()) {
                evt.consume();
                TrackParticipants trackParticipants = new TrackParticipants((JFrame)SwingUtilities.getWindowAncestor(this), true);
                trackParticipants.showDialog(mTrackList.get(lstTracks.getSelectedIndex()));
            }
        }
        
    }//GEN-LAST:event_lstTracksMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private decibelrestjava.panels.AlbumInformation albumInformation;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelectDepth;
    private javax.swing.JComboBox<Object> cmbSearchAlbumTitle;
    private javax.swing.JComboBox<Object> cmbSearchArtist;
    private javax.swing.JComboBox<Object> cmbSearchTrackTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel lblSearchAlbumTitle;
    private javax.swing.JLabel lblSearchArtist;
    private javax.swing.JLabel lblSearchDepth;
    private javax.swing.JLabel lblSearchParameters;
    private javax.swing.JLabel lblSearchTrackTitle;
    private javax.swing.JLabel lblSearchUrl;
    private javax.swing.JLabel lblSpacer;
    private javax.swing.JList<String> lstTracks;
    private javax.swing.JTable tblTrackResults;
    private javax.swing.JTree trvTrackResults;
    private javax.swing.JTextField txtSearchDepth;
    private javax.swing.JTextArea txtSearchUrl;
    // End of variables declaration//GEN-END:variables
}
