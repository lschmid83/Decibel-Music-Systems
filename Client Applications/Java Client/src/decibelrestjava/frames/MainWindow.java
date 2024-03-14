package decibelrestjava.frames;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import decibelrestjava.panels.SearchCallback;
import java.awt.FontMetrics;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;

/**
 * The main application window.
 */
@SuppressWarnings("serial")
public class MainWindow extends javax.swing.JFrame implements SearchCallback {

    /**
     * The selected tab pane.
     */
    private int mSelectedTab;

    /**
     * Creates new form MainWindow.
     */
    public MainWindow() {

        // Read the application settings
        Settings.readApplicationSettings();
        if (Util.isNullOrWhiteSpace(Settings.ApiAddress) || Util.isNullOrWhiteSpace(Settings.ApplicationID) || Util.isNullOrWhiteSpace(Settings.ApplicationKey)) {
            Options options = new Options(this, false);
            options.setVisible(true);
        }       
        
        initComponents(); 
        
        this.setTitle("Decibel REST API Sample Project");
        this.jProgressBar.setVisible(false);
        this.jProgressBar.setIndeterminate(true);

        this.tabAlbums.initSearchCallback(this);
        this.tabParticipants.initSearchCallback(this);
        this.tabTracks.initSearchCallback(this);  
        this.tabWorks.initSearchCallback(this);

        // Add the edit menu with cut, copy and paste actions
        JMenu editMenu = new JMenu("Edit");

        Action cutAction = new DefaultEditorKit.CutAction();
        cutAction.putValue(Action.NAME, "Cut");
        editMenu.add(cutAction);

        Action copyAction = new DefaultEditorKit.CopyAction();
        copyAction.putValue(Action.NAME, "Copy");
        editMenu.add(copyAction);

        Action pasteAction = new DefaultEditorKit.PasteAction();
        pasteAction.putValue(Action.NAME, "Paste");
        editMenu.add(pasteAction);

        this.getJMenuBar().add(editMenu, 1);

        // Register a change listener for the tabbed pane
        jTabbedPane.addChangeListener(new ChangeListener() {
            // This method is called whenever the selected tab changes
            @Override
            public void stateChanged(ChangeEvent evt) {
                JTabbedPane pane = (JTabbedPane) evt.getSource();

                // Get current tab
                mSelectedTab = pane.getSelectedIndex();
                setDefaultButton(mSelectedTab);
                refreshSearchUrl(mSelectedTab);
            }
        });
        this.getRootPane().setDefaultButton(tabAlbums.getDefaultButton());       
    }

    /**
     * Sets the default button for the selected tab pane.
     *
     * @param selectedTab The selected tab pane.
     */
    public void setDefaultButton(int selectedTab) {
        if (selectedTab == 0) {
            this.getRootPane().setDefaultButton(tabAlbums.getDefaultButton());
        } else if (selectedTab == 1) {
            this.getRootPane().setDefaultButton(tabParticipants.getDefaultButton());
        } else if (selectedTab == 2) {
            this.getRootPane().setDefaultButton(tabTracks.getDefaultButton());
        } else if (selectedTab == 3) {
            this.getRootPane().setDefaultButton(tabWorks.getDefaultButton());
        }
    }

    /**
     * Refreshes the search URL.
     *
     * @param selectedTab The selected tab pane.
     */
    public void refreshSearchUrl(int selectedTab) {
        if (selectedTab == 0) {
            this.tabAlbums.refreshSearchUrl();
        } else if (selectedTab == 1) {
            this.tabParticipants.refreshSearchUrl();
        } else if (selectedTab == 2) {
            this.tabTracks.refreshSearchUrl();
        } else if (selectedTab == 3) {
            this.tabWorks.refreshSearchUrl();
        }
    }
    
    /**
     * Begins the search.
     *
     * @param url The Decibel REST request URL.
     */
    @Override
    public void beginSearch(String url) {
        this.jProgressBar.setVisible(true);
        this.txtStatus.setText("Searching...");
    }

    /**
     * Occurs when the album, track or participant search completes.
     *
     * @param resultCount The number of results returned.
     */
    @Override
    public void endSearch(int resultCount) {
        this.jProgressBar.setVisible(false);
        this.txtStatus.setText("Ready");
    }

