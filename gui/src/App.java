import cargo.Cargo;
import eventSystem.EventHandler;
import eventSystem.ObserverPattern.CargoObserver;
import eventSystem.eventListeners.GLEventListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import manager.Warehouse.CargoWarehouse;
import manager.Warehouse.CustomerWarehouse;
import manager.Warehouse.WarehouseFacade;

import java.io.IOException;

public class App extends Application {

    public void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Setup storage, cargo capacity 100
        WarehouseFacade<Cargo> warehouseFacade = new WarehouseFacade<>(new CustomerWarehouse(), new CargoWarehouse<>(100));
        warehouseFacade.registerObserver(new CargoObserver<>(warehouseFacade));
        EventHandler eventHandler = new EventHandler();
        eventHandler.add(new GLEventListener<>(warehouseFacade));

        // Setup GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("app.fxml"));
        Parent root = loader.load();

        // Set the controller
        Controller controller = loader.getController();
        controller.setEventHandler(eventHandler);

        //add observer to update list
        warehouseFacade.registerObserver(new WarehouseGUIObserver<>(warehouseFacade, controller));

        primaryStage.setTitle("Prog3 Beleg");
        primaryStage.setScene(new Scene(root, 1100, 500));
        primaryStage.show();
    }

}
