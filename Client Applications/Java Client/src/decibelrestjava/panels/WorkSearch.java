package decibelrestjava.panels;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import decibelrestjava.autocomplete.AutoCompleteField;
import decibelrestjava.frames.SearchDepth;
import decibelrestjava.DecibelUrlBuilder;
import decibelrestjava.DecibelUrlBuilder.WorkSearchParam;
import decibelrestjava.webclient.WebClient;
import decibelrestjava.webclient.DownloadStringCompletedEvent;
import decibelrestjava.webclient.WebClientListener;
import decibelrestjava.tables.TrackAppearancesTableModel;
import decibelrestjava.tables.WorkTableModel;
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
 * Searches for works using the Decibel REST API.
 */
@SuppressWarnings("serial")
public class WorkSearch extends javax.swing.JPanel implements ISearchPanel, PageChangedCallback {

    /**
     * The collection of works returned from the search.
     */
    private List<Work> mWorks;    
    /**
     * The collection of tracks appearances for the work.
     */
    private List<TrackAppearance> mTrackAppearance;  
    /**
     * The work search parameters.
     */
    private WorkSearchParam mSearchParam;
    /**
     * The works search client.
     */
    private WebClient mSearchClient;
    /**
     * The track search client.
     */
    private WebClient mTrackSearchClient;
    /**
     * Stores the object which implements the search callback function.
     */
    private SearchCallback mSearchCallback;
    /**
     * The response string.
     */
    private String mResponse;
    /**
     * The work search query result ID.
     */
    private String mQueryResultID;
    /**
     * The work information search depth.
     */
    private String mWorkDepth;
        
    /**
     * Creates new form WorkSearch.
     */
    public WorkSearch() {
        initComponents();
        initPanel();
    }

    /**
     * Initializes the panel.
     */
    @Override
    public void initPanel() {

        // Initialize the search parameters
        mSearchParam = new WorkSearchParam();
        mSearchParam.Depth = "Names;";

        // Clear the results
        initResultsTable();
        initTrackAppearancesTable();
        trvWorkResults.setModel(null);

        // Initialize the autocomplete fields
        initAutoComplete();

        // Initialize the list listeners
        initListListeners();

        // Initialize the search depth text field
        mWorkDepth = "Publications;Names;";
        txtSearchDepth.setText(Util.getSearchDepthTruncated(mWorkDepth));
        txtSearchDepth.setEditable(false);

        // Initialize the search URL text field 
        txtSearchUrl.setText(getWorkSearchUrl(mSearchParam));
        txtSearchUrl.setLineWrap(true);
        txtSearchUrl.setEditable(false);
        txtSearchUrl.setBackground(Color.WHITE);
       
        // Register the TabbedPane event listeners
        ((TabbedPane)jTabbedPane1).initMouseListeners();
        ((TabbedPane)jTabbedPane1).initPageSelectedCallback(this);            
    }

    /**
     * Initializes the autocomplete fields.
     */
    @Override
    public void initAutoComplete() {
        ((AutoCompleteField)cmbSearchName).setDictionary("Works");
        ((AutoCompleteField)cmbSearchCatalogue).setDictionary("MusicCatalogues");
        ((AutoCompleteField)cmbSearchComposers).setDictionary("WorksComposers");
    }

