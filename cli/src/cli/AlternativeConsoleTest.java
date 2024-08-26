package cli;

import eventSystem.EventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlternativeConsoleTest {

    private AlternativeConsole alternativeConsole;

    @BeforeEach
    void setUp() {
        EventHandler eventHandler = new EventHandler(); // You may need to mock this
        alternativeConsole = new AlternativeConsole(eventHandler);
    }

    @Test
    void changeModeTest() {
        String result = alternativeConsole.changeMode(":c");
        Assertions.assertEquals("Switched to insert mode", result);

        result = alternativeConsole.changeMode(":d");
        Assertions.assertEquals("Switched to deletion mode", result);

        result = alternativeConsole.changeMode(":r");
        Assertions.assertEquals("Switched to show mode", result);

        result = alternativeConsole.changeMode(":u");
        Assertions.assertEquals("Switched to update mode", result);

        result = alternativeConsole.changeMode(":p");
        Assertions.assertEquals("Switched to persistent mode", result);

        result = alternativeConsole.changeMode(":invalid");
        Assertions.assertEquals("Error: invalid mode specified", result);
    }
}
