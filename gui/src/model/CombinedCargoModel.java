package model;

import cargo.*;
import javafx.beans.property.*;

import java.time.Duration;
import java.util.Date;

public class CombinedCargoModel {
    private final Cargo cargo;
    private StringProperty cargoType;
    private BooleanProperty fragile;
    private IntegerProperty grainSize;
    private StringProperty hazards;
    private StringProperty name;
    private BooleanProperty pressurized;
    private IntegerProperty storageLocation;
    private DoubleProperty value;
    private ObjectProperty<Date> insertionDate;
    private ObjectProperty<Date> lastInspectionDate;
    private ObjectProperty<Duration> durationOfStorage;

    public CombinedCargoModel(Cargo cargo) {
        this.cargo = cargo;
        this.setCargoData();
    }

    private void setCargoData() {
        this.cargoType = new SimpleStringProperty(this.cargo.getCargoType());
        this.hazards = new SimpleStringProperty(this.cargo.getHazards().toString());
        this.name = new SimpleStringProperty(this.cargo.getCustomer().name());
        this.storageLocation = new SimpleIntegerProperty(this.cargo.getStorageLocation());
        this.value = new SimpleDoubleProperty(this.cargo.getValue().doubleValue());
        this.durationOfStorage = new SimpleObjectProperty<>(this.cargo.getDurationOfStorage());
        this.insertionDate = new SimpleObjectProperty<>(this.cargo.getInsertionDate());
        this.lastInspectionDate = new SimpleObjectProperty<>(this.cargo.getLastInspectionDate());
        switch (this.cargo) {
            case LiquidBulkAndUnitisedCargo liquidBulkAndUnitisedCargo -> {
                this.pressurized = new SimpleBooleanProperty(liquidBulkAndUnitisedCargo.isPressurized());
                this.fragile = new SimpleBooleanProperty(liquidBulkAndUnitisedCargo.isFragile());
            }
            case DryBulkAndUnitisedCargo dryBulkAndUnitisedCargo -> {
                this.grainSize = new SimpleIntegerProperty(dryBulkAndUnitisedCargo.getGrainSize());
                this.fragile = new SimpleBooleanProperty(dryBulkAndUnitisedCargo.isFragile());
            }
            case UnitisedCargo unitisedCargo -> {
                this.fragile = new SimpleBooleanProperty(unitisedCargo.isFragile());
                // Clear other properties to avoid confusion
                clearNonUnitisedCargoProperties();
            }
            case LiquidAndDryBulkCargo liquidAndDryBulkCargo -> {
                this.grainSize = new SimpleIntegerProperty(liquidAndDryBulkCargo.getGrainSize());
                this.pressurized = new SimpleBooleanProperty(liquidAndDryBulkCargo.isPressurized());
            }
            case LiquidBulkCargo liquidBulkCargo -> {
                this.pressurized = new SimpleBooleanProperty(liquidBulkCargo.isPressurized());
                // Clear other properties to avoid confusion
                clearNonLiquidBulkCargoProperties();
            }
            case DryBulkCargo dryBulkCargo -> {
                this.grainSize = new SimpleIntegerProperty(dryBulkCargo.getGrainSize());
                // Clear other properties to avoid confusion
                clearNonDryBulkCargoProperties();
            }
            default -> throw new IllegalStateException("Unexpected value: " + this.cargo);
        }
    }


    // Clear methods to set type-specific properties to null or default values

    private void clearNonUnitisedCargoProperties() {
        this.pressurized = null;
        this.grainSize = null;
    }

    private void clearNonLiquidBulkCargoProperties() {
        this.fragile = null;
        this.grainSize = null;
    }

    private void clearNonDryBulkCargoProperties() {
        this.pressurized = null;
        this.fragile = null;
    }

    // Getter-Methoden für Observable Properties

    public StringProperty cargoTypeProperty() {
        return cargoType;
    }

    public BooleanProperty fragileProperty() {
        return fragile;
    }

    public IntegerProperty grainSizeProperty() {
        return grainSize;
    }

    public StringProperty hazardsProperty() {
        return hazards;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public BooleanProperty pressurizedProperty() {
        return pressurized;
    }

    public IntegerProperty storageLocationProperty() {
        return storageLocation;
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public Date getInsertionDate() {
        return insertionDate.get();
    }

    public Date getLastInspectionDate() {
        return lastInspectionDate.get();
    }

    public Duration getDurationOfStorage() {
        return durationOfStorage.get();
    }

    public String getCargoType() {
        return cargoType.get();
    }

    public boolean isFragile() {
        return fragile.get();
    }

    public int getGrainSize() {
        return grainSize.get();
    }

    public StringProperty getHazards() {
        return hazards;
    }

    public String getName() {
        return name.get();
    }

    public boolean isPressurized() {
        return pressurized.get();
    }

    public int getStorageLocation() {
        return storageLocation.get();
    }

    public double getValue() {
        return value.get();
    }

    public ObjectProperty<Date> insertionDateProperty() {
        return insertionDate;
    }

    public ObjectProperty<Date> lastInspectionDateProperty() {
        return lastInspectionDate;
    }

    public ObjectProperty<Duration> durationOfStorageProperty() {
        return durationOfStorage;
    }
}

