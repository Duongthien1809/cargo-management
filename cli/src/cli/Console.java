package cli;

import client.Client;
import eventSystem.EventHandler;
import utils.EventGenerator;

import java.util.EventObject;
import java.util.Scanner;

public class Console {
    private EventHandler eventHandler;
    private Client client;
    private String status = "c";

    public Console(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public Console(client.Client client) {
        this.client = client;
    }

    public void execute() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter command:");
            do {
                String input = scanner.nextLine();
                if (input.charAt(0) == ':') {
                    this.printMsg(changeMode(input));
                } else {
                    handleInput(status, input);
                }
            } while (true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String changeMode(String text) {
        switch (text) {
            case ":c" -> {
                this.status = "c";
                return "Switched to insert mode";
            }
            case ":d" -> {
                this.status = "d";
                return "Switched to deletion mode";
            }
            case ":r" -> {
                this.status = "r";
                return "Switched to show mode";
            }
            case ":u" -> {
                this.status = "u";
                return "Switched to update mode";
            }
            case ":p" -> {
                this.status = "p";
                return "Switched to persistent mode";
            }
            default -> {
                return "Error: invalid mode specified";
            }
        }
    }

    void handleInput(String status, String input) {
        String[] inputArr = input.split(" ");
        EventObject event = EventGenerator.createEvent(status, inputArr);
        if (this.client == null) { //without client
            if (event != null) {
                String msg = eventHandler.handleWithMsg(event);
                printMsg(msg);
            } else {
                System.out.println("Invalid command.");
            }
        } else {//with client
            String msg = this.client.sendEvent(event);
            printMsg(msg);
        }
    }

    private void printMsg(String msg) {
        System.out.println(msg);
    }

    public Client getClient() {
        return client;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public String getStatus() {
        return status;
    }
}
