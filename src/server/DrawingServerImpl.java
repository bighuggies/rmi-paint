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

    private ArrayList<DrawingClient> fClients = new ArrayList<DrawingClient>();
    ArrayList<DrawingCommand> fCompletedDrawingCommands = new ArrayList<DrawingCommand>();
    ExecutorService fThreadPool;

    public DrawingServerImpl() throws RemoteException {
        fThreadPool = Executors.newCachedThreadPool();
    }

    @Override
    synchronized public void addDrawingClient(DrawingClient client)
            throws RemoteException {
        fClients.add(client);

        for (DrawingCommand cmd : fCompletedDrawingCommands) {
            fThreadPool.execute(new DrawingCommandDispatcher("", client, cmd));
        }

        System.out.println("Registered client " + client.getName());
    }

    @Override
    synchronized public void removeDrawingClient(DrawingClient client)
            throws RemoteException {
        System.out.println("De-registering client.");
        fClients.remove(client);
    }

    @Override
    synchronized public void broadcastDrawingCommand(String sender,
            DrawingCommand cmd) throws RemoteException {
        System.out.println("Received command " + cmd.name() + " from " + sender);

        fCompletedDrawingCommands.add(cmd);

        for (DrawingClient client : fClients) {
            fThreadPool.execute(new DrawingCommandDispatcher(sender, client,
                    cmd));
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

            String registryHost = "localhost";
            int registryPort = 1099;

            if (args.length == 2) {
                registryHost = args[0];
                registryPort = Integer.parseInt(args[1]);
            }

            Registry r = LocateRegistry.getRegistry(registryHost, registryPort);
            r.rebind("drawingserver", new DrawingServerImpl());
            System.out.println("Bound drawing server");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
