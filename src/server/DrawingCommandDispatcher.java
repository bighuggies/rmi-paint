package server;

import java.rmi.RemoteException;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;

/**
 * Dispatch a finished drawing command to a specific drawing client.
 */
public class DrawingCommandDispatcher implements Runnable {

    String fSender;
    DrawingClient fClient;
    DrawingCommand fCmd;

    /**
     * Creates a new command dispatcher.
     * 
     * @param sender
     *            The originator of the command. We need to know this so that we
     *            do not retransmit the command back to the originator.
     * @param client
     *            The client to send the drawing command to.
     * @param cmd
     *            The drawing command to send to the client.
     */
    public DrawingCommandDispatcher(String sender, DrawingClient client,
            DrawingCommand cmd) {
        fSender = sender;
        fClient = client;
        fCmd = cmd;
    }

    @Override
    public void run() {
        try {
            // Don't send the command if it came from the client.
            if (!fSender.equalsIgnoreCase(fClient.getName()))
                fClient.receiveDrawingCommandFromServer(fCmd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
