package decibelrestjava.panels;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import decibelrestjava.autocomplete.AutoCompleteField;
import decibelrestjava.frames.SearchDepth;
import decibelrestjava.frames.TrackParticipants;
import decibelrestjava.DecibelUrlBuilder;
import decibelrestjava.DecibelUrlBuilder.AlbumSearchParam;
import decibelrestjava.webclient.WebClient;
import decibelrestjava.webclient.DownloadStringCompletedEvent;
import decibelrestjava.webclient.WebClientListener;
import decibelrestjava.tables.AlbumTableModel;
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
import queryresultholder.Album;
import queryresultholder.AlbumQueryResult;
import queryresultholder.Track;

/**
 * Searches for albums using the Decibel REST API.
 */
@SuppressWarnings("serial")
public class AlbumSearch extends javax.swing.JPanel implements ISearchPanel, DiscChangedCallback, PageChangedCallback {

    /**
     * The collection of albums returned from the search.
     */
    private List<Album> mAlbums;
    /**
     * The selected album with specified retrieval depth.
     */
    private Album mAlbum;
    /**
     * The track information for the selected album.
     */
    private List<Track> mTrackList;
    /**
     * The album search parameters.
     */
    private AlbumSearchParam mSearchParam;
    /**
     * Stores the object which implements the search callback functions.
     */
    private SearchCallback mSearchCallback;
    /**
     * The response string.
     */
    private String mResponse;
    /**
     * The album search client.
     */
    private WebClient mSearchClient;
    /**
     * The album information search client.
     */
    private WebClient mAlbumClient;
    /**
     * The album search query result ID.
     */
    private String mQueryResultID;
    /**
     * The album information search depth.
     */
    private String mAlbumDepth;

    /**
     * Creates new form AlbumSearch.
     */
    public AlbumSearch() {
        initComponents();
        initPanel();
    }

    /**
     * Initializes the panel.
     */
    @Override
    public void initPanel() {

        // Initialize the search parameters
        mSearchParam = new AlbumSearchParam();
        mSearchParam.Depth = "Publications;";

        // Clear the results
        initResultsTable();
        albumInformation.clearAlbumInformation();
        lstTracks.setModel(new DefaultListModel<String>());
        trvAlbumResults.setModel(null);

        // Initialize the autocomplete fields
        initAutoComplete();

        // Initialize the list listeners
        initListListeners();

        // Initialize the search depth text field
        mAlbumDepth = "Tracks;ImageThumbnail;Media;Names;ExternalIdentifiers;Genres;Publications;";
        txtSearchDepth.setText(Util.getSearchDepthTruncated(mAlbumDepth));
        txtSearchDepth.setEditable(false);

        // Initialize the search URL text field 
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
        txtSearchUrl.setLineWrap(true);
        txtSearchUrl.setEditable(false);
        txtSearchUrl.setBackground(Color.WHITE);

        // Register the AlbumInformation event listeners
        albumInformation.initMouseListeners();
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
        ((AutoCompleteField)cmbSearchAlbumTitle).setDictionary("Albums");
        ((AutoCompleteField)cmbSearchLabel).setDictionary("Publishers");
        ((AutoCompleteField)cmbSearchArtistName).setDictionary("Participants");
        ((AutoCompleteField)cmbSearchGenre).setDictionary("MusicGenres");
        ((AutoCompleteField)cmbSearchBarcode).setDictionary("Barcodes");
    }

