package administration;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public interface Storable extends Serializable {
    Customer getCustomer();

    /**
     * liefert die vergangene Zeit seit dem Einfügen
     *
     * @return vergangene Zeit oder null wenn kein Einfügedatum gesetzt
     */
    Duration getDurationOfStorage();

    Date getLastInspectionDate();

    int getStorageLocation();

    Date getInsertionDate();

    void setStorageLocation(int storageLocation);

    void setLastInspectionDate();
}
