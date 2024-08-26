import cargo.Cargo;
import cli.CLI;
import client.TCPClient;
import client.UDPClient;
import eventSystem.EventHandler;
import eventSystem.ObserverPattern.CargoObserver;
import eventSystem.ObserverPattern.Observer;
import eventSystem.eventListeners.GLEventListener;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;
import server.TCPServer;
import server.UDPServer;

import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        //init Warehouse Facade
        CustomerWarehouse customerWarehouse = new CustomerWarehouse();
        customerWarehouse.addCustomer("Alice");
        customerWarehouse.addCustomer("Bob");
        CargoWarehouse<Cargo> cargoWarehouse = new CargoWarehouse<>(12);
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(customerWarehouse, cargoWarehouse);
        //init event handler and add listener
        EventHandler eventHandler = new EventHandler();
        eventHandler.add(new GLEventListener<>(warehouseFacade));
        //init observer
        Observer storageObserver = new CargoObserver<>(warehouseFacade);
        warehouseFacade.registerObserver(storageObserver);

        //create serverport
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter tcp or udp to start server");
        String res = scanner.nextLine();
        server(eventHandler, 12345, res);
    }

    public static void server(EventHandler eventHandler, int port, String protocol) {
        switch (protocol) {
            case "tcp" -> {
                TCPServer tcpServer = new TCPServer(eventHandler, port);
                System.out.println("TCP Server is running on port " + port);
                tcpServer.start();
                new CLI(new TCPClient(port)).start();
            }
            case "udp" -> {
                UDPServer udpServer = new UDPServer(eventHandler, port);
                System.out.println("UDP Server is running on port " + port);
                udpServer.start();
                new CLI(new UDPClient(port)).start();
            }
            default -> System.out.println("invalid protocol");
        }
    }
}
