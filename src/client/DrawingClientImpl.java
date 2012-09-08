package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingClientImpl extends UnicastRemoteObject implements
        DrawingClient, DrawingSpaceListener {

    String fName;
    DrawingApp fDrawingApp;
    DrawingSpace fDrawingSpace;
    DrawingServer fServer;

    protected DrawingClientImpl(DrawingApp app, DrawingSpace space,
            DrawingServer server) throws RemoteException {
        fDrawingApp = app;
        fDrawingSpace = space;
        fServer = server;
        fName = "canvas" + UUID.randomUUID();

        fDrawingSpace.addDrawingSpaceListener(this);
        fServer.addDrawingServerListener(this);
    }

    @Override
    public String getName() throws RemoteException {
        return fName;
    }

    @Override
    synchronized public void receiveDrawingCommandFromServer(DrawingCommand cmd)
            throws RemoteException {
        fDrawingSpace.addCompletedDrawingCommand(cmd);
        fDrawingApp.addDrawingCommand(cmd);
    }

    @Override
    public void drawingCommandExecuted(DrawingCommand command) {
        try {
            fServer.broadcastDrawingCommand(this.getName(), command);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
