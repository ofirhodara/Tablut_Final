package GameView;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.*;
import ofirtablut.Controller.IController;

/**
 *
 * @author Ofir
 */
class BoardPanel extends JPanel {

    protected boolean isAI = false;
    protected boolean bufferDirty = true;
    private Image buffer;
    protected IController controller;

    public void setAI(boolean isAi) {
        this.isAI = isAi;
    }

    public void computerFirst() {
        controller.playAI();
    }

    /**
     * Paint a section of the board to the screen. Calling the drawBoard()
     * method if necessary.
     */
    public void paint(Graphics g) {

        // Check if buffer is null
        if (buffer == null) {
            buffer = this.createImage(getSize().width, getSize().height);
            bufferDirty = true;
        }

        // Repaint board if it has changed
        if (bufferDirty) {
            Graphics buf = buffer.getGraphics();
            buf.setClip(0, 0, buffer.getWidth(this), buffer.getHeight(this));
            drawBoard(buf);
            bufferDirty = false;
        }

        // Paint from our buffer to the screen
        g.drawImage(buffer, 0, 0, this);
    }

    /**
     * Update the image of the board. This is called by the paint()
     */
    public void drawBoard(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(this.getBackground());
        g.fillRect(clip.x, clip.y, clip.width, clip.height);
    }

}
