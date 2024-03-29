package decibelrestjava.frames;

import decibelrestjava.Settings;
import decibelrestjava.Util;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Displays album cover art.
 */
@SuppressWarnings("serial")
public class AlbumImage extends javax.swing.JDialog {

    /**
     * The album cover art image.
     */
    private BufferedImage mImage;
    /**
     * The image not available graphic.
     */
    private Image mUnavailable;
    /**
     * The animated loading GIF.
     */
    private Image mLoader;
    /**
     * The object we will use to write with instead of the standard screen
     * graphics.
     */
    private Graphics bufferGraphics;
    /**
     * The image that will contain everything that has been drawn on
     * bufferGraphics.
     */
    private Image offscreen;
    /**
     * Is the cover art image downloading.
     */
    private boolean mDownloading;

    /**
     * Creates new form AlbumImage.
     */
    public AlbumImage(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();

        this.setSize(480, 450);

        // Animated loading gif
        URL url = getClass().getResource("/decibelrestjava/resources/loading.gif");
        mLoader = Toolkit.getDefaultToolkit().createImage(url);
        
        // No image available graphic
        url = getClass().getResource("/decibelrestjava/resources/unavailable.png");
        mUnavailable = new ImageIcon(url).getImage();

        // Create an offscreen image to draw on 
        offscreen = createImage(this.getWidth(), this.getHeight());

        // Get the offscreen image graphics context
        bufferGraphics = offscreen.getGraphics();
    }

    /**
     * Shows album image dialog.
     *
     * @param imageUrl The cover art image URL.
     */
    public void showDialog(final String imageUrl) {

        mDownloading = true;
        
        // Download cover art image in background thread
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        
                        String error = null;
                        try {
                            URLConnection urlConnection = new URL(imageUrl).openConnection();
                            urlConnection.setRequestProperty("DecibelAppID", Settings.ApplicationID);
                            urlConnection.setRequestProperty("DecibelAppKey", Settings.ApplicationKey);
                            urlConnection.setRequestProperty("DecibelTimestamp", new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
                            mImage = ImageIO.read(urlConnection.getInputStream());
                            mDownloading = false;
                        } catch (Exception e) {
                            error = e.toString();
                        }
                        
                        if (error != null) {
                            JOptionPane.showMessageDialog(null, "The Decibel Web Service request "
                                    + "was not successful due to the following error:"
                                    + System.lineSeparator() + error);
                        }
                    }
                }).start();
        
        Util.centreWindow(this);
        this.setVisible(true);
    }

    /**
     * Draws the cover art image on the panel.
     */
    @Override
    public void paint(Graphics g) {

        if(mDownloading)
        {
            // Draw loading animation
            bufferGraphics.setColor(Color.WHITE);
            bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
            bufferGraphics.drawImage(mLoader, (this.getWidth() / 2) - (mLoader.getWidth(this) / 2),
                    (this.getWidth() / 2) - (mLoader.getHeight(this) / 2), this);
        }
        else
        {
            if(mImage != null) // Draw cover art image
            {
                bufferGraphics.drawImage(mImage, 0, 25, this.getWidth(), this.getHeight(), this);     
            }
            else // No image available
            {
                bufferGraphics.drawImage(mUnavailable, (this.getWidth() / 2) - (mUnavailable.getWidth(this) / 2),
                                                       (this.getWidth() / 2) - (mUnavailable.getHeight(this) / 2),  this);     
            }
        }
        
        g.drawImage(offscreen, 0, 0, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cover Art");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        getContentPane().add(jLabel1, new java.awt.GridBagConstraints());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
