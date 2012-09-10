package service_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Remote server to facilitate the broadcast of finished drawing commands
 * amongst a group of remote drawing applications.
 * 
 * @author Andrew
 * 
 */
public interface DrawingServer extends Remote {
    /**
     * Whenever a drawing client starts up it will attempt to register with the
     * server by calling this method.
     * 
     * @param client
     *            The client which wishes to register with the server.
     * @throws RemoteException
     */
    public void addDrawingClient(DrawingClient client) throws RemoteException;

    /**
     * Removes a drawing client.
     * 
     * @param client
     * @throws RemoteException
     */
    public void removeDrawingClient(DrawingClient client)
            throws RemoteException;

    /**
     * Once a client has finished executing a drawing command, it will call this
     * method in order to broadcast the drawing command to the rest of the
     * registered drawing clients.
     * 
     * @param sender
     *            The origin of the drawing command.
     * @param cmd
     *            The finished drawing command to be broadcast.
     * @throws RemoteException
     */
    public void broadcastDrawingCommand(String sender, DrawingCommand cmd)
            throws RemoteException;
}
