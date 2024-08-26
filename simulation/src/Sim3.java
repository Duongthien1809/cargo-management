import administration.Storable;
import cargo.Cargo;
import eventSystem.EventHandler;
import manager.Warehouse.WarehouseFacade;
import utils.EventGenerator;
import utils.SimCargoRandomGenerator;

import java.util.Date;
import java.util.EventObject;
import java.util.Random;

public class Sim3<T extends Storable & Cargo> extends Thread {
    private final WarehouseFacade<T> warehouseFacade;
    private final EventHandler eventHandler;

    public Sim3(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler, String name, int numThreads, int intervalMillis) {
        this.warehouseFacade = warehouseFacade;
        this.eventHandler = eventHandler;

        // Start threads
        startThreads(numThreads, name);

        // Start periodic state reporting thread
        startStateReportingThread(intervalMillis);
    }

    private void startThreads(int numThreads, String name) {
        for (int i = 0; i < numThreads; i++) {
            InsertThread<T> insertThread = new InsertThread<>(warehouseFacade, eventHandler, name);
            DeleteThread<T> deleteThread = new DeleteThread<>(warehouseFacade, eventHandler);
            InspectThread<T> inspectThread = new InspectThread<>(warehouseFacade, eventHandler);

            insertThread.start();
            inspectThread.start();
            deleteThread.start();
        }
    }

    private void startStateReportingThread(int intervalMillis) {
        StateReportingThread stateReportingThread = new StateReportingThread(warehouseFacade, intervalMillis);
        stateReportingThread.start();
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
                    try {
                        while (warehouseFacade.isEmpty()) {
                            warehouseFacade.wait(); // Wait if the warehouse is empty
                        }

                        // Find the cargo with the oldest inspection date
                        T oldestCargo = findOldestCargo();

                        // Remove the cargo
                        EventObject eventObject = EventGenerator.createEvent("d", String.valueOf(oldestCargo.getStorageLocation()).split(" "));
                        String msg = this.eventHandler.handleWithMsg(eventObject);
                        System.out.println(msg);

                        // Notify the insert threads that space is available
                        warehouseFacade.notifyAll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        private T findOldestCargo() {
            T oldestCargo = null;
            long oldestDuration = Long.MIN_VALUE;

            for (T cargo : warehouseFacade.listCargos()) {
                long duration = cargo.getDurationOfStorage().toMillis();

                if (duration > oldestDuration) {
                    oldestDuration = duration;
                    oldestCargo = cargo;
                }
            }

            return oldestCargo;
        }

    }

    private static class InspectThread<T extends Storable & Cargo> extends Thread {
        private final WarehouseFacade<T> warehouseFacade;
        private final EventHandler eventHandler;

        public InspectThread(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler) {
            this.warehouseFacade = warehouseFacade;
            this.eventHandler = eventHandler;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouseFacade) {
                    try {
                        while (warehouseFacade.isEmpty()) {
                            warehouseFacade.wait(); // Wait if the warehouse is empty
                        }
                        int index = new Random().nextInt(warehouseFacade.getCargoWarehouse().listAllCargos().size());
                        // update cargo
                        EventObject eventObject = EventGenerator.createEvent("u", String.valueOf(index).split(" "));
                        String msg = this.eventHandler.handleWithMsg(eventObject);
                        System.out.println(msg);

                        // Notify the insert threads that space is available
                        warehouseFacade.notifyAll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    private static class InsertThread<T extends Storable & Cargo> extends Thread {
        private final SimCargoRandomGenerator simCargoRandomGenerator;
        private final WarehouseFacade<T> warehouseFacade;
        private final EventHandler eventHandler;

        public InsertThread(WarehouseFacade<T> warehouseFacade, EventHandler eventHandler, String name) {
            this.warehouseFacade = warehouseFacade;
            this.simCargoRandomGenerator = new SimCargoRandomGenerator(name);
            this.eventHandler = eventHandler;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (warehouseFacade) {
                    try {
                        while (warehouseFacade.isFull()) {
                            warehouseFacade.wait(); // Wait if the warehouse is full
                        }

                        EventObject eventObject = EventGenerator.createEvent("c", simCargoRandomGenerator.generateRandomCargo().split(" "));
                        String msg = this.eventHandler.handleWithMsg(eventObject);
                        System.out.println(msg);

                        // Notify the delete thread that a cargo is added
                        warehouseFacade.notifyAll();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }

    private static class StateReportingThread extends Thread {
        private final WarehouseFacade<?> warehouseFacade;
        private final int intervalMillis;

        public StateReportingThread(WarehouseFacade<?> warehouseFacade, int intervalMillis) {
            this.warehouseFacade = warehouseFacade;
            this.intervalMillis = intervalMillis;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(intervalMillis);
                    reportState();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        private void reportState() {
            // Implement the logic to report the state of the warehouse
            // You can use warehouseFacade methods to get information about the state
            System.out.println("Warehouse state report: " + new Date());
        }
    }
}
