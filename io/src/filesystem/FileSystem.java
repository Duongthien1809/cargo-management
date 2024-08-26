package filesystem;

import administration.Storable;
import cargo.Cargo;
import manager.Warehouse.WarehouseFacade;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileSystem<T extends Storable & Cargo> implements Serializable {
    private final WarehouseFacade<T> warehouseFacade;
    private final String fileJBP = "warehouse_jbp.txt";
    private final String fileJOS = "warehouse_jos.txt";
    private final Lock lock = new ReentrantLock();

    public FileSystem(WarehouseFacade<T> warehouseFacade) {
        this.warehouseFacade = warehouseFacade;
    }

    public FileOutputStream getWriteStream(String input) throws IOException {
        File file = new File(input);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException("Unable to create the file: " + input);
        }
        return new FileOutputStream(input);
    }

    public FileInputStream getReadStream(String address) throws IOException {
        File file = new File(address);
        if (!file.exists()) {
            throw new FileNotFoundException("The file does not exist: " + address);
        }
        return new FileInputStream(address);
    }

    public String saveToFile(String command) {
        this.lock.lock();
        try {
            switch (command) {
                case "jos" -> saveJOS();
                case "jbp" -> saveJBP();
                default -> {
                    return "Invalid command for writing warehouse: " + command;
                }
            }
            return "Warehouse saved";
        } catch (IOException e) {
            System.err.println("IO error");
            e.printStackTrace();
            return "IO error: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Unexpected error");
            e.printStackTrace();
            return "Unexpected error: " + e.getMessage();
        } finally {
            this.lock.unlock();
        }
    }

    public String loadWarehouse(String command) {
        this.lock.lock();
        try {
            WarehouseFacade<T> localWarehouse;
            switch (command) {
                case "jos" -> localWarehouse = loadJOS();
                case "jbp" -> localWarehouse = loadJBP();
                default -> {
                    return "Invalid command for writing to warehouse: " + command;
                }
            }
            this.warehouseFacade.replaceContents(localWarehouse);
            return "Warehouse loaded";
        } catch (IOException e) {
            System.err.println("IO error");
            e.printStackTrace();
            return "IO error: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("Unexpected error");
            e.printStackTrace();
            return "Unexpected error: " + e.getMessage();
        } finally {
            this.lock.unlock();
        }
    }

    void saveJOS() throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(getWriteStream(this.fileJOS))) {
            stream.writeObject(warehouseFacade);
        }
    }

    @SuppressWarnings("unchecked")
    WarehouseFacade<T> loadJOS() throws IOException, ClassNotFoundException {
        try (ObjectInputStream stream = new ObjectInputStream(getReadStream(this.fileJOS))) {
            return (WarehouseFacade<T>) stream.readObject();
        }
    }

    void saveJBP() throws IOException {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(getWriteStream(this.fileJBP)))) {
            encoder.writeObject(warehouseFacade);
        }
    }

    @SuppressWarnings("unchecked")
    WarehouseFacade<T> loadJBP() throws IOException {
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(getReadStream(this.fileJBP)))) {
            return (WarehouseFacade<T>) decoder.readObject();
        }
    }
}
