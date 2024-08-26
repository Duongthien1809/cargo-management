package cli;

import client.Client;
import eventSystem.EventHandler;

public class CLI extends Thread {
    private EventHandler eventHandler;
    private Client client;

    public CLI(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public CLI(Client client) {
        this.client = client;
    }

    public void run() {
        if (client == null) {
            new Console(eventHandler).execute();
        } else {
            new Console(client).execute();
        }

    }
}
