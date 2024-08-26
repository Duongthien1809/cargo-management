package cargoImpl;

import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CargoImpl implements Cargo, Serializable {
    private int storageLocation;
    private final Date insertionDate;
    private Date lastInspectionDate;
    private final Customer customer;
    private final Collection<Hazard> hazards;
    private final BigDecimal value;
    private final String cargoType;

    public CargoImpl(String cargoType, Customer customer, BigDecimal value, Collection<Hazard> hazards) {
        this.customer = customer;
        this.cargoType = cargoType;
        this.insertionDate = new Date();
        this.lastInspectionDate = new Date();
        this.value = value;
        this.hazards = hazards;
    }


    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public Duration getDurationOfStorage() {
        Date currentDate = new Date();
        Date insertionDate = getInsertionDate();
        long durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTime() - insertionDate.getTime());
        return Duration.ofSeconds(durationInSeconds);
    }

    @Override
    public Date getLastInspectionDate() {
        return lastInspectionDate;
    }

    @Override
    public int getStorageLocation() {
        return storageLocation;
    }

    @Override
    public Date getInsertionDate() {
        return insertionDate;
    }

    @Override
    public void setStorageLocation(int storageLocation) {
        this.storageLocation = storageLocation;
    }

    @Override
    public void setLastInspectionDate() {
        this.lastInspectionDate = new Date();
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public Collection<Hazard> getHazards() {
        return hazards;
    }

    public String getCargoType() {
        return cargoType;
    }

//    /**
//     * todo: maybe not here, can be move to cli
//     *
//     * @return
//     */
//    @Override
//    public String toString() {
//        return getCargoType() + ": owner: " + getCustomer().getName() + ", storageLocation:  " + getStorageLocation() + ", InsertionDate: " + getInsertionDate() + ", InspektionDatum: " + getLastInspectionDate() + ", value: " + getValue() + ", Hazard: " + getHazards().toString() + ", Storage duration:  " + getDurationOfStorage();
//    }
}
