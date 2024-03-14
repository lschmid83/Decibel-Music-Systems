package decibelrestjava.panels;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import javax.swing.*;

/**
 * Subclass of JTabbedPane which displays an input box allowing the user to
 * select paged results.
 */
@SuppressWarnings("serial")
public class TabbedPane extends JTabbedPane implements MouseMotionListener, MouseListener {

    /**
     * Stores the object which implements the page changed callback function.
     */
    private PageChangedCallback mCallback;
    /**
     * The icon displayed next to the page number.
     */
    private Image mIcon;
    /**
     * The text displayed.
     */
    private String mHeader;
    /**
     * The font used to draw the text.
     */
    private Font mFont;
    /**
     * Encapsulates information about the rendering of a particular font.
     */
    private FontMetrics mMetrics;
    /**
     * The current page number.
     */
    private int mCurrentPage;
    /**
     * The number of pages.
     */
    private int mPageTotal;

    /**
     * TabbedPage constructor.
     */
    public TabbedPane() {
        URL url = getClass().getResource("/decibelrestjava/resources/menu.png");
        mIcon = new ImageIcon(url).getImage();
        mFont = new Font("Tahoma", Font.PLAIN, 12);
        mHeader = "Page " + mCurrentPage + " of " + mPageTotal;
        mCurrentPage = 1;
        mPageTotal = 1;
    }
    
    /**
     * Initializes the mouse event listeners.
     */
    public void initMouseListeners()
    {
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /**
     * Initialize the page selected callback function.
     *
     * @param callback
     */
    public void initPageSelectedCallback(PageChangedCallback callback) {
        mCurrentPage = 1;
        mCallback = callback;
    }

    /**
     * Sets the page number.
     *
     * @param pageNumber The page number.
     */
    public void setPageNumber(int pageNumber) {
        mCurrentPage = pageNumber;
    }

    /**
     * Sets the total number of pages.
     *
     * @param pageCount The total number of pages.
     */
    public void setPageCount(int pageCount) {
        mPageTotal = pageCount;
        this.updateUI();
    }

    /**
     * Gets the current page number.
     *
     * @return The current page number.
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * Gets the total number of pages.
     *
     * @return The total number of pages.
     */
    public int getPageCount() {
        return mPageTotal;
    }

    /**
     * Draws the page number on the component.
     * 
     * @param g The Graphics object.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mPageTotal > 1) {
            mMetrics = this.getGraphics().getFontMetrics(mFont);
            mHeader = "Page " + mCurrentPage + " of " + mPageTotal;
            g.setFont(mFont);
            g.drawString(mHeader, (this.getWidth() - mMetrics.stringWidth(mHeader)) - 15, 15);
            g.drawImage(mIcon, (this.getWidth() - mMetrics.stringWidth(mHeader)) - 35, 3, this);
        }
    }

    /**
     * Occurs when the mouse cursor has been moved onto a component but no
     * buttons have been pushed.
     *
     * @param e Event arguments.
     */
    @Override   
    public void mouseMoved(MouseEvent e) {

        if (mPageTotal > 1) {
            if ((e.getX() > (this.getWidth() - mMetrics.stringWidth(mHeader)) - 35 && e.getX() < this.getWidth())
                    && (e.getY() > 0 && e.getY() < 20)) {
                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    /**
     * Occurs when the mouse button has been clicked (pressed and released) on a component.
     * 
     * @param e Event arguments.
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (mPageTotal > 1) {
            if ((e.getX() > (this.getWidth() - mMetrics.stringWidth(mHeader)) - 35 && e.getX() < this.getWidth())
                    && (e.getY() > 0 && e.getY() < 20)) {
                String userInput = JOptionPane.showInputDialog(null, "Please enter a page number.");
                if (userInput != null && !userInput.isEmpty()) {
                    try {
                        int pageNo = Integer.parseInt(userInput);
                        if (pageNo > 0 && pageNo <= mPageTotal) {
                            mCurrentPage = pageNo;
                            mCallback.pageChanged(mCurrentPage);
                            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            this.updateUI();
                        } else {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException er) {
                        JOptionPane.showMessageDialog(null, "Invalid page number.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
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
     * Occurs when a mouse button has been pressed on a component.
     *
     * @param e Event arguments.
     */
    @Override
    public void mousePressed(MouseEvent e) {
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
}
