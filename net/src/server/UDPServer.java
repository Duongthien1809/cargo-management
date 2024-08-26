package server;

import eventSystem.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.EventObject;

public class UDPServer extends Thread implements Server {
    private static final int PACKET_SIZE = 10240;
    private final EventHandler handler;
    private final int port;

    public UDPServer(EventHandler handler, int port) {
        this.port = port;
        this.handler = handler;
    }

    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            handleClientConnections(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClientConnections(DatagramSocket socket) {
        try {
            byte[] incomingData = new byte[PACKET_SIZE];

            while (true) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);

                InetAddress clientAddress = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();

                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);

                String reply = processIncomingEvent(is);
                sendReply(socket, reply, clientAddress, clientPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String processIncomingEvent(ObjectInputStream is) {
        try {
            EventObject event = (EventObject) is.readObject();
            return (event != null) ? handleEvent(event) : "got no event";
        } catch (IOException | ClassNotFoundException e) {
            return "error, got an invalid event object";
        }
    }

    private void sendReply(DatagramSocket socket, String reply, InetAddress clientAddress, int clientPort) throws IOException {
        byte[] replyBytes = reply.getBytes();
        DatagramPacket replyPacket = new DatagramPacket(replyBytes, replyBytes.length, clientAddress, clientPort);
        socket.send(replyPacket);
    }

    @Override
    public String handleEvent(EventObject event) {
        return this.handler.handleWithMsg(event);
    }
}
