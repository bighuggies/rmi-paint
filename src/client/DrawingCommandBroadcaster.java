package client;

import java.rmi.RemoteException;

import service_interface.DrawingCommand;
import service_interface.DrawingServer;

public class DrawingCommandBroadcaster implements Runnable {

    DrawingServer fServer;
    String fName;
    DrawingCommand fCmd;

    public DrawingCommandBroadcaster(DrawingServer server, String name,
            DrawingCommand cmd) {
        fServer = server;
        fName = name;
        fCmd = cmd;
    }

    @Override
    public void run() {
        try {
            fServer.broadcastDrawingCommand(fName, fCmd);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
