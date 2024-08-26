package utils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimCargoRandomGenerator {
    private final String customerName;

    public SimCargoRandomGenerator(String customerName) {
        this.customerName = customerName;
    }

    public String generateRandomCargo() {
        // Initialize random values
        BigDecimal value = new BigDecimal(ThreadLocalRandom.current().nextInt(200));
        int randInt = ThreadLocalRandom.current().nextInt(6); // Assuming 3 types of cargo
        String cargoType;
        String hazards = generateRandomHazards();
        switch (randInt) {
            case 0:
                cargoType = "DryBulkCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextInt(1, 100);
            case 1:
                cargoType = "UnitisedCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextBoolean();
            case 2:
                cargoType = "LiquidBulkCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextBoolean();
            case 3:
                cargoType = "DryBulkAndUnitisedCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextBoolean() + " " + ThreadLocalRandom.current().nextInt(1, 100);
            case 4:
                cargoType = "LiquidAndDryBulkCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextBoolean() + " " + ThreadLocalRandom.current().nextInt(1, 100);
            case 5:
                cargoType = "LiquidBulkAndUnitisedCargo";
                return cargoType + " " + this.customerName + " " + value + " " + hazards + " " + ThreadLocalRandom.current().nextBoolean() + " " + ThreadLocalRandom.current().nextBoolean();
        }
        return "";
    }

    private String generateRandomHazards() {
        switch (new Random().nextInt(1, 6)) {
            case 1 -> {
                return "explosive";
            }
            case 2 -> {
                return "flammable";
            }
            case 3 -> {
                return "toxic";
            }
            case 4 -> {
                return "radioactive";
            }
            case 5 -> {
                return "explosive,toxic";
            }
        }
        return "";
    }


}
