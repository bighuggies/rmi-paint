package client.commands;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import service_interface.DrawingCommand;
import service_interface.DrawingCommandListener;
import service_interface.DrawingCommandListener.DrawingCommandEvent;

/**
 * Convenience class that provides a partial implementation of the
 * DrawingCommand interface. DrawingCommand implementations will typically
 * extend AbstractDrawingCommand rather than implement the interface from
 * scratch.
 * 
 * @author Ian Warren
 * 
 */
public abstract class AbstractDrawingCommand extends MouseAdapter implements
        DrawingCommand {

    // List of registered listeners. Note that this member is transient - so
    // if an instance of a subclass of AbstractDrawingCommand is serialized the
    // list of DrawingCommandListener objects will not be serialised.
    protected transient List<DrawingCommandListener> fDrawingCommandListeners;

    public AbstractDrawingCommand() {
        fDrawingCommandListeners = new ArrayList<DrawingCommandListener>();
    }

    /**
     * Returns the name of a DrawingCommand. The value of the String returned is
     * the name of the class of which the DrawingCommand object is an instance
     * of. E.g. given an instance of Pen (a known subclass of
     * AbstractDrawingCommand), name() would return "Pen".
     */
    public String name() {
        String commandName = getClass().getName();
        return commandName.substring(commandName.lastIndexOf('.') + 1);
    }

    /**
     * Adds a DrawingCommandListener,e.g. a DrawingSpace object, to a
     * DrawingCommand instance.
     */
    public void addDrawingCommandListener(DrawingCommandListener listener) {
        fDrawingCommandListeners.add(listener);
    }

    /**
     * Removes a specified DrawingCommandListener instance.
     */
    public void removeDrawingCommandListener(DrawingCommandListener listener) {
        fDrawingCommandListeners.remove(listener);
    }

    /**
     * Returns the default (pointer) cursor.
     */
    public Cursor cursor() {
        return new Cursor(Cursor.DEFAULT_CURSOR);
    }

    /**
     * Returns a copy of a DrawingCommand object. This method should be
     * overridden by subclasses if necessary to ensure a deep copy is made.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Intended to be called by subclasses to notify registered listeners of the
     * progress of executing a DrawingCommand.
     * 
     * @param event
     */
    protected void updateListeners(DrawingCommandEvent event) {
        for (DrawingCommandListener listener : fDrawingCommandListeners) {
            listener.update(event, this);
        }
    }

    /**
     * Implementation of readObject(), to be called as part of a customised
     * deserialisation process for instances of subclasses of
     * AbstractDrawingCommand. This method simply ensures that a deserialised
     * instance has a non-null value for the transient member
     * fDrawingCommandListeners.
     */
    private void readObject(ObjectInputStream stream) throws IOException,
            ClassNotFoundException {
        fDrawingCommandListeners = new ArrayList<DrawingCommandListener>();
    }
}
