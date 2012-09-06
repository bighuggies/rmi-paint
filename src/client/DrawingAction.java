package client;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import service_interface.DrawingCommand;

/**
 * Implementation of Swing's Action interface. An instance of this class is 
 * intended to be plugged into a menu item or button. When the menu item is
 * selected, or when the button is pressed, the DrawingAction object's 
 * actionPerformed method will be called. A DrawingAction object maintains, as
 * part of its state a reference to a DrawingCommand instance. In response to
 * an actionPerformed call, a DrawingAction object simply sets its 
 * DrawingCommand instance as the current DrawingCommand for a particular 
 * DrawingSpace component.
 * 
 * @author Ian Warren
 *
 */
public class DrawingAction extends AbstractAction {

	private DrawingSpace fDrawingSpace;
	private DrawingCommand fCommand;

	/**
	 * Creates a DrawingAction object with specified DrawingSpace and 
	 * DrawingCommand objects. 
	 */
	public DrawingAction(DrawingSpace drawingSpace, DrawingCommand cmd) {
		fDrawingSpace = drawingSpace;
		fCommand = cmd;
		
		// Store the name of this DrawingAction object. Menu items and buttons
		// retrieve this stored value to determine what text to display on the
		// menu item / button.
		putValue(AbstractAction.NAME, cmd.name());
	}

	/**
	 * Sets this DrawingAction's DrawingCommand object as the current drawing
	 * command on this DrawingAction's DrawingSpace component.
	 */
	public void actionPerformed(ActionEvent arg0) {
		try {
			fDrawingSpace.setDrawingCommand(fCommand);
		} catch(IllegalArgumentException e) {
			// Another drawing command has started execution and has not yet 
			// completed. This represents an error with the implementation of
			// the DrawingApp client program.
			System.err.println("Illegal attempt to set drawing command.");
			e.printStackTrace();
		}
	}

	/** 
	 * Returns the DrawingCommand object encapsulated by this DrawingAction.
	 */
	public DrawingCommand drawingCommand() {
		return fCommand;
	}
}
