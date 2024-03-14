package decibelrestjava;

import decibelrestjava.webclient.WebClient;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import queryresultholder.Album;
import queryresultholder.ExternalIdentifier;
import queryresultholder.Publication;

/**
 * Contains utility functions.
 */
public class Util {

    /**
     * Deserializes an XML string into an object of type T.
     *
     * @param <T> The type of object.
     * @param resultClass The result class.
     * @param str The XML string.
     * @return The object representation of the XML.
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeXmlString(Class<T> resultClass, String str) throws JAXBException {

        // Convert result string to InputSource
        InputSource is = new InputSource(new StringReader(str));

        // Create a JAXB context and unmarshaller
        JAXBContext context = JAXBContext.newInstance(resultClass);
        Unmarshaller handler = context.createUnmarshaller();

        // Deserialize the XML stream 
        return (T) handler.unmarshal(is);
    }

    /**
     * Sets the WebClient request headers.
     *
     * @param client The WebClient object.
     */
    public static void setDecibelRequestHeaders(WebClient client) {
        client.Headers.put("DecibelAppID", Settings.ApplicationID);
        client.Headers.put("DecibelAppKey", Settings.ApplicationKey);
        client.Headers.put("DecibelTimestamp", new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
    }

    /**
     * Gets the album publishers from publication information.
     *
     * @param Album The Album object.
     * @return A list of publisher names.
     */
    public static List<String> getAlbumPublishers(Album album) {

        List<String> publishers = new ArrayList<>();
        if (album.getPublications() != null) {
            for (Publication publication : album.getPublications().getPublication()) {
                publishers.add(publication.getPublisher().getName());
            }
        }
        return publishers;
    }

    /**
     * Gets the album barcodes from the external identifier information.
     *
     * @param Album The Album object.
     * @return A list of barcodes.
     */
    public static List<String> getAlbumBarcodes(Album album) {
        List<String> barcodes = new ArrayList<>();
        if (album.getExternalIdentifiers() != null) {
            for (ExternalIdentifier externalIdentifier : album.getExternalIdentifiers().getExternalIdentifier()) {
                if (externalIdentifier.getExternalDatabase().getName().equals("UPC Barcode")) {
                    barcodes.add(externalIdentifier.getIdentifier());
                }
            }
        }
        return barcodes;
    }

    /**
     * Returns the first two retrieval depths from a string.
     *
     * @param retrievalDepth The string containing retrieval depths.
     * @return The truncated retrieval depth string.
     */
    public static String getSearchDepthTruncated(String retrievalDepth) {
        String[] depth = retrievalDepth.split(";");
        if (depth.length >= 4) {
            return depth[0] + ";" + depth[1] + "...";
        } else {
            return retrievalDepth;
        }
    }

    /**
     * Converts an input stream to a string.
     *
     * @param is The input stream.
     * @return The input stream converted to a string.
     * @throws IOException
     */
    public static String inputStreamToString(InputStream is)
            throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

    /**
     * Prints the contents of a component scaled to fit the page.
     *
     * @param component The component to print.
     */
    public static void printComponent(final Component component) {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Print Component ");

        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) pg;
                double factorX = pf.getImageableWidth() / component.getWidth();
                double factorY = pf.getImageableHeight() / component.getHeight();
                double factor = Math.min(factorX, factorY);
                g2.scale(factor, factor);
                g2.translate(pf.getImageableX(), pf.getImageableY());
                component.paint(g2);
                return Printable.PAGE_EXISTS;
            }
        });

        if (pj.printDialog() == false) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(null, "Unable to print component." 
                    + System.lineSeparator() + ex.toString());
        }
    }

    /**
     * Displays a save file dialog and saves a string to a file.
     *
     * @param text The string to write to file.
     */
    public static void saveAs(Component component, String text) {

        //Create a file chooser
        final JFileChooser fc = new JFileChooser();

        // Set the file filter
        fc.removeChoosableFileFilter(fc.getFileFilter());
        FileNameExtensionFilter ff = new FileNameExtensionFilter("XML File", "xml");
        fc.addChoosableFileFilter(ff);

        int returnVal = fc.showSaveDialog(component);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String fp = file.toString();
            try (PrintStream out = new PrintStream(new FileOutputStream(fp))) {
                out.print(text);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Unable to save document." 
                    + System.lineSeparator() + e.toString());
            }
        }
    }

    /**
     * Converts seconds to hh:mm:ss.
     *
     * @param seconds Total number of seconds.
     * @return Seconds converted to a string in the format hh:mm:ss.
     */
    public static String formatTime(int seconds) {
        int hours = seconds / 3600,
                remainder = seconds % 3600,
                min = remainder / 60,
                sec = remainder % 60;
        return ((hours < 10 ? "0" : "") + hours
                + ":" + (min < 10 ? "0" : "") + min
                + ":" + (sec < 10 ? "0" : "") + sec);
    }

    /**
     * Sets the location of a JFrame to the centre of the screen.
     *
     * @param frame The frame to centre.
     */
    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     * Indicates whether a specified string is null, empty, or consists only of
     * white-space characters.
     *
     * @param param The string to test.
     * @return True if the value parameter is null or String.Empty, or if value
     * consists exclusively of white-space characters.
     */
    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Sets a JTable column width.
     *
     * @param table The JTable to modify.
     * @param column The column index.
     * @param width The new width.
     */
    public static void setTableColumnWidth(JTable table, int column, int width) {
        TableColumn col = table.getColumnModel().getColumn(column);
        col.setPreferredWidth(width);
    }
}