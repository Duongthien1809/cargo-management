package cli;

import client.Client;
import eventSystem.EventHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsoleTest {

    @Test
    void changeModeTest() {
        //init
        EventHandler eventHandler = mock(EventHandler.class);// You may need to mock this
        Console console = new Console(eventHandler);

        String result = console.changeMode(":c");
        assertEquals("Switched to insert mode", result);

        result = console.changeMode(":d");
        assertEquals("Switched to deletion mode", result);

        result = console.changeMode(":r");
        assertEquals("Switched to show mode", result);

        result = console.changeMode(":u");
        assertEquals("Switched to update mode", result);

        result = console.changeMode(":p");
        assertEquals("Switched to persistent mode", result);

        result = console.changeMode(":invalid");
        assertEquals("Error: invalid mode specified", result);
    }

    @Test
    void executeCommandExceptionTest() {
        //init
        EventHandler mockEventHandler = mock(EventHandler.class);
        Client mockClient = mock(Client.class);
        Console console = mock(Console.class);
        when(console.getClient()).thenReturn(mockClient);
        when(console.getEventHandler()).thenReturn(mockEventHandler);
        // Mock the behavior of the Scanner
        Scanner mockScanner = mock(Scanner.class);
        Mockito.when(mockScanner.nextLine())
                .thenReturn(":r")  // Simulate a command
                .thenReturn("Switched to insert mode");  // Simulate the user exiting

        // Call the method to be tested
        console.execute();
        // Verify that the expected methods were called
        Mockito.verify(mockEventHandler, Mockito.never()).handleWithMsg(Mockito.any());
        Mockito.verify(mockClient, Mockito.never()).sendEvent(Mockito.any());
    }

}