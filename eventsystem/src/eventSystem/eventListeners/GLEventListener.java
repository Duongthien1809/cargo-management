package eventSystem.eventListeners;

import administration.Customer;
import administration.Storable;
import cargo.Cargo;
import cargo.Hazard;
import eventSystem.EventTypes;
import eventSystem.Listener;
import eventSystem.events.*;
import filesystem.FileSystem;
import manager.Warehouse.WarehouseFacade;
import utils.ParseString;

import java.io.Serializable;
import java.util.EventObject;
import java.util.List;


public class GLEventListener<T extends Storable & Cargo> implements Listener, Serializable {
    private final WarehouseFacade<T> warehouseFacade;
    private final FileSystem<T> fs;
    private ParseString<T> parseString;

    public GLEventListener(WarehouseFacade<T> warehouseFacade) {
        this.warehouseFacade = warehouseFacade;
        this.fs = new FileSystem<>(warehouseFacade);
        parseString = new ParseString<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(EventObject eventObject) {
        if (eventObject == null) {
            return;
        }

        switch (eventObject.getClass().getSimpleName()) {
            case EventTypes.CUSTOMER_CREATE -> handleCustomerCreateEvent((CustomerCreateEvent) eventObject);
            case EventTypes.CUSTOMER_REMOVE -> handleCustomerRemoveEvent((CustomerRemoveEvent) eventObject);
            case EventTypes.CARGO_CREATE -> handleCargoCreateEvent((CargoCreateEvent<T>) eventObject);
            case EventTypes.CARGO_REMOVE -> handleCargoRemoveEvent((CargoRemoveEvent<T>) eventObject);
            case EventTypes.CARGO_INSPECTION -> handleInspectionEvent((CargoInspectionEvent<T>) eventObject);
            case EventTypes.SHOW_CARGOS -> handleShowCargosEvent((ShowCargosEvent) eventObject);
            case EventTypes.SHOW_HAZARDS -> handleShowHazardsEvent((ShowHazardsEvent<T>) eventObject);
            case EventTypes.SHOW_CUSTOMERS -> handleShowCustomersEvent((ShowCustomersEvent) eventObject);
            case EventTypes.LOAD_EVENT -> handleLoadEvent((LoadEvent) eventObject);
            case EventTypes.SAVE_EVENT -> handleSaveEvent((SaveEvent) eventObject);
            default -> {
            }
            // Unbekanntes Event, hier kannst du entsprechend handeln.
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String onEventWithMsg(EventObject eventObject) {
        if (eventObject == null) return "";
        String msg = "";
        switch (eventObject.getClass().getSimpleName()) {
            case EventTypes.CUSTOMER_CREATE -> msg = handleCustomerCreateEvent((CustomerCreateEvent) eventObject);
            case EventTypes.CUSTOMER_REMOVE -> msg = handleCustomerRemoveEvent((CustomerRemoveEvent) eventObject);
            case EventTypes.CARGO_CREATE -> msg = handleCargoCreateEvent((CargoCreateEvent<T>) eventObject);
            case EventTypes.CARGO_REMOVE -> msg = handleCargoRemoveEvent((CargoRemoveEvent<T>) eventObject);
            case EventTypes.CARGO_INSPECTION -> msg = handleInspectionEvent((CargoInspectionEvent<T>) eventObject);
            case EventTypes.SHOW_CARGOS -> msg = handleShowCargosEvent((ShowCargosEvent) eventObject);
            case EventTypes.SHOW_HAZARDS -> msg = handleShowHazardsEvent((ShowHazardsEvent<T>) eventObject);
            case EventTypes.SHOW_CUSTOMERS -> msg = handleShowCustomersEvent((ShowCustomersEvent) eventObject);
            case EventTypes.LOAD_EVENT -> msg = handleLoadEvent((LoadEvent) eventObject);
            case EventTypes.SAVE_EVENT -> msg = handleSaveEvent((SaveEvent) eventObject);
            default -> {
            }
            // Unbekanntes Event, hier kannst du entsprechend handeln.
        }

        return msg;
    }

    private String handleSaveEvent(SaveEvent eventObject) {
        String msg = fs.saveToFile(eventObject.getFileType());
        warehouseFacade.observerNotify("save to File");
        return msg;
    }

    private String handleLoadEvent(LoadEvent eventObject) {
        String msg = fs.loadWarehouse(eventObject.getFileType());
        warehouseFacade.observerNotify("load Data from File");
        return msg;
    }

    private String handleShowCustomersEvent(ShowCustomersEvent eventObject) {
        StringBuilder msg = new StringBuilder();
        for (Customer customer : warehouseFacade.listCustomers()) {
            msg.append(parseString.getCustomerInfo(customer, warehouseFacade));
        }
        if (msg.isEmpty()) {
            return "no customers in storage";
        }
        return msg.toString();
    }

    private String handleCustomerCreateEvent(CustomerCreateEvent event) {
        if (warehouseFacade.insertCustomer(event.getCustomerName())) {
            return "Customer " + event.getCustomerName() + " inserted!";
        } else {
            return "customer insert failed!";
        }
    }

    private String handleCustomerRemoveEvent(CustomerRemoveEvent event) {
        if (warehouseFacade.removeCustomer(event.getCustomerName())) {
            return "Customer " + event.getCustomerName() + " removed!";
        } else {
            return "customer remove failed!";
        }
    }

    private String handleCargoCreateEvent(CargoCreateEvent<T> event) {
        T cargo = event.getCargo();
        if (cargo != null) {
            List<Hazard> previousHazards = warehouseFacade.includeHazard();
            String notification = this.notifyCargoStorageIsFastFull();
            if (null != notification) warehouseFacade.observerNotify(notification + " of storage reached!");
            if (warehouseFacade.insertCargo(cargo)) {
                this.notifyHazardsUpdate(previousHazards);
                return "cargo is inserted!";
            }
        }
        return "cargo insert failed!";
    }

    private String handleCargoRemoveEvent(CargoRemoveEvent<T> event) {
        int storageLocation = event.getStorageLocation();
        List<Hazard> previousHazards = warehouseFacade.includeHazard();
        String notification = this.notifyCargoStorageIsFastFull();
        if (null != notification) warehouseFacade.observerNotify(notification + " of storage reached!");
        if (warehouseFacade.removeCargoByLocation(storageLocation)) {
            this.notifyHazardsUpdate(previousHazards);
            return "cargo at location: " + storageLocation + " removed!";
        }
        return "cargo remove failed!";
    }

    String notifyCargoStorageIsFastFull() {
        double currentCapacityPercentage = warehouseFacade.calculateCurrentCapacityPercentage();
        if (currentCapacityPercentage >= 90.0) { //notify when storage more than 90 percent of capacity
            return String.valueOf(currentCapacityPercentage);
        }
        return null;
    }

    void notifyHazardsUpdate(List<Hazard> previousHazards) {
        List<Hazard> currentHazards = warehouseFacade.includeHazard();
        if (!currentHazards.equals(previousHazards)) {
            warehouseFacade.observerNotify("Hazard update: " + currentHazards);
        }
    }

    private String handleInspectionEvent(CargoInspectionEvent<T> event) {
        int storageLocation = event.getStorageLocation();
        if (warehouseFacade.updateCargoByStorageLocation(storageLocation)) {
            return "cargo at location: " + storageLocation + " updated!";
        }
        return "Inspection failed!";
    }

    private String handleShowCargosEvent(ShowCargosEvent event) {
        StringBuilder msg = new StringBuilder();
        if (event.getCargoType().isEmpty()) {
            for (T cargo : warehouseFacade.listCargos()) {
                msg.append(parseString.printCargo(cargo)).append("\n");
            }
        } else {
            for (T cargo : warehouseFacade.listCargosByType(event.getCargoType())) {
                msg.append(parseString.printCargo(cargo)).append("\n");
            }
        }
        if (msg.isEmpty()) {
            return "no cargos in storage";
        }
        return msg.toString();
    }


    private String handleShowHazardsEvent(ShowHazardsEvent<T> event) {
        List<Hazard> hazards;
        StringBuilder msg = new StringBuilder();
        if (event.containsHazards()) {
            hazards = warehouseFacade.includeHazard();
            msg.append("include hazards: ");
        } else {
            hazards = warehouseFacade.excludeHazard();
            msg.append("exclude hazards: ");
        }
        for (Hazard hazard : hazards) {
            msg.append(hazard.toString()).append(", ");
        }
        return msg.toString();
    }

}

