import administration.Storable;
import cargo.Cargo;
import eventSystem.EventHandler;
import manager.Warehouse.WarehouseFacade;
import utils.EventGenerator;
import utils.SimCargoRandomGenerator;

import java.util.EventObject;
import java.util.Random;

public class Sim1<T extends Storable & Cargo> {
    private final WarehouseFacade<T> warehouseFacade;
    private final EventHandler eventHandler;

    public Sim1(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler, String name) {
        this.warehouseFacade = warehouseFacade;
        this.eventHandler = eventHandler;
        startInsertThread(name);
        startDeleteThread();
    }

    public void startInsertThread(String customerName) {
        InsertThread<T> insertThread = new InsertThread<>(warehouseFacade, customerName, eventHandler);
        insertThread.start();
    }

    public void startDeleteThread() {
        DeleteThread<T> deleteThread = new DeleteThread<>(warehouseFacade, eventHandler);
        deleteThread.start();
    }

    private static class DeleteThread<T extends Storable & Cargo> extends Thread {
        private final WarehouseFacade<T> warehouseFacade;
        private final EventHandler eventHandler;

        public DeleteThread(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler) {
            this.warehouseFacade = warehouseFacade;
            this.eventHandler = eventHandler;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouseFacade) {
                    if (warehouseFacade.isFull()) {
                        int rand = new Random().nextInt(this.warehouseFacade.getCargoWarehouse().listAllCargos().size());
                        EventObject eventObject = EventGenerator.createEvent("d", String.valueOf(rand).split(" "));
                        String msg = this.eventHandler.handleWithMsg(eventObject);
                        System.out.println(msg);
                    }
                }
            }
        }
    }

    private static class InsertThread<T extends Storable & Cargo> extends Thread {
        private final SimCargoRandomGenerator simCargoRandomGenerator;
        private final WarehouseFacade<T> warehouseFacade;
        private final EventHandler eventHandler;

        public InsertThread(WarehouseFacade<T> warehouseFacade, String customerName, EventHandler eventHandler) {
            this.warehouseFacade = warehouseFacade;
            this.simCargoRandomGenerator = new SimCargoRandomGenerator(customerName);
            this.eventHandler = eventHandler;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouseFacade) {
                    if (!warehouseFacade.isFull()) {
                        EventObject eventObject = EventGenerator.createEvent("c", simCargoRandomGenerator.generateRandomCargo().split(" "));
                        String msg = this.eventHandler.handleWithMsg(eventObject);
                        System.out.println(msg);
                    }
                }
            }
        }
    }

}
