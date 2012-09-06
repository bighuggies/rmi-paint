package client.commands;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import service_interface.DrawingCommandListener.DrawingCommandEvent;


/** 
 * Implementation of the DrawingCommand interface that allows the user to draw
 * freehand.
 * 
 * @author Ian Warren
 *
 */
public class Pen extends AbstractDrawingCommand {
	// Constants associated with dynamic array data structure.
	private static final int ARRAY_INITIAL_SIZE = 64;
	private static final int ARRAY_INCREMENT_SIZE = 16;

	// Attributes to record the pens's state.
	private int[] fXPoints;
	private int[] fYPoints;
	int fIndex;

	// Attribute used to track whether or not the Pen command has started
	// but not yet finished executing.
	private boolean fIsExecuting;
	
	/**
	 * Creates a Pen DrawingCommand instance.
	 */
	public Pen() {
		fIsExecuting = false;
		fXPoints = null;
		fYPoints = null;
		fIndex = 0;
	}

	/**
	 * Processes a mouse-pressed event. This causes the Pen command to start 
	 * executing.
	 */
	public void mousePressed(MouseEvent event) {
		fXPoints = new int[ARRAY_INITIAL_SIZE];
		fYPoints = new int[ARRAY_INITIAL_SIZE];
		fIndex = 0;

		storePoint(event);

		// Notify any registered listeners that this command has started executing.
		this.updateListeners(DrawingCommandEvent.DrawingStarted);
		fIsExecuting = true;
	}

	/**
	 * Processes a mouse-released event. This causes the Pen command to 
	 * complete execution.
	 */
	public void mouseReleased(MouseEvent event) {
		if (fIndex == fXPoints.length) {
			grow();
		}
		storePoint(event);

		// Notify any registered listeners that this command has stopped executing.
		this.updateListeners(DrawingCommandEvent.DrawingCompleted);
		fIsExecuting = false;
	}

	/**
	 * Processes a mouse-dragged event. This occurs between execution start and
	 * completion, when the user holds down the mouse button and drags the 
	 * mouse.
	 */
	public void mouseDragged(MouseEvent event) {
		if (fIndex == fXPoints.length) {
			grow();
		}
		storePoint(event);

		// Notify any registered listeners that this command has made progress.
		this.updateListeners(DrawingCommandEvent.DrawingProgressed);
	}

	/**
	 * Returns a deep copy of a Pen object.
	 */
	public Object clone() throws CloneNotSupportedException {
		Pen copy = (Pen) super.clone();
		copy.fXPoints = Arrays.copyOf(fXPoints, fIndex);
		copy.fYPoints = Arrays.copyOf(fYPoints, fIndex);
		return copy;
	}

	/**
	 * Paints the progress/effect of executing a Pen command.
	 */
	public void draw(Graphics g) {
		g.drawPolyline(fXPoints, fYPoints, fIndex);
	}

	/**
	 * Returns true if the Pen command has started but not finished execution,
	 * false otherwise.
	 */
	public boolean isExecuting() {
		return fIsExecuting;
	}

	/**
	 * Returns true if this Pen object has identical state to the object 
	 * supplied as argument, false otherwise.
	 */
	public boolean equals(Object other) {
		boolean result = false;
		
		if(other != null && other instanceof Pen) {
			
			Pen otherPen = (Pen)other;
			result = Arrays.equals(fXPoints, otherPen.fXPoints) && 
				Arrays.equals(fXPoints, otherPen.fXPoints) &&
				fIndex == otherPen.fIndex &&
				fIsExecuting == otherPen.fIsExecuting;
		}
		return result;
	}
	
	/**
	 * Implementation method to increase the capacity of the array used to 
	 * store points of freehand drawing.
	 */
	private void grow() {
		fXPoints = Arrays.copyOf(fXPoints, fIndex + ARRAY_INCREMENT_SIZE);
		fYPoints = Arrays.copyOf(fYPoints, fIndex + ARRAY_INCREMENT_SIZE);
	}

	/**
	 * Implementation method to append a point.
	 */
	private void storePoint(MouseEvent event) {
		fXPoints[fIndex] = event.getX();
		fYPoints[fIndex] = event.getY();
		fIndex++;
	}
}
