package service_interface;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

/**
 * Interface that is implemented by drawing commands. Known implementations are
 * Line, a command that allows users to draw point-to-point straight lines, and
 * Pen, a freehand drawing command.
 * 
 * DrawingCommand extends MouseListener and MouseMotionListener; DrawingCommand
 * implementations should implement the methods defined in these interfaces.
 * Alternatively DrawingCommand implementation classes may extend
 * AbstractDrawingCommand, which implements these methods by extending 
 * MouseAdapter. In this case, the subset of MouseListener and 
 * MouseMotionListener methods that are actually needed can be overridden with
 * a meaningful implementation.
 * 
 * DrawingCommand also extends marker interfaces Cloneable and Serializable. It
 * is therefore permitted to clone (copy) instances of classes that implement
 * DrawingCommand. It is also permitted to serialise instance of DrawingCommand
 * implementation classes.
 * 
 * Classes that implement DrawingCommand are expected to support cloning. A
 * DrawingCommand object is executed via MouseListener and MouseMotionListener
 * calls. Once execution is complete, a copy of the DrawingCommand is made, via
 * a call to clone(), the the copy can be stored in a list of executed 
 * commands. The original command can be executed an arbitrary times, a copy 
 * being each time is has finished executing and the copy stored in a list. To
 * show the effect of the many executions, the lists of clones can be iterated
 * and the draw() method called on each.
 * 
 * @author Ian Warren
 *
 */
public interface DrawingCommand extends MouseListener, MouseMotionListener,
		Cloneable, Serializable {

	/**
	 * Registers a DrawingCommandListener object with a DrawingCommand 
	 * instance.
	 */
	public void addDrawingCommandListener(DrawingCommandListener listener);
	
	/**
	 * Deregisters a DrawingCommandListener object from a DrawingCommand
	 * instance.
	 */
	public void removeDrawingCommandListener(DrawingCommandListener listener);
	
	/**
	 * Returns the name of a DrawingCommand object. The name returned is the
	 * name of the class of object on which the name() call is made. E.g. given
	 * an instance of class Pen, which implements DrawingCommand, calling 
	 * name() on the instance returns the String "Pen".
	 */
	public String name();
	
	/**
	 * Returns the screen cursor that should be displayed when using a 
	 * particular DrawingCommand object. 
	 */
	public Cursor cursor();

	/**
	 * Renders the effect of a DrawingCommand. If the DrawingCommand has 
	 * started but not yet finished executing, this method shows the progress
	 * of executing the command so far. If the command has completed execution,
	 * the final effect of executing the command is shown.
	 * 
	 * @param g the AWT/Swing Graphics object that provides drawing primitives
	 * used by implementations of this method.
	 */
	public void draw(Graphics g);

	/**
	 * Returns true if the DrawingCommand is currently in progress, false
	 * otherwise. A DrawingCommand is in progress if the user has started its
	 * execution but hasn't finished.
	 * 
	 * @return
	 */
	public boolean isExecuting();

	/**
	 * Returns a deep copy of a DrawingCommand object. If a DrawingCommand
	 * implementation defines only instance variables that are of primitive 
	 * data types, the default implementation inherited from class Object will 
	 * construct the deep copy. In other cases, where an implementation class 
	 * includes reference variables, the class should override method clone(). 
	 */
	public Object clone() throws CloneNotSupportedException;
}
