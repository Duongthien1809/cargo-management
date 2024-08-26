package server;

import java.util.EventObject;

public interface Server {
    String handleEvent(EventObject event);
}
