package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingServerImpl extends UnicastRemoteObject implements
        DrawingServer {

    protected DrawingServerImpl() throws RemoteException {
        super();
    }

    private ArrayList<DrawingClient> fClients = new ArrayList<DrawingClient>();

    @Override
    public void addDrawingServerListener(DrawingClient dsl)
            throws RemoteException {
        fClients.add(dsl);
    }

    @Override
    public void removeDrawingServerListener(DrawingClient dsl)
            throws RemoteException {
        fClients.remove(dsl);
    }

    @Override
    public void receiveDrawingCommand(String sender, DrawingCommand cmd)
            throws RemoteException {

        System.out.println("Received command from " + sender);
        for (DrawingClient c : fClients) {
            if (!sender.equalsIgnoreCase(c.getName()))
                sendDrawingCommand(c, cmd);
        }
    }

    @Override
    public void sendDrawingCommand(DrawingClient client, DrawingCommand cmd)
            throws RemoteException {
        client.receiveDrawing(cmd);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            
            Registry r = LocateRegistry.getRegistry();
            r.rebind("drawingserver", new DrawingServerImpl());
            System.out.println("Bound drawing server");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}
