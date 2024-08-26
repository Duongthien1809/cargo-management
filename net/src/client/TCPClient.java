package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.EventObject;

/**
 * <a href="https://www.geeksforgeeks.org/simple-calculator-using-tcp-java/?ref=gcse">...</a>
 */
public class TCPClient implements Client {
    private Socket socket;
    private InputStream inStream;
    private ObjectOutputStream outStream;

    public TCPClient(int port) {
        connectSocket(port);
    }

    private void connectSocket(int port) {
        try {
            socket = new Socket(InetAddress.getLocalHost(), port);
            inStream = socket.getInputStream();
            outStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            handleConnectionError(e);
        }
    }

    public String sendEvent(EventObject event) {
        try {
            checkSocketConnection();

            outStream.writeObject(event);
            outStream.flush();

            byte[] responseBuffer = new byte[1024];
            inStream.read(responseBuffer);
            return new String(responseBuffer, StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            handleCommunicationError(e);
        }
        return "";
    }

    private void checkSocketConnection() throws IOException {
        if (!socket.isConnected()) {
            throw new IOException("Socket not connected. Initialize the socket before sending an event.");
        }
    }

    private void handleConnectionError(IOException e) {
        e.printStackTrace();
        System.exit(1);
    }

    private void handleCommunicationError(IOException e) {
        e.printStackTrace();
    }
}