    /**
     * Occurs when the status bar text changes.
     *
     * @param text The text to display in the status bar.
     */
    @Override
    public void updateStatus(String text) {

        FontMetrics metrics = this.getGraphics().getFontMetrics(this.txtStatus.getFont());
        jPanel1.setSize(metrics.stringWidth(text), 20);
        txtStatus.setSize(metrics.stringWidth(text), 16);
        this.txtStatus.setText(text);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jStatusbar = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        txtStatus = new javax.swing.JLabel();
        jTabbedPane = new javax.swing.JTabbedPane();
        tabAlbums = new decibelrestjava.panels.AlbumSearch();
        tabParticipants = new decibelrestjava.panels.ParticipantSearch();
        tabTracks = new decibelrestjava.panels.TrackSearch();
        tabWorks = new decibelrestjava.panels.WorkSearch();
        jMenuBar = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        mnuNew = new javax.swing.JMenuItem();
        mnuSaveAs = new javax.swing.JMenuItem();
        mnuPrint = new javax.swing.JMenuItem();
        mnuExit = new javax.swing.JMenuItem();
        mnuTools = new javax.swing.JMenu();
        mnuOptions = new javax.swing.JMenuItem();
        mnuHelp = new javax.swing.JMenu();
        mnuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 300));

        jStatusbar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jStatusbar.setPreferredSize(new java.awt.Dimension(400, 25));

        jPanel2.setMinimumSize(new java.awt.Dimension(50, 20));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jProgressBar.setMinimumSize(new java.awt.Dimension(80, 14));
        jPanel2.add(jProgressBar, new java.awt.GridBagConstraints());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        txtStatus.setText("Ready");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(txtStatus, gridBagConstraints);

        javax.swing.GroupLayout jStatusbarLayout = new javax.swing.GroupLayout(jStatusbar);
        jStatusbar.setLayout(jStatusbarLayout);
        jStatusbarLayout.setHorizontalGroup(
            jStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jStatusbarLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(774, Short.MAX_VALUE))
        );
        jStatusbarLayout.setVerticalGroup(
            jStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jStatusbarLayout.createSequentialGroup()
                .addGroup(jStatusbarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(782, 782, 782))
        );

        getContentPane().add(jStatusbar, java.awt.BorderLayout.SOUTH);

        jTabbedPane.addTab("Albums", tabAlbums);
        jTabbedPane.addTab("Participants", tabParticipants);
        jTabbedPane.addTab("Tracks", tabTracks);
        jTabbedPane.addTab("Works", tabWorks);

        getContentPane().add(jTabbedPane, java.awt.BorderLayout.CENTER);

        mnuFile.setText("File");

        mnuNew.setText("New");
        mnuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewActionPerformed(evt);
            }
        });
        mnuFile.add(mnuNew);

        mnuSaveAs.setText("Save As...");
        mnuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSaveAsActionPerformed(evt);
            }
        });
        mnuFile.add(mnuSaveAs);

        mnuPrint.setText("Print...");
        mnuPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPrintActionPerformed(evt);
            }
        });
        mnuFile.add(mnuPrint);

        mnuExit.setText("Exit");
        mnuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuExitActionPerformed(evt);
            }
        });
        mnuFile.add(mnuExit);

        jMenuBar.add(mnuFile);

        mnuTools.setText("Tools");

        mnuOptions.setText("Options");
        mnuOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuOptionsActionPerformed(evt);
            }
        });
        mnuTools.add(mnuOptions);

        jMenuBar.add(mnuTools);

        mnuHelp.setText("Help");

        mnuAbout.setText("About");
        mnuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAboutActionPerformed(evt);
            }
        });
        mnuHelp.add(mnuAbout);

        jMenuBar.add(mnuHelp);

        setJMenuBar(jMenuBar);

        setSize(new java.awt.Dimension(1024, 768));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Occurs when the New menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewActionPerformed
        if (mSelectedTab == 0) {
            this.tabAlbums.initPanel();
        } else if (mSelectedTab == 1) {
            this.tabParticipants.initPanel();
        } else if (mSelectedTab == 2) {
            this.tabTracks.initPanel();
        } else if (mSelectedTab == 3) {
            this.tabWorks.initPanel();
        }
        mnuSaveAs.setEnabled(false);
    }//GEN-LAST:event_mnuNewActionPerformed

    /**
     * Occurs when the Options menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuOptionsActionPerformed
        Options optionsWindow = new Options(this, true);
        optionsWindow.setVisible(true);
        refreshSearchUrl(mSelectedTab);
    }//GEN-LAST:event_mnuOptionsActionPerformed

    /**
     * Occurs when the About menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        JOptionPane.showMessageDialog(null, "Decibel REST API Sample Project\nDecibel Music Systems", "About", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_mnuAboutActionPerformed

    /**
     * Occurs when the Exit menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuExitActionPerformed

    /**
     * Occurs when the Print menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPrintActionPerformed
        if (mSelectedTab == 0) {
            Util.printComponent(this.tabAlbums);
        } else if (mSelectedTab == 1) {
            Util.printComponent(this.tabParticipants);
        } else if (mSelectedTab == 2) {
            Util.printComponent(this.tabTracks);
        } else if (mSelectedTab == 3) {
            Util.printComponent(this.tabWorks);
        }
    }//GEN-LAST:event_mnuPrintActionPerformed

    /**
     * Occurs when the Save As menu item is clicked.
     *
     * @param evt Event arguments.
     */
    private void mnuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSaveAsActionPerformed
        if (mSelectedTab == 0) {
            Util.saveAs(this, tabAlbums.getResponse());
        } else if (mSelectedTab == 1) {
            Util.saveAs(this, tabParticipants.getResponse());
        } else if (mSelectedTab == 2) {
            Util.saveAs(this, tabTracks.getResponse());
        } else if (mSelectedTab == 3) {
            Util.saveAs(this, tabWorks.getResponse());
        }
    }//GEN-LAST:event_mnuSaveAsActionPerformed
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
 
        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JPanel jStatusbar;
    private javax.swing.JTabbedPane jTabbedPane;
    private javax.swing.JMenuItem mnuAbout;
    private javax.swing.JMenuItem mnuExit;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu mnuHelp;
    private javax.swing.JMenuItem mnuNew;
    private javax.swing.JMenuItem mnuOptions;
    private javax.swing.JMenuItem mnuPrint;
    private javax.swing.JMenuItem mnuSaveAs;
    private javax.swing.JMenu mnuTools;
    private decibelrestjava.panels.AlbumSearch tabAlbums;
    private decibelrestjava.panels.ParticipantSearch tabParticipants;
    private decibelrestjava.panels.TrackSearch tabTracks;
    private decibelrestjava.panels.WorkSearch tabWorks;
    private javax.swing.JLabel txtStatus;
    // End of variables declaration//GEN-END:variables
}