    /**
     * Initializes the results table model.
     */
    @Override
    public void initResultsTable() {

        // Set the result table model
        tblAlbumResults.setModel(new AlbumTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblAlbumResults, 0, 57);
        Util.setTableColumnWidth(tblAlbumResults, 1, 310);
        Util.setTableColumnWidth(tblAlbumResults, 2, 255);
        Util.setTableColumnWidth(tblAlbumResults, 3, 125);
        Util.setTableColumnWidth(tblAlbumResults, 4, 115);
        Util.setTableColumnWidth(tblAlbumResults, 5, 115);
        
        tblAlbumResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblAlbumResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the list selection changed event listeners for the result
     * table.
     */
    private void initListListeners() {

        // Register the result table selection changed listener
        tblAlbumResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            /**
             * Occurs when the result table row selection changes.
             *
             * @param e Event arguments.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int sel = tblAlbumResults.getSelectedRow();
                if (sel > -1 && mAlbums != null && sel < mAlbums.size()) {
                    refreshAlbumInformation(mAlbums.get(sel));
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
     * Gets the album search URL.
     *
     * @param searchParam The album search parameters.
     * @return The album search URL.
     */
    private String getAlbumSearchUrl(AlbumSearchParam searchParam) {
        DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
        return urlBuilder.getAlbumSearchUrl(searchParam);
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
        lblSearchArtistName = new javax.swing.JLabel();
        lblSearchGenre = new javax.swing.JLabel();
        lblSearchLabel = new javax.swing.JLabel();
        lblSearchDepth = new javax.swing.JLabel();
        txtSearchDepth = new javax.swing.JTextField();
        btnSelectDepth = new javax.swing.JButton();
        lblSearchUrl = new javax.swing.JLabel();
        lblSearchParameters = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        lblSpacer = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtSearchUrl = new javax.swing.JTextArea();
        cmbSearchAlbumTitle = new AutoCompleteField();
        cmbSearchLabel = new AutoCompleteField();
        cmbSearchArtistName = new AutoCompleteField();
        cmbSearchGenre = new AutoCompleteField();
        cmbSearchBarcode = new AutoCompleteField();
        jTabbedPane1 = new TabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlbumResults = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        albumInformation = new decibelrestjava.panels.AlbumInformation();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstTracks = new javax.swing.JList<String>();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        trvAlbumResults = new javax.swing.JTree();

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblSearchAlbumTitle.setText("Album Title");
        lblSearchAlbumTitle.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchAlbumTitle, gridBagConstraints);

        lblSearchBarcode.setText("Label");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchBarcode, gridBagConstraints);

        lblSearchArtistName.setText("Artist Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchArtistName, gridBagConstraints);

        lblSearchGenre.setText("Genre");
        lblSearchGenre.setPreferredSize(new java.awt.Dimension(70, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchGenre, gridBagConstraints);

        lblSearchLabel.setText("Barcode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchLabel, gridBagConstraints);

        lblSearchDepth.setText("Depth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchDepth, gridBagConstraints);

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchAlbumTitle, gridBagConstraints);

        cmbSearchLabel.setEditable(true);
        cmbSearchLabel.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchLabel.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchLabel.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchLabelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(cmbSearchLabel, gridBagConstraints);

        cmbSearchArtistName.setEditable(true);
        cmbSearchArtistName.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchArtistName.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchArtistName.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchArtistName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchArtistNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchArtistName, gridBagConstraints);

        cmbSearchGenre.setEditable(true);
        cmbSearchGenre.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchGenre.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchGenre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchGenreActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(cmbSearchGenre, gridBagConstraints);

        cmbSearchBarcode.setEditable(true);
        cmbSearchBarcode.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchBarcode.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchBarcodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchBarcode, gridBagConstraints);

        jSplitPane1.setDividerLocation(210);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);

        tblAlbumResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAlbumResults);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1204, Short.MAX_VALUE)
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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 693, Short.MAX_VALUE)
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
        trvAlbumResults.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane5.setViewportView(trvAlbumResults);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1206, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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
        search(getAlbumSearchUrl(mSearchParam));
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
        mAlbums = null;

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
                    AlbumQueryResult albumQueryResult;
                    try {
                        albumQueryResult = Util.<AlbumQueryResult>deserializeXmlString(AlbumQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }
                    mAlbums = albumQueryResult.getResultSet().getAlbum();

                    // Store the query result ID for paged results
                    if(((TabbedPane)jTabbedPane1).getCurrentPage() == 1)
                         mQueryResultID = albumQueryResult.getQueryResultID();

                    // Initialize the page navigator
                    if(albumQueryResult.getPages() > 1)
                        ((TabbedPane)jTabbedPane1).setPageCount(albumQueryResult.getPages());

                    // Initialize the results table
                    DefaultTableModel model = (DefaultTableModel) tblAlbumResults.getModel();
                    model.getDataVector().removeAllElements();

                    for (Album album : mAlbums) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = album.getName();
                        row[2] = album.getArtists();
                        List<String> publishers  = Util.getAlbumPublishers(album);
                        row[3] = publishers.size() > 0 ? publishers.get(0) : null;
                        row[4] = Util.formatTime(album.getTotalSeconds().intValue());
                        row[5] = album.getTrackCount();
                        model.addRow(row);
                        count++;
                    }
                    tblAlbumResults.setModel(model);

                    // Initialize the XML results tree view
                    try {
                        InputSource source = new InputSource(new StringReader(evt.Result));
                        XMLTreeModel xmlTree = new XMLTreeModel(DOMUtil.createDocument(source));
                        trvAlbumResults.setModel(xmlTree);
                        trvAlbumResults.expandRow(0);
                        trvAlbumResults.expandRow(1);
                        trvAlbumResults.expandRow(9);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Unable to initialize XML result tree view." 
                                + System.lineSeparator() + e.toString());
                        trvAlbumResults.setModel(null);
                    }

                    tblAlbumResults.setRowSelectionInterval(0, 0);
                    tblAlbumResults.requestFocus();

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
            String url = Settings.ApiAddress + "Albums/Pages/" + mQueryResultID + "?pageNumber=" + (pageNumber - 1);
            search(url);
        }
    }

    /**
     * Refreshes the album information panel.
     *
     * @param album The Album object.
     */
    private void refreshAlbumInformation(Album album) {

        albumInformation.clearAlbumInformation();
        lstTracks.setModel(new DefaultListModel<String>());

        String url = Settings.ApiAddress + "Albums/" + "?depth=" + mAlbumDepth + "Performances;&id=" + album.getID();
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
     * Refreshes the track information for album and disc number.
     *
     * @param album The Album object.
     * @param discNumber The disc number.
     */
    private void refreshTrackInformation(Album album, String discNumber) {

        mTrackList = album.getTracks().getTrack();

        DefaultListModel<String> list = new DefaultListModel<String>();
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
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
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
    private void cmbSearchAlbumTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchAlbumTitleActionPerformed
        mSearchParam.AlbumTitle = ((AutoCompleteField)cmbSearchAlbumTitle).getText();
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchAlbumTitleActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchArtistNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchArtistNameActionPerformed
        mSearchParam.ArtistName = ((AutoCompleteField) cmbSearchArtistName).getText();
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchArtistNameActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchGenreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchGenreActionPerformed
        mSearchParam.Genre = ((AutoCompleteField)cmbSearchGenre).getText();
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchGenreActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchLabelActionPerformed
        mSearchParam.Label = ((AutoCompleteField)cmbSearchLabel).getText();
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchLabelActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */
    private void cmbSearchBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchBarcodeActionPerformed
        mSearchParam.Barcode = ((AutoCompleteField) cmbSearchBarcode).getText();
        txtSearchUrl.setText(getAlbumSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchBarcodeActionPerformed

    /**
     * Occurs when the select depth button is clicked.
     *
     * @param evt Event arguments.
     */
    private void btnSelectDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDepthActionPerformed
        List<String> selections = Arrays.asList(mAlbumDepth.split(";"));
        List<String> suggestions = SearchDepth.getAlbumDepthParameters();
        SearchDepth searchDepth = new SearchDepth((JFrame)SwingUtilities.getWindowAncestor(this), true);
        int ret = searchDepth.showDialog(suggestions, selections);
        if (ret == 0) {
            mAlbumDepth = searchDepth.getSelectedItems();
            txtSearchDepth.setText(Util.getSearchDepthTruncated(mAlbumDepth));
        }
    }//GEN-LAST:event_btnSelectDepthActionPerformed

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
    private javax.swing.JComboBox<Object> cmbSearchArtistName;
    private javax.swing.JComboBox<Object> cmbSearchBarcode;
    private javax.swing.JComboBox<Object> cmbSearchGenre;
    private javax.swing.JComboBox<Object> cmbSearchLabel;
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
    private javax.swing.JLabel lblSearchArtistName;
    private javax.swing.JLabel lblSearchBarcode;
    private javax.swing.JLabel lblSearchDepth;
    private javax.swing.JLabel lblSearchGenre;
    private javax.swing.JLabel lblSearchLabel;
    private javax.swing.JLabel lblSearchParameters;
    private javax.swing.JLabel lblSearchUrl;
    private javax.swing.JLabel lblSpacer;
    private javax.swing.JList<String> lstTracks;
    private javax.swing.JTable tblAlbumResults;
    private javax.swing.JTree trvAlbumResults;
    private javax.swing.JTextField txtSearchDepth;
    private javax.swing.JTextArea txtSearchUrl;
    // End of variables declaration//GEN-END:variables
}
