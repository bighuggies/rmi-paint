package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingClientImpl extends UnicastRemoteObject implements
        DrawingClient, DrawingSpaceListener {

    DrawingSpace fDrawingSpace;
    DrawingServer fServer;

    protected DrawingClientImpl(DrawingSpace space, DrawingServer server)
            throws RemoteException {
        fDrawingSpace = space;
        fServer = server;

        fDrawingSpace.addDrawingSpaceListener(this);
        fServer.addDrawingServerListener(this);
    }

    @Override
    public String getName() throws RemoteException {
        return "canvas" + UUID.randomUUID();
    }

    @Override
    public void receiveDrawing(DrawingCommand cmd) throws RemoteException {
        fDrawingSpace.addDrawingCommand(cmd);
    }

    @Override
    public void drawingCommandExecuted(DrawingCommand command) {
        try {
            fServer.receiveDrawingCommand(this.getName(), command);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
