package decibelrestjava.panels;

import queryresultholder.Album;
import queryresultholder.GenreValue;
import queryresultholder.Genre;
import queryresultholder.GeoEntityValue;
import decibelrestjava.Util;
import decibelrestjava.frames.AlbumImage;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import queryresultholder.*;

/**
 * Reads a Decibel Album object and displays album information.
 */
@SuppressWarnings("serial")
public class AlbumInformation extends javax.swing.JPanel implements MouseListener, MouseMotionListener {

    /**
     * Stores the object which implements the disc changed callback function.
     */
    private DiscChangedCallback mCallback;
    /**
     * Stores the thumbnail image.
     */
    private BufferedImage mImage;
    /**
     * Stores the coordinates of the thumbnail image.
     */
    private Rectangle mRect;
    /**
     * Stores the URL of the album image.
     */
    private String mImageUrl;
    /**
     * The selected disc index
     */
    private int mSelectedDisc;

    /**
     * Creates new form AlbumInformation.
     */
    public AlbumInformation() {
        initComponents();
    }
    
    /**
     * Initializes the mouse event listeners.
     */
    public void initMouseListeners()
    {
        addMouseListener(this);
        addMouseMotionListener(this);   
    }

    /**
     * Initialize the disc changed callback function.
     *
     * @param callback
     */
    public void initDiscChangedCallback(DiscChangedCallback callback) {
        mSelectedDisc = 0;
        mCallback = callback;
    }

