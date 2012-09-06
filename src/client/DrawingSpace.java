package client;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import service_interface.DrawingCommand;
import service_interface.DrawingCommandListener;

/**
 * A DrawingSpace is essentially a canvas onto which DrawingCommand objects 
 * paint. A DrawingSpace object is registered as a DrawingCommandListener onto
 * DrawingCommand objects; whenever a DrawingCommand draws it notifies the 
 * DrawingSpace. A DrawingSpace object stores the effects of executing
 * DrawingCommands and renders these. The effect of executing a DrawingCommand
 * is represented by a clone of the executed DrawingCommand - see the API
 * description for DrawingCommand.
 * 
 * Entities that are interested in the changing state of a DrawingSpace object 
 * should implement the DrawingSpaceListener interface. A DrawingSpaceListener
 * can be registered with a DrawingSpace instance and notified of updates. This
 * facility may be useful as the application is evolved, e.g. to to track 
 * changes and update a remote server. 
 *  
 * @author Ian Warren
 *
 */
public class DrawingSpace extends JPanel implements DrawingCommandListener {
	private static Color BACKGROUND_COLOR = Color.WHITE;

	// A map of executed drawing commands, indexed by sequence number.
	private Map<Integer, DrawingCommand> fCommittedDrawingCommands; 
	
	private List<DrawingSpaceListener> fDrawingSpaceListeners;

	// Currently selected drawing command.
	private DrawingCommand fSelectedCommand;

	private DrawingApp fDrawingApp;

	/**
	 * Creates a DrawingSpace object.
	 */
	public DrawingSpace(DrawingApp drawingApp) {
		fCommittedDrawingCommands = new Hashtable<Integer, DrawingCommand>();
		fDrawingSpaceListeners = new ArrayList<DrawingSpaceListener>();
		fSelectedCommand = null;
		fDrawingApp = drawingApp;

		setBackground(BACKGROUND_COLOR);
	}

	/**
	 * Sets the DrawingCommand to be used to draw on this DrawingSpace object.
	 * Setting the DrawingCommand involves registering it as a MouseListener 
	 * and a MouseMotionListener so that the command will be notified of such
	 * events. The only DrawingCommand object that receives these events is the
	 * currently set command. In addition to registering the command as a 
	 * listener for mouse events, the command is given the opportunity to 
	 * change the screen cursor.
	 * @param cmd the DrawingCommand object to set.
	 * @throws IllegalArgumentException if some other DrawingCommand is currently
	 * in progress. In this case, this method has no effect.
	 */
	public void setDrawingCommand(DrawingCommand cmd) throws IllegalArgumentException {
		if(fSelectedCommand != null && fSelectedCommand.isExecuting()) {
			throw new IllegalArgumentException(
					"Unable to set new drawing command when another has not yet completed");
		}

		// Deregister the current drawing command so that it no longer 
		// processes mouse events.
		if (fSelectedCommand != null) {
			removeMouseListener(fSelectedCommand);
			removeMouseMotionListener(fSelectedCommand);
		}

		// Remember the new drawing command.
		fSelectedCommand = cmd;

		// Change the cursor.
		setCursor(cmd.cursor());

		// Register the newly set drawing command so that it receives mouse
		// events.
		addMouseListener(fSelectedCommand);
		addMouseMotionListener(fSelectedCommand);
	}

	/**
	 * Paints the effects of previously executed DrawingCommands, plus the 
	 * effect of the currently selected command if it is in progress.
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw all DrawingCommands that have finished executing.
		for (DrawingCommand cmd : fCommittedDrawingCommands.values()) {
			cmd.draw(g);
		}

		// Finally, draw any command that is in progress.
		if (fSelectedCommand != null && fSelectedCommand.isExecuting()) {
			fSelectedCommand.draw(g);
		}
	}

	/**
	 * Adds a DrawingSpaceListener to this DrawingSpace object.
	 */
	public void addDrawingSpaceListener(
			DrawingSpaceListener listener) {
		fDrawingSpaceListeners.add(listener);
	}
	
	/**
	 * Removes a DrawingSpaceListener from this DrawingSpace object.
	 */
	public void removeDrawingSpaceListener(
			DrawingSpaceListener listener) {
		fDrawingSpaceListeners.remove(listener);
	}
	
	/**
	 * DrawingCommandListener method that is called when a DrawingCommand 
	 * object has generated an event of interest to this DrawingSpace object.
	 */
	public void update(DrawingCommandEvent event, DrawingCommand cmd) {
		if(event == DrawingCommandEvent.DrawingStarted) {
			// No action necessary.
		} else if(event == DrawingCommandEvent.DrawingProgressed) {
			// Render the progress made by the DrawingCommand.
			repaint();
		} else if(event == DrawingCommandEvent.DrawingCompleted) {
			try {
				// Clone the DrawingCommand and store the copy; this represents
				// the effect of executing the command.
				fCommittedDrawingCommands.put(fCommittedDrawingCommands.size(), (DrawingCommand)cmd.clone());
				repaint();

				// Notify listeners that a DrawingCommand has completed execution.
				for (DrawingSpaceListener listener : fDrawingSpaceListeners) {
					listener.drawingCommandExecuted(cmd);
				}
			} catch (CloneNotSupportedException e) {
				// No action necessary. DrawingCommands are cloneable.
			}
		}
	}
}
