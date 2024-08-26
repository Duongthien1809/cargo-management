import administration.Storable;
import cargo.Cargo;
import eventSystem.EventHandler;
import manager.Warehouse.WarehouseFacade;
import utils.EventGenerator;
import utils.SimCargoRandomGenerator;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Random;

public class Sim2<T extends Storable & Cargo> {
    private final WarehouseFacade<T> warehouseFacade;
    private final EventHandler eventHandler;

    public Sim2(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler, String name, int numThreads) {
        this.warehouseFacade = warehouseFacade;
        this.eventHandler = eventHandler;
        startThreads(name, numThreads);
    }

    private void startThreads(String customerName, int numThreads) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            InsertThread<T> insertThread = new InsertThread<>(warehouseFacade, customerName, eventHandler);
            DeleteThread<T> deleteThread = new DeleteThread<>(warehouseFacade, eventHandler);

            threads.add(insertThread);
            threads.add(deleteThread);

            insertThread.start();
            deleteThread.start();
        }

        // shutdown hook to gracefully stop the threads on program exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }));
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
                        int rand = new Random().nextInt(this.warehouseFacade.getCargoWarehouse().getStorageCapacity());
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