    /**
     * Initializes the result table model.
     */
    @Override
    public void initResultsTable() {

        // Create the result table model
        tblWorkResults.setModel(new WorkTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblWorkResults, 0, 57);
        Util.setTableColumnWidth(tblWorkResults, 1, 450);
        Util.setTableColumnWidth(tblWorkResults, 2, 450);
        
        tblWorkResults.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblWorkResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Initializes the list selection changed event listener for the results
     * table.
     */
    private void initListListeners() {

        // Register the result table selection changed listener
        tblWorkResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            /**
             * Occurs when the result table row selection changes.
             *
             * @param e Event arguments.
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int sel = tblWorkResults.getSelectedRow();
                if (sel > -1 && mWorks != null && sel < mWorks.size()) {
                    refreshTrackAppearances(mWorks.get(sel));
                }
            }
        });
    }   
    
    /**
     * Initialiaze the track appearances table.
     */
    private void initTrackAppearancesTable() {

        // Create the result table model
        tblTrackAppearances.setModel(new TrackAppearancesTableModel());

        // Set the default column widths
        Util.setTableColumnWidth(tblTrackAppearances, 0, 57);
        Util.setTableColumnWidth(tblTrackAppearances, 1, 255);
        Util.setTableColumnWidth(tblTrackAppearances, 2, 255);
        Util.setTableColumnWidth(tblTrackAppearances, 3, 255);
        Util.setTableColumnWidth(tblTrackAppearances, 4, 80);
        Util.setTableColumnWidth(tblTrackAppearances, 5, 80);
        
        tblTrackAppearances.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblTrackAppearances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Gets the work search request URL.
     *
     * @param searchParam The work search parameters.
     * @return The work search URL.
     */
    private String getWorkSearchUrl(WorkSearchParam searchParam) {
        DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
        return urlBuilder.getWorkSearchUrl(searchParam);
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
        lblSearchName = new javax.swing.JLabel();
        lblSearchArtistName = new javax.swing.JLabel();
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
        cmbSearchName = new AutoCompleteField();
        cmbSearchComposers = new AutoCompleteField();
        cmbSearchCatalogue = new AutoCompleteField();
        jTabbedPane1 = new TabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblWorkResults = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTrackAppearances = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        trvWorkResults = new javax.swing.JTree();

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblSearchName.setText("Name");
        lblSearchName.setPreferredSize(new java.awt.Dimension(80, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchName, gridBagConstraints);

        lblSearchArtistName.setText("Composer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchArtistName, gridBagConstraints);

        lblSearchLabel.setText("Catalogue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel1.add(lblSearchLabel, gridBagConstraints);

        lblSearchDepth.setText("Depth");
        lblSearchDepth.setPreferredSize(new java.awt.Dimension(70, 14));
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

        cmbSearchComposers.setEditable(true);
        cmbSearchComposers.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cmbSearchComposers.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchComposers.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchComposers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchComposersActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchComposers, gridBagConstraints);

        cmbSearchCatalogue.setEditable(true);
        cmbSearchCatalogue.setMinimumSize(new java.awt.Dimension(6, 20));
        cmbSearchCatalogue.setPreferredSize(new java.awt.Dimension(10, 20));
        cmbSearchCatalogue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSearchCatalogueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 15);
        jPanel1.add(cmbSearchCatalogue, gridBagConstraints);

        jSplitPane1.setDividerLocation(210);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);

        tblWorkResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblWorkResults);

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
        jScrollPane3.setViewportView(tblTrackAppearances);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1069, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
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
        trvWorkResults.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane5.setViewportView(trvWorkResults);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
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

    private void btnSelectDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDepthActionPerformed
        List<String> selections = Arrays.asList(mWorkDepth.split(";"));
        List<String> suggestions = SearchDepth.getWorkDepthParameters();
        SearchDepth searchDepth = new SearchDepth((JFrame)SwingUtilities.getWindowAncestor(this), true);
        int ret = searchDepth.showDialog(suggestions, selections);
        if (ret == 0) {
            mWorkDepth = searchDepth.getSelectedItems();
            txtSearchDepth.setText(Util.getSearchDepthTruncated(mWorkDepth));
        }
    }//GEN-LAST:event_btnSelectDepthActionPerformed

    /**
     * Occurs when the search button is clicked.
     *
     * @param evt Event arguments.
     */ 
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        search(getWorkSearchUrl(mSearchParam));
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

        mResponse = "";
        mWorks = null;

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
                    WorkQueryResult workQueryResult;
                    try {
                        workQueryResult = Util.<WorkQueryResult>deserializeXmlString(WorkQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }       
                    mWorks = workQueryResult.getResultSet().getWork();

                    // Store the query result ID for paged results
                    if(((TabbedPane)jTabbedPane1).getCurrentPage() == 1)
                         mQueryResultID = workQueryResult.getQueryResultID();

                    // Initialize the page navigator
                    if(workQueryResult.getPages() > 1)
                        ((TabbedPane)jTabbedPane1).setPageCount(workQueryResult.getPages());                    

                    // Initialise the results table
                    DefaultTableModel model = (DefaultTableModel) tblWorkResults.getModel();
                    model.getDataVector().removeAllElements();

                    for (Work work : mWorks) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = work.getName();
                        row[2] = work.getComposers();
                        model.addRow(row);
                        count++;
                    }
                    tblWorkResults.setModel(model);

                    // Initialize the XML results tree view
                    try {
                        InputSource source = new InputSource(new StringReader(evt.Result));
                        XMLTreeModel xmlTree = new XMLTreeModel(DOMUtil.createDocument(source));
                        trvWorkResults.setModel(xmlTree);
                        trvWorkResults.expandRow(0);
                        trvWorkResults.expandRow(1);
                        trvWorkResults.expandRow(9);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Unable to initialize XML result tree view." 
                                + System.lineSeparator() + e.toString());
                        trvWorkResults.setModel(null);
                    }

                    if(tblWorkResults.getModel().getRowCount() > 0)
                    {
                        tblWorkResults.setRowSelectionInterval(0, 0);
                    }
                    tblWorkResults.requestFocus();
                }
                mSearchCallback.endSearch(count);
                btnSearch.setEnabled(true);
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
            String url = Settings.ApiAddress + "Works/Pages/" + mQueryResultID + "?pageNumber=" + (pageNumber - 1);
            search(url);
        }
    }   
 
    /**
     * Refreshes the track appearances.
     *
     * @param index The Work object.
     */
    private void refreshTrackAppearances(Work work) {
        
        String url = Settings.ApiAddress + "Works/" + work.getID() + "/Tracks";

        mSearchCallback.beginSearch(url);

        if (mTrackSearchClient != null) {
            mTrackSearchClient.stop();
        }
        mTrackSearchClient = new WebClient();
        Util.setDecibelRequestHeaders(mTrackSearchClient);

        // Register the WebClient downloadStringCompleted event
        mTrackSearchClient.addWebClientEventListener(new WebClientListener() {
            @Override
            public void downloadStringCompleted(DownloadStringCompletedEvent evt) {

                int count = 0;
                mResponse = evt.Result;

                if (evt.Error == null) {
                    
                    // Deserialize the response
                    WorkTrackAppearanceQueryResult trackQueryResult;
                    try {
                        trackQueryResult = Util.<WorkTrackAppearanceQueryResult>deserializeXmlString(WorkTrackAppearanceQueryResult.class, evt.Result);
                    } catch (JAXBException e) {
                        JOptionPane.showMessageDialog(null, "Unable to deserialize object from response body." 
                                + System.lineSeparator() + e.toString());
                        return;
                    }   
                    mTrackAppearance = trackQueryResult.getResultSet().getTrackAppearance();

                    // Initialise the results table
                    DefaultTableModel model = (DefaultTableModel) tblTrackAppearances.getModel();
                    model.getDataVector().removeAllElements();

                    for (TrackAppearance trackAppearance : mTrackAppearance) {
                        Object[] row = new Object[6];
                        row[0] = count + 1;
                        row[1] = trackAppearance.getTrackName();
                        row[2] = trackAppearance.getTrackArtistName();
                        row[3] = trackAppearance.getAlbumName();
                        row[4] = trackAppearance.getTrackNumber();
                        row[5] = Util.formatTime(trackAppearance.getTotalSeconds().intValue());
                        model.addRow(row);
                        count++;
                    }
                    tblTrackAppearances.setModel(model);
                }
                mSearchCallback.endSearch(count);
            }
        });

        mTrackSearchClient.downloadStringAsync(url);
    }
        
    /**
     * Initializes the search callback function.
     *
     * @param callback
     */
    @Override
    public void initSearchCallback(SearchCallback callback) {
        mSearchCallback = callback;
    }

    /**
     * Gets the default button on the panel.
     *
     * @return The search button.
     */
    @Override
    public JButton getDefaultButton() {
        return btnSearch;
    }

    /**
     * Refreshes the search URL.
     */
    public void refreshSearchUrl() {
        txtSearchUrl.setText(getWorkSearchUrl(mSearchParam));
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
        txtSearchUrl.setText(getWorkSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchNameActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */  
    private void cmbSearchComposersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchComposersActionPerformed
        mSearchParam.Composers = ((AutoCompleteField)cmbSearchComposers).getText();
        txtSearchUrl.setText(getWorkSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchComposersActionPerformed

    /**
     * Occurs when a item is selected from the suggestion list or a key is
     * released.
     *
     * @param evt Event arguments.
     */   
    private void cmbSearchCatalogueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSearchCatalogueActionPerformed
        mSearchParam.Catalogue = ((AutoCompleteField)cmbSearchCatalogue).getText();
        txtSearchUrl.setText(getWorkSearchUrl(mSearchParam));
    }//GEN-LAST:event_cmbSearchCatalogueActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSelectDepth;
    private javax.swing.JComboBox<Object> cmbSearchCatalogue;
    private javax.swing.JComboBox<Object> cmbSearchComposers;
    private javax.swing.JComboBox<Object> cmbSearchName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblSearchArtistName;
    private javax.swing.JLabel lblSearchDepth;
    private javax.swing.JLabel lblSearchLabel;
    private javax.swing.JLabel lblSearchName;
    private javax.swing.JLabel lblSearchParameters;
    private javax.swing.JLabel lblSearchUrl;
    private javax.swing.JLabel lblSpacer;
    private javax.swing.JTable tblTrackAppearances;
    private javax.swing.JTable tblWorkResults;
    private javax.swing.JTree trvWorkResults;
    private javax.swing.JTextField txtSearchDepth;
    private javax.swing.JTextArea txtSearchUrl;
    // End of variables declaration//GEN-END:variables
}
