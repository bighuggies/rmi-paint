package service_interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DrawingClient extends Remote {
    String getName() throws RemoteException;

    void receiveDrawing(DrawingCommand cmd) throws RemoteException;
}
