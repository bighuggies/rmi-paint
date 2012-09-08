package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingServerImpl extends UnicastRemoteObject implements
        DrawingServer {

    ExecutorService fThreadPool;

    protected DrawingServerImpl() throws RemoteException {
        fThreadPool = Executors.newCachedThreadPool();
    }

    private ArrayList<DrawingClient> fClients = new ArrayList<DrawingClient>();

    @Override
    synchronized public void addDrawingClient(DrawingClient client)
            throws RemoteException {
        fClients.add(client);
        System.out.println("Registered client " + client.getName());
    }

    @Override
    synchronized public void removeDrawingClient(DrawingClient client)
            throws RemoteException {
        fClients.remove(client);
    }

    @Override
    public void broadcastDrawingCommand(String sender, DrawingCommand cmd)
            throws RemoteException {

        System.out.println("Received command from " + sender);
        for (DrawingClient client : fClients) {
            fThreadPool.execute(new DrawingCommandDispatcher(sender, client, cmd));
        }
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
