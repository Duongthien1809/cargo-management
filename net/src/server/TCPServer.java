package server;

import eventSystem.EventHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EventObject;

/**
 * <a href="https://www.geeksforgeeks.org/simple-calculator-using-tcp-java/?ref=gcse">...</a>
 */
public class TCPServer extends Thread implements Server {
    private final EventHandler handler;
    private final int port;

    public TCPServer(EventHandler handler, int port) {
        this.port = port;
        this.handler = handler;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            handleClientConnections(serverSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClientConnections(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            try (ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream())) {
                OutputStream outStream = socket.getOutputStream();

                while (socket.isConnected()) {
                    String reply = processIncomingEvent(inStream);
                    sendReply(outStream, reply);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processIncomingEvent(ObjectInputStream inStream) {
        try {
            EventObject event = (EventObject) inStream.readObject();
            return (event != null) ? handleEvent(event) : "got no event";
        } catch (IOException | ClassNotFoundException e) {
            return "error, got invalid event object";
        }
    }

    private void sendReply(OutputStream outStream, String reply) throws IOException {
        byte[] replyBytes = reply.getBytes();
        outStream.write(replyBytes);
        outStream.flush();
    }

    @Override
    public String handleEvent(EventObject event) {
        return this.handler.handleWithMsg(event);
    }
}
