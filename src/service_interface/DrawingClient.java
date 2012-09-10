package service_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface to facilitate the receival of finished drawing commands from
 * a drawing server.
 * 
 * @author Andrew
 * 
 */
public interface DrawingClient extends Remote {
    /**
     * Return a unique name for the drawing client.
     * 
     * @return A name uniquely identifying the drawing client.
     * @throws RemoteException
     */
    String getName() throws RemoteException;

    /**
     * When a drawing command is executed on another client the server
     * broadcasts it to each other client by calling this method.
     * 
     * @param cmd
     *            The finished drawing command which was executed on another
     *            client.
     * @throws RemoteException
     */
    void receiveDrawingCommandFromServer(DrawingCommand cmd)
            throws RemoteException;
}
