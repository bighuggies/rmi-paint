package service_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DrawingServer extends Remote {
    public void addDrawingServerListener(DrawingClient dsl)
            throws RemoteException;

    public void removeDrawingServerListener(DrawingClient dsl)
            throws RemoteException;

    public void receiveDrawingCommand(String sender, DrawingCommand cmd) throws RemoteException;

    public void sendDrawingCommand(DrawingClient client, DrawingCommand cmd) throws RemoteException;
}
