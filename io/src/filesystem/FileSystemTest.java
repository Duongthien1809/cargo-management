package filesystem;

import cargo.Cargo;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FileSystemTest {

    WarehouseFacade<Cargo> warehouseFacade;
    FileSystem<Cargo> fs;

    @BeforeEach
    void setUp() {
        warehouseFacade = new WarehouseFacade<>(new CustomerWarehouse(), new CargoWarehouse<>(100));
        fs = spy(new FileSystem<>(warehouseFacade));
        warehouseFacade.insertCustomer("test");
    }

    @AfterAll
    static void teardown() {
        for (String protocol : new String[]{"warehouse_jos", "warehouse_jbp"}) {
            File file = new File(protocol);
            if (file.exists()) {
                assertTrue(file.delete());
            }
        }
    }

    private void clearFileBeforeSave(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            assertTrue(file.delete());
        }

    }

    @Test
    void saveJOSTest() throws IOException {
        clearFileBeforeSave("warehouse_jos");
        doReturn(new FileOutputStream("warehouse_jos.txt")).when(fs).getWriteStream("warehouse_jos.txt");

        String result = fs.saveToFile("jos");

        assertTrue(new File("warehouse_jos.txt").exists());
        assertEquals("Warehouse saved", result);
        // verify
        verify(fs, never()).saveJBP();
    }

    @Test
    void loadJOSTest() throws IOException {
        // Saving the warehouse to file before loading
        fs.saveToFile("jos");
        doReturn(new FileInputStream("warehouse_jos.txt")).when(fs).getReadStream("warehouse_jos.txt");

        String result = fs.loadWarehouse("jos");

        // Assuming you have a valid toString implementation in your WarehouseFacade
        assertTrue(result.contains("Warehouse loaded"));
        // verify
        verify(fs, never()).loadJBP();
    }

    @Test
    void saveJBPSpyTest() throws IOException {
        clearFileBeforeSave("warehouse_jbp");
        doReturn(new FileOutputStream("warehouse_jbp.txt")).when(fs).getWriteStream("warehouse_jbp.txt");

        String result = fs.saveToFile("jbp");

        assertTrue(new File("warehouse_jbp.txt").exists());
        assertEquals("Warehouse saved", result);
        //verify
        verify(fs, never()).saveJOS();
    }

    @Test
    void loadJBPSpyTest() throws IOException, ClassNotFoundException {
        // Saving the warehouse to file before loading
        fs.saveToFile("jbp");
        doReturn(new FileInputStream("warehouse_jbp.txt")).when(fs).getReadStream("warehouse_jbp.txt");

        String result = fs.loadWarehouse("jbp");

        // Assuming you have a valid toString implementation in your WarehouseFacade
        assertTrue(result.contains("Warehouse loaded"));
        // verify
        verify(fs, never()).loadJOS();
    }

    @Test
    void saveToFileInvalidCommandTest() {
        String result = fs.saveToFile("invalid");
        assertEquals("Invalid command for writing warehouse: invalid", result);
    }

    @Test
    void loadWarehouseInvalidCommandTest() {
        String result = fs.loadWarehouse("invalid");
        assertEquals("Invalid command for writing to warehouse: invalid", result);
    }

    @Test
    void saveToFileIOExceptionTest() throws Exception {
        Mockito.doThrow(new IOException("Simulated IO Exception")).when(fs).saveJOS();

        String result = fs.saveToFile("jos");

        assertEquals("IO error: Simulated IO Exception", result);
        // verify
        verify(fs, never()).saveJBP();
        verify(fs, times(1)).saveJOS();
    }

    @Test
    void saveToFileUnexpectedExceptionTest() throws Exception {
        Mockito.doThrow(new RuntimeException("Simulated Unexpected Exception")).when(fs).saveJOS();

        String result = fs.saveToFile("jos");

        assertEquals("Unexpected error: Simulated Unexpected Exception", result);
        //verify
        verify(fs, never()).saveJBP();
        verify(fs, times(1)).saveJOS();
    }

    @Test
    void loadToFileIOExceptionTest() throws Exception {
        Mockito.doThrow(new IOException("Simulated IO Exception")).when(fs).loadJBP();

        String result = fs.loadWarehouse("jbp");

        assertEquals("IO error: Simulated IO Exception", result);
        //verify
        verify(fs, never()).loadJOS();
        verify(fs, times(1)).loadJBP();
    }

    @Test
    void loadToFileUnexpectedExceptionTest() throws Exception {
        Mockito.doThrow(new RuntimeException("Simulated Unexpected Exception")).when(fs).loadJBP();

        String result = fs.loadWarehouse("jbp");

        assertEquals("Unexpected error: Simulated Unexpected Exception", result);
        // verify
        verify(fs, never()).loadJOS();
        verify(fs, times(1)).loadJBP();
    }
}
