package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingClientImpl extends UnicastRemoteObject implements
        DrawingClient, DrawingSpaceListener {

    String fName;
    DrawingApp fDrawingApp;
    DrawingSpace fDrawingSpace;
    DrawingServer fServer;
    ExecutorService fThreadPool;

    protected DrawingClientImpl(DrawingApp app, DrawingSpace space,
            DrawingServer server) throws RemoteException {
        fName = "canvas" + UUID.randomUUID();
        fDrawingApp = app;
        fDrawingSpace = space;
        fServer = server;
        fThreadPool = Executors.newCachedThreadPool();

        fDrawingSpace.addDrawingSpaceListener(this);
        fServer.addDrawingClient(this);
    }

    @Override
    public String getName() throws RemoteException {
        return fName;
    }

    @Override
    synchronized public void receiveDrawingCommandFromServer(DrawingCommand cmd)
            throws RemoteException {
        fDrawingSpace.commitDrawingCommand(cmd);
        fDrawingApp.addDrawingCommand(cmd);
    }

    @Override
    public void drawingCommandExecuted(DrawingCommand cmd) {
        fThreadPool.execute(new DrawingCommandBroadcaster(fServer, fName, cmd));
    }
}
