package client;

import service_interface.DrawingCommand;

/**
 * Listener interface intended to be implemented by classes whose instances are
 * interested in the changing state of a DrawingSpace object. Specifically, a 
 * registered DrawingSpaceListener is notified whenever a DrawingCommand 
 * completes execution.
 * 
 * @author Ian Warren
 *
 */
public interface DrawingSpaceListener {
	/**
	 * Called when a specified DrawingCommand has completed executing.
	 */
	void drawingCommandExecuted(DrawingCommand command);
}
