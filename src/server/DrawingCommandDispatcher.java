package server;

import java.rmi.RemoteException;

import service_interface.DrawingClient;
import service_interface.DrawingCommand;

public class DrawingCommandDispatcher implements Runnable {

    String fSender;
    DrawingClient fClient;
    DrawingCommand fCmd;

    public DrawingCommandDispatcher(String sender, DrawingClient client,
            DrawingCommand cmd) {
        fSender = sender;
        fClient = client;
        fCmd = cmd;
    }

    @Override
    public void run() {
        try {
            if (!fSender.equalsIgnoreCase(fClient.getName()))
                fClient.receiveDrawingCommandFromServer(fCmd);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
