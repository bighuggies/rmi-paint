package client.commands;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import service_interface.DrawingCommandListener.DrawingCommandEvent;

/**
 * Implementation of the DrawingCommand interface that allows the user to draw a
 * point-to-point straight line.
 * 
 * @author Ian Warren
 * 
 */
public class Line extends AbstractDrawingCommand {
    // Attribute used to track whether or not the Line command has started
    // but not yet finished executing.
    private boolean fIsExecuting;

    // Attributes to record the line's state.
    private int fStartX;
    private int fStartY;
    private int fEndX;
    private int fEndY;

    // Attributes storing previous end-point of a line, used when dragging the
    // mouse.
    private int fFormerEndX;
    private int fFormerEndY;

    /**
     * Creates a Line DrawingCommand instance.
     */
    public Line() {
        fIsExecuting = false;
    }

    /**
     * Returns the "crosshair" cursor.
     */
    public Cursor cursor() {
        return new Cursor(Cursor.CROSSHAIR_CURSOR);
    }

    /**
     * Processes a mouse-pressed event. This causes the Line command to start
     * executing.
     */
    public void mousePressed(MouseEvent event) {
        fStartX = event.getX();
        fStartY = event.getY();

        // Notify any registered listeners that this command has started
        // executing.
        this.updateListeners(DrawingCommandEvent.DrawingStarted);
        fIsExecuting = true;
    }

    /**
     * Processes a mouse-released event. This causes the Line command to
     * complete execution.
     */
    public void mouseReleased(MouseEvent event) {
        fEndX = event.getX();
        fEndY = event.getY();

        // Notify any registered listeners that this command has stopped
        // executing.
        this.updateListeners(DrawingCommandEvent.DrawingCompleted);
        fIsExecuting = false;
    }

    /**
     * Processes a mouse-dragged event. This occurs between execution start and
     * completion, when the user holds down the mouse button and drags the
     * mouse.
     */
    public void mouseDragged(MouseEvent event) {
        fFormerEndX = fEndX;
        fFormerEndY = fEndY;

        fEndX = event.getX();
        fEndY = event.getY();

        // Notify any registered listeners that this command has made progress.
        this.updateListeners(DrawingCommandEvent.DrawingProgressed);
    }

    /**
     * Paints the progress/effect of executing a Line command.
     */
    public void draw(Graphics g) {
        if (isExecuting()) {
            // Command has not yet completed, so paint its progress.
            Color currentColor = g.getColor();
            g.setColor(Color.WHITE);
            g.drawLine(fStartX, fStartY, fFormerEndX, fFormerEndY);
            g.setColor(currentColor);
            g.drawLine(fStartX, fStartY, fEndX, fEndY);
        } else {
            // Command has completed, so paint the effect of its execution.
            g.drawLine(fStartX, fStartY, fEndX, fEndY);
        }
    }

    /**
     * Returns true if the Line command has started but not finished execution,
     * false otherwise.
     */
    public boolean isExecuting() {
        return fIsExecuting;
    }

    /**
     * Returns true if this Line object has identical state to the object
     * supplied as argument, false otherwise.
     */
    public boolean equals(Object other) {
        boolean result = false;

        if (other != null && other instanceof Line) {
            Line otherLine = (Line) other;
            result = fStartX == otherLine.fStartX
                    && fStartY == otherLine.fStartY && fEndX == otherLine.fEndX
                    && fEndY == otherLine.fStartY
                    && fFormerEndX == otherLine.fFormerEndX
                    && fFormerEndY == otherLine.fFormerEndY
                    && fIsExecuting == otherLine.fIsExecuting;
        }
        return result;
    }
}