    /**
     * Sets the album information.
     *
     * @param album The album query result object.
     */
    public void setAlbumInformation(Album album) {

        clearAlbumInformation();

        // Album name
        txtAlbumName.setText(album.getName());

        // Artists
        txtAlbumArtists.setText(album.getArtists());

        // Discs
        for (int i = 1; i <= album.getDiscCount(); i++) {
            cmbAlbumDiscs.addItem(i);
        }
        if (cmbAlbumDiscs.getItemCount() > 0) {
            cmbAlbumDiscs.setEnabled(true);
            cmbAlbumDiscs.setSelectedIndex(0);
        }

        // Number of tracks
        txtAlbumTracks.setText(Integer.toString(album.getTrackCount()));

        // Duration
        int duration = album.getTotalSeconds().intValue();
        if (duration > 0) {
            txtAlbumDuration.setText(Util.formatTime(duration));
        }

        // Format
        if (album.getDiscs() != null && album.getDiscs().getDisc().size() > 0
                && album.getDiscs().getDisc().get(mSelectedDisc).getMusicMedium() != null) {
            txtAlbumFormat.setText(album.getDiscs().getDisc().get(mSelectedDisc).getMusicMedium().getName());
        }

        // Release date
        if (album.getReleaseDate() != null && !album.getReleaseDate().getName().isEmpty()) {
            txtAlbumReleaseDate.setText(album.getReleaseDate().getName());
        }

        // Catalogue number
        if (album.getPublications() != null && album.getPublications().getPublication().size() > 0) {
            txtAlbumCatalogue.setText(album.getPublications().getPublication().get(0).getCatalogueNumber());
        }

        // Label
        List<String> publishers = Util.getAlbumPublishers(album);
        if (publishers.size() > 0) {
            txtAlbumPublisher.setText(publishers.get(0));
        }

        // Genres
        if (album.getGenres() != null) {
            for (GenreValue genreValue : album.getGenres().getGenreValue()) {
                Genre genre = genreValue.getGenre();
                if (!genre.getName().isEmpty()) {
                    txtAlbumGenres.setText(txtAlbumGenres.getText() + genre.getName() + ", ");
                }
            }
            if (txtAlbumGenres.getText().length() > 2) {
                txtAlbumGenres.setText(txtAlbumGenres.getText().substring(0, txtAlbumGenres.getText().length() - 2));
            }
        }

        // Region
        if (album.getGeoEntities() != null) {
            for (GeoEntityValue gev : album.getGeoEntities().getGeoEntityValue()) {
            	GeoEntity ge = gev.getGeoEntity();
                if (ge != null && ge.getName() != null) {
                    txtAlbumRegion.setText(txtAlbumRegion.getText() + ge.getName() + ", ");
                }
            }
            if (txtAlbumRegion.getText().length() > 2) {
                txtAlbumRegion.setText(txtAlbumRegion.getText().substring(0, txtAlbumRegion.getText().length() - 2));
            }
        }

        // Barcode
        List<String> barcodes = Util.getAlbumBarcodes(album);
        if (barcodes.size() > 0)
            txtAlbumBarcode.setText(barcodes.get(0));

        // Thumbnail image
        if(album.getThumbnail() != null)
        {
            InputStream in = new ByteArrayInputStream(album.getThumbnail());
            try {
                mImage = ImageIO.read(in);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Unable to read image thumbnail." 
                        + System.lineSeparator() + e.toString());
            }
        }
        mImageUrl = album.getImageUrl().getUrl();
        this.updateUI();
    }

    /**
     * Draws the thumbnail image on the component.
     * 
     * @param g The Graphics object.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        mRect = new Rectangle(this.getWidth() - 90, 10, 80, 80);
        g.drawImage(mImage, mRect.x, mRect.y, mRect.width, mRect.height, this);
    }

    /**
     * Clears the album information fields.
     */
    public void clearAlbumInformation() {
        txtAlbumName.setText("");
        txtAlbumArtists.setText("");
        cmbAlbumDiscs.removeAllItems();
        cmbAlbumDiscs.setEnabled(false);
        txtAlbumTracks.setText("");
        txtAlbumDuration.setText("");
        txtAlbumFormat.setText("");
        txtAlbumPublisher.setText("");
        txtAlbumReleaseDate.setText("");
        txtAlbumRegion.setText("");
        txtAlbumCatalogue.setText("");
        txtAlbumBarcode.setText("");
        txtAlbumGenres.setText("");
        mImage = null;
        this.updateUI();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblAlbumName = new javax.swing.JLabel();
        txtAlbumName = new javax.swing.JTextField();
        lblAlbumArtists = new javax.swing.JLabel();
        txtAlbumArtists = new javax.swing.JTextField();
        lblAlbumDiscs = new javax.swing.JLabel();
        cmbAlbumDiscs = new JComboBox<Integer>();
        lblAlbumDuration = new javax.swing.JLabel();
        txtAlbumDuration = new javax.swing.JTextField();
        lblAlbumFormat = new javax.swing.JLabel();
        txtAlbumFormat = new javax.swing.JTextField();
        lblAlbumReleaseDate = new javax.swing.JLabel();
        txtAlbumReleaseDate = new javax.swing.JTextField();
        lblAlbumGenres = new javax.swing.JLabel();
        txtAlbumGenres = new javax.swing.JTextField();
        lblAlbumBarcode = new javax.swing.JLabel();
        txtAlbumCatalogue = new javax.swing.JTextField();
        lblAlbumTracks = new javax.swing.JLabel();
        txtAlbumTracks = new javax.swing.JTextField();
        lblAlbumRegion = new javax.swing.JLabel();
        txtAlbumRegion = new javax.swing.JTextField();
        spacer12 = new javax.swing.JLabel();
        spacer2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtAlbumPublisher = new javax.swing.JTextField();
        txtAlbumBarcode = new javax.swing.JTextField();

        setLayout(new java.awt.GridBagLayout());

        lblAlbumName.setText("Title");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 58;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumName, gridBagConstraints);

        txtAlbumName.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumName, gridBagConstraints);

        lblAlbumArtists.setText("Artists");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumArtists, gridBagConstraints);

        txtAlbumArtists.setPreferredSize(new java.awt.Dimension(300, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumArtists, gridBagConstraints);

        lblAlbumDiscs.setText("Discs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumDiscs, gridBagConstraints);

        cmbAlbumDiscs.setMinimumSize(new java.awt.Dimension(50, 20));
        cmbAlbumDiscs.setPreferredSize(new java.awt.Dimension(50, 20));
        cmbAlbumDiscs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAlbumDiscsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(cmbAlbumDiscs, gridBagConstraints);

        lblAlbumDuration.setText("Duration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumDuration, gridBagConstraints);

        txtAlbumDuration.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        txtAlbumDuration.setMinimumSize(new java.awt.Dimension(120, 20));
        txtAlbumDuration.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumDuration, gridBagConstraints);

        lblAlbumFormat.setText("Format");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumFormat, gridBagConstraints);

        txtAlbumFormat.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        txtAlbumFormat.setMinimumSize(new java.awt.Dimension(120, 20));
        txtAlbumFormat.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumFormat, gridBagConstraints);

        lblAlbumReleaseDate.setText("Release Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumReleaseDate, gridBagConstraints);

        txtAlbumReleaseDate.setMaximumSize(new java.awt.Dimension(100, 2147483647));
        txtAlbumReleaseDate.setMinimumSize(new java.awt.Dimension(120, 20));
        txtAlbumReleaseDate.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumReleaseDate, gridBagConstraints);

        lblAlbumGenres.setText("Genres");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumGenres, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumGenres, gridBagConstraints);

        lblAlbumBarcode.setText("Catalogue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(lblAlbumBarcode, gridBagConstraints);

        txtAlbumCatalogue.setMinimumSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumCatalogue, gridBagConstraints);

        lblAlbumTracks.setText("Tracks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(lblAlbumTracks, gridBagConstraints);

        txtAlbumTracks.setMinimumSize(new java.awt.Dimension(50, 20));
        txtAlbumTracks.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumTracks, gridBagConstraints);

        lblAlbumRegion.setText("Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(lblAlbumRegion, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumRegion, gridBagConstraints);

        spacer12.setMinimumSize(new java.awt.Dimension(10, 14));
        spacer12.setPreferredSize(new java.awt.Dimension(10, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        add(spacer12, gridBagConstraints);

        spacer2.setMinimumSize(new java.awt.Dimension(90, 0));
        spacer2.setPreferredSize(new java.awt.Dimension(90, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        add(spacer2, gridBagConstraints);

        jLabel1.setText("Barcode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Label");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumPublisher, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        add(txtAlbumBarcode, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Occurs when a mouse button has been pressed on a component.
     *
     * @param e Event arguments.
     */
    @Override
    public void mousePressed(MouseEvent e) {

        if (mImage != null && e.getModifiers() == InputEvent.BUTTON1_MASK) {
            if ((e.getX() > mRect.x && e.getX() < mRect.x + mRect.width) 
                    && (e.getY() > mRect.y && e.getY() < mRect.y + mRect.height)) {
                if (!mImageUrl.isEmpty()) {
                    AlbumImage albumImage = new AlbumImage((JFrame)SwingUtilities.getWindowAncestor(this), true);
                    albumImage.showDialog(mImageUrl);
                }
            }
        }
    }

    /**
     * Occurs when a mouse button has been released on a component.
     *
     * @param e Event arguments.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Occurs when the mouse enters a component.
     *
     * @param e Event arguments.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Occurs when the mouse exits a component.
     *
     * @param e Event arguments.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Occurs when the mouse button has been clicked (pressed and released) on a component.
     * 
     * @param e Event arguments.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Occurs when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     *
     * @param e Event arguments.
     */
    @Override
    public void mouseMoved(MouseEvent e) {

        // Change the mouse cursor to Hand if mouse is over the thumbnail image
        if (mImage != null) {
            if ((e.getX() > mRect.x && e.getX() < mRect.x + mRect.width)
                    && (e.getY() > mRect.y && e.getY() < mRect.y + mRect.height)) {
                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        } else {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Occurs when a mouse button is pressed on a component and then dragged.
     *
     * @param e Event arguments.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    /**
     * Occurs when a item is selected from the combo box.
     *
     * @param evt Event arguments.
     */
    private void cmbAlbumDiscsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAlbumDiscsActionPerformed
        if (cmbAlbumDiscs.getSelectedItem() != null) {
            mCallback.discChanged(cmbAlbumDiscs.getSelectedItem().toString());
            mSelectedDisc = cmbAlbumDiscs.getSelectedIndex();
        }
    }//GEN-LAST:event_cmbAlbumDiscsActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Integer> cmbAlbumDiscs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAlbumArtists;
    private javax.swing.JLabel lblAlbumBarcode;
    private javax.swing.JLabel lblAlbumDiscs;
    private javax.swing.JLabel lblAlbumDuration;
    private javax.swing.JLabel lblAlbumFormat;
    private javax.swing.JLabel lblAlbumGenres;
    private javax.swing.JLabel lblAlbumName;
    private javax.swing.JLabel lblAlbumRegion;
    private javax.swing.JLabel lblAlbumReleaseDate;
    private javax.swing.JLabel lblAlbumTracks;
    private javax.swing.JLabel spacer12;
    private javax.swing.JLabel spacer2;
    private javax.swing.JTextField txtAlbumArtists;
    private javax.swing.JTextField txtAlbumBarcode;
    private javax.swing.JTextField txtAlbumCatalogue;
    private javax.swing.JTextField txtAlbumDuration;
    private javax.swing.JTextField txtAlbumFormat;
    private javax.swing.JTextField txtAlbumGenres;
    private javax.swing.JTextField txtAlbumName;
    private javax.swing.JTextField txtAlbumPublisher;
    private javax.swing.JTextField txtAlbumRegion;
    private javax.swing.JTextField txtAlbumReleaseDate;
    private javax.swing.JTextField txtAlbumTracks;
    // End of variables declaration//GEN-END:variables
}
