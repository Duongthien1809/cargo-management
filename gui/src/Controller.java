import administration.Customer;
import cargo.Cargo;
import cargo.Hazard;
import eventSystem.EventHandler;
import eventSystem.events.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.CombinedCargoModel;
import model.CustomerModel;
import utils.EventGenerator;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Controller implements Serializable {
    private EventHandler eventHandler;

    @FXML
    private transient TextField addCustomerField;
    @FXML
    private transient TableView<CombinedCargoModel> cargoTableView;
    @FXML
    private transient TableView<CustomerModel> customerTableView;

    @FXML
    private transient TableColumn<CombinedCargoModel, String> cargoType;
    @FXML
    private transient TableColumn<CustomerModel, Integer> counts;
    @FXML
    private transient TableColumn<CombinedCargoModel, String> customerName;
    @FXML
    private transient TableColumn<CustomerModel, String> customerTName;
    @FXML
    private transient TableColumn<CombinedCargoModel, String> duration;
    @FXML
    private transient TableColumn<CombinedCargoModel, Boolean> fragile;
    @FXML
    private transient TableColumn<CombinedCargoModel, Boolean> grainsize;
    @FXML
    private transient TableColumn<CombinedCargoModel, Hazard> hazards;
    @FXML
    private transient TextField insertField;
    @FXML
    private transient TableColumn<CombinedCargoModel, String> insertionDate;
    @FXML
    private transient TableColumn<CombinedCargoModel, String> lastInspectionDate;
    @FXML
    private transient TableColumn<CombinedCargoModel, String> pressurized;
    @FXML
    private transient TableColumn<CombinedCargoModel, Integer> storageLocation;
    @FXML
    private transient TableColumn<CombinedCargoModel, Integer> value;

    @FXML
    private transient Label statusLabel;

    private transient final ObservableList<CombinedCargoModel> cargoData = FXCollections.observableArrayList();
    private transient final ObservableList<CustomerModel> customerData = FXCollections.observableArrayList();

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void initialize() {
        createCargoTableView(cargoData);
        createCustomerTableView(customerData);
    }

    @FXML
    void addCargo(ActionEvent event) {
        try {
            String[] inputArr = insertField.getText().split(" ");
            if (inputArr.length > 1) {
                eventHandler.handle(EventGenerator.createEvent("c", inputArr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addCustomer(ActionEvent event) {
        try {
            String[] inputArr = addCustomerField.getText().split(" ");
            if (inputArr.length == 1) {
                this.eventHandler.handle(new CustomerCreateEvent(inputArr[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadWarehouse(ActionEvent event) {
        try {
            this.eventHandler.handle(new LoadEvent("jos"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveWarehouse(ActionEvent event) {
        try {
            this.eventHandler.handle(new SaveEvent("jos"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createCargoTableView(List<CombinedCargoModel> cargos) {
        cargoTableView.getColumns().clear();
        setupCargoTableColumns();
        setupButtonColumnForCargoTable();
        cargoData.setAll(cargos);
    }

    private void createCustomerTableView(List<CustomerModel> customers) {
        customerTableView.getColumns().clear();
        ObservableList<CustomerModel> updatedCustomerData = FXCollections.observableArrayList(customers);
        customerTName.setCellValueFactory(new PropertyValueFactory<>("name"));
        counts.setCellValueFactory(new PropertyValueFactory<>("counts"));
        customerTableView.setItems(customerData);
        customerTableView.getColumns().addAll(customerTName, counts);
        setupButtonColumnForCustomerTable();
        customerData.setAll(updatedCustomerData);
    }

    private void setupCargoTableColumns() {
        storageLocation.setCellValueFactory(new PropertyValueFactory<>("storageLocation"));
        cargoType.setCellValueFactory(new PropertyValueFactory<>("cargoType"));
        hazards.setCellValueFactory(new PropertyValueFactory<>("hazards"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        pressurized.setCellValueFactory(new PropertyValueFactory<>("pressurized"));
        fragile.setCellValueFactory(new PropertyValueFactory<>("fragile"));
        grainsize.setCellValueFactory(new PropertyValueFactory<>("grainSize"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
        insertionDate.setCellValueFactory(new PropertyValueFactory<>("insertionDate"));
        lastInspectionDate.setCellValueFactory(new PropertyValueFactory<>("lastInspectionDate"));
        duration.setCellValueFactory(new PropertyValueFactory<>("durationOfStorage"));

        cargoTableView.setItems(cargoData);
        cargoTableView.getColumns().addAll(storageLocation, cargoType, customerName, value, insertionDate, lastInspectionDate, duration, hazards, fragile, grainsize, pressurized);
    }

    private void setupButtonColumnForCargoTable() {
        TableColumn<CombinedCargoModel, Void> buttonColumn = new TableColumn<>("Action");
        buttonColumn.setCellFactory(generateButtonForCargoTable());
        cargoTableView.getColumns().add(buttonColumn);
    }

    private void setupButtonColumnForCustomerTable() {
        TableColumn<CustomerModel, Void> buttonColumn = new TableColumn<>("Action");
        buttonColumn.setCellFactory(generateBtnForCustomerTable());
        customerTableView.getColumns().add(buttonColumn);
    }


    private Callback<TableColumn<CombinedCargoModel, Void>, TableCell<CombinedCargoModel, Void>> generateButtonForCargoTable() {
        return new Callback<>() {
            @Override
            public TableCell<CombinedCargoModel, Void> call(final TableColumn<CombinedCargoModel, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button updateButton = new Button("Update");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            CombinedCargoModel combinedCargoModel = getTableView().getItems().get(getIndex());
                            int location = combinedCargoModel.storageLocationProperty().get();
                            eventHandler.handle(new CargoRemoveEvent<>(location));
                            cargoTableView.refresh();
                            createCustomerTableView(customerData);
                            createCargoTableView(cargoData);
                        });


                        updateButton.setOnAction((ActionEvent event) -> {
                            CombinedCargoModel combinedCargoModel = getTableView().getItems().get(getIndex());
                            int location = combinedCargoModel.storageLocationProperty().get();
                            eventHandler.handle(new CargoInspectionEvent<>(location));
                            cargoTableView.refresh();
                            createCargoTableView(cargoData);
                            createCustomerTableView(customerData);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsContainer = new HBox(deleteButton, updateButton);
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        };
    }

    private Callback<TableColumn<CustomerModel, Void>, TableCell<CustomerModel, Void>> generateBtnForCustomerTable() {
        return new Callback<TableColumn<CustomerModel, Void>, TableCell<CustomerModel, Void>>() {
            @Override
            public TableCell<CustomerModel, Void> call(final TableColumn<CustomerModel, Void> param) {
                return new TableCell<CustomerModel, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction((ActionEvent event) -> {
                            CustomerModel customerModel = getTableView().getItems().get(getIndex());
                            String customerName = customerModel.getName();
                            eventHandler.handle(new CustomerRemoveEvent(customerName));
                            createCargoTableView(cargoData);
                            createCustomerTableView(customerData);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonsContainer = new HBox(deleteButton);
                            setGraphic(buttonsContainer);
                        }
                    }
                };
            }
        };
    }

    //src: https://stackoverflow.com/questions/21083945/how-to-avoid-not-on-fx-application-thread-currentthread-javafx-application-th/32489845#32489845
    public void updateDB(List<Cargo> cargos, Set<Customer> customers) {
        Platform.runLater(() -> {
            cargoData.clear();
            for (Cargo cargo : cargos) {
                cargoData.add(new CombinedCargoModel(cargo));
            }

            customerData.clear();
            for (Customer customer : customers) {
                customerData.add(new CustomerModel(customer));
            }
        });
    }

    public void setStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }
}
