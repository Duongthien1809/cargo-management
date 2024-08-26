package client;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.EventObject;

public class UDPClient implements Client {
    private final int serverPort;
    private DatagramSocket socket;

    public UDPClient(int port) {
        this.serverPort = port;
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Couldn't start UDP socket", e);
        }
    }

    @Override
    public String sendEvent(EventObject event) {
        try {
            // Serialize EventObject to byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(event);
            os.flush();
            byte[] data = out.toByteArray();

            // Send DatagramPacket
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), this.serverPort);
            this.socket.send(sendPacket);

            // Receive response DatagramPacket
            byte[] buffer = new byte[1024];
            DatagramPacket incomingPacket = new DatagramPacket(buffer, buffer.length);
            this.socket.setSoTimeout(4000); // wait 4 sec for response

            try {
                this.socket.receive(incomingPacket);
                return new String(incomingPacket.getData()).trim();
            } catch (SocketTimeoutException e) {
                // No response, move on
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSocket(); // Close the socket
            recreateSocket(); // Recreate the socket for potential future use
        }
        return "";
    }


    public void closeSocket() {
        // Close the socket
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    // Method to recreate the socket
    public void recreateSocket() {
        if (socket.isClosed()) {
            try {
                this.socket = new DatagramSocket();
            } catch (SocketException e) {
                throw new RuntimeException("Couldn't recreate UDP socket", e);
            }
        }
    }
}
