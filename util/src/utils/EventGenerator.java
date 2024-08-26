package utils;

import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;
import cargoImpl.*;
import eventSystem.events.*;
import manager.CustomerImpl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;

public class EventGenerator implements Serializable {
    public static EventObject createEvent(String command, String[] inputArr) {
        EventObject event = null;

        switch (command) {
            case "c" -> event = createInsertEvent(inputArr);
            case "d" -> event = createRemoveEvent(inputArr);
            case "r" -> event = createShowEvent(inputArr);
            case "u" -> event = createInspectionEvent(inputArr);
            case "p" -> event = createPersistenceEvent(inputArr);
        }

        return event;
    }

    private static EventObject createInsertEvent(String[] inputArr) {
        if (inputArr.length == 1) {
            return new CustomerCreateEvent(inputArr[0]);
        } else if (inputArr.length >= 5) {
            Cargo cargo = createCargo(inputArr);
            if (cargo != null) {
                return new CargoCreateEvent<>(cargo);
            }
        }
        return null;
    }

    private static EventObject createRemoveEvent(String[] inputArr) {
        if (inputArr.length == 1 && inputArr[0].matches("\\d+")) {
            return new CargoRemoveEvent<>(Integer.parseInt(inputArr[0]));
        } else if (!inputArr[0].isEmpty() && !Character.isDigit(inputArr[0].charAt(0))) {
            return new CustomerRemoveEvent(inputArr[0]);
        }
        return null;
    }

    private static EventObject createShowEvent(String[] inputArr) {
        if (inputArr.length == 1) {
            if (inputArr[0].equalsIgnoreCase("customers")) {
                return new ShowCustomersEvent("");
            }
            if (inputArr[0].equalsIgnoreCase("cargos")) {
                return new ShowCargosEvent("");
            }
        } else if (inputArr.length == 2 && inputArr[0].equalsIgnoreCase("cargos")) {
            return new ShowCargosEvent(inputArr[1]);
        } else if (inputArr.length == 2 && inputArr[0].equalsIgnoreCase("hazards")) {
            boolean containHazard;
            switch (inputArr[1]) {
                case "i" -> containHazard = true;
                case "e" -> containHazard = false;
                default -> throw new IllegalStateException("Unexpected value: " + inputArr[1]);
            }
            return new ShowHazardsEvent<>(containHazard);
        }
        return null;
    }

    private static EventObject createInspectionEvent(String[] inputArr) {
        if (inputArr.length == 1 && inputArr[0].matches("\\d+")) {
            return new CargoInspectionEvent<>(Integer.parseInt(inputArr[0]));
        }
        return null;
    }

    private static EventObject createPersistenceEvent(String[] inputArr) {
        if (inputArr.length == 1) {
            String fileType = inputArr[0];
            switch (fileType) {
                case "saveJOS" -> {
                    return new SaveEvent("jos");
                }
                case "saveJBP" -> {
                    return new SaveEvent("jbp");
                }
                case "loadJOS" -> {
                    return new LoadEvent("jos");
                }
                case "loadJBP" -> {
                    return new LoadEvent("jbp");
                }
            }
        }
        return null;
    }

    private static Cargo createCargo(String[] inputArr) {
        String cargoType = inputArr[0];
        Customer customer = new CustomerImpl(inputArr[1]);
        BigDecimal value = new BigDecimal(inputArr[2].replace(",", "."));
        String[] hazardArr = inputArr[3].split(",");
        Collection<Hazard> hazards = convertToCollection(hazardArr);
        if (hazards == null) {//check when wrong enum is given
            return null;
        }
        switch (cargoType.toLowerCase()) {
            case "drybulkcargo" -> {
                if (inputArr.length == 5) {
                    int grainSize = Integer.parseInt(inputArr[4]);
                    return new DryBulkCargoImpl(cargoType, customer, value, hazards, grainSize);
                }
            }
            case "liquidbulkcargo" -> {
                if (inputArr.length == 5) {
                    boolean isPressurized = convertStringToBoolean(inputArr[4]);
                    return new LiquidBulkCargoImpl(cargoType, customer, value, hazards, isPressurized);
                }
            }
            case "unitisedcargo" -> {
                if (inputArr.length == 5) {
                    boolean isFragile = convertStringToBoolean(inputArr[4]);
                    return new UnitisedCargoImpl(cargoType, customer, value, hazards, isFragile);
                }
            }
            case "drybulkandunitisedcargo" -> {
                if (inputArr.length == 6) {
                    boolean isFragile = convertStringToBoolean(inputArr[4]);
                    int grainSize = Integer.parseInt(inputArr[5]);
                    return new DryBulkAndUnitisedCargoImpl(cargoType, customer, value, hazards, isFragile, grainSize);
                }
            }
            case "liquidanddrybulkcargo" -> {
                if (inputArr.length == 6) {
                    boolean pressurize = convertStringToBoolean(inputArr[4]);
                    int grainSize = Integer.parseInt(inputArr[5]);
                    return new LiquidAndDryBulkCargoImpl(cargoType, customer, value, hazards, pressurize, grainSize);
                }
            }
            case "liquidbulkandunitisedcargo" -> {
                if (inputArr.length == 6) {
                    boolean isFragile = convertStringToBoolean(inputArr[4]);
                    boolean pressurize = convertStringToBoolean(inputArr[5]);
                    return new LiquidBulkAndUnitisedCargoImpl(cargoType, customer, value, hazards, isFragile, pressurize);
                }
            }

            default -> System.err.println("there is no cargo with given cargo type");
        }
        return null;
    }

    private static Collection<Hazard> convertToCollection(String[] hazardArr) {
        Collection<Hazard> hazards = new ArrayList<>();
        for (String value : hazardArr) {
            try {
                Hazard enumValue = Hazard.valueOf(value);
                hazards.add(enumValue);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return hazards;
    }

    private static boolean convertStringToBoolean(String name) {
        if (name.equalsIgnoreCase("true")) {
            return true;
        } else if (name.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new IllegalArgumentException("Cannot convert this string to boolean.");
        }
    }
}
