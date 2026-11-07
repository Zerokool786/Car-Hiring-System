package carhiring.controllers;

import carhiring.ReceiptStage;
import carhiring.models.CustomerModel;
import carhiring.models.Model;
import carhiring.models.data.Booking;
import carhiring.models.data.Car;
import carhiring.models.data.Customer;
import carhiring.utils.SceneType;
import carhiring.utils.SceneUpdater;
import carhiring.views.CustomerView;
import carhiring.views.View;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.Period;

/**
 * CustomerCustomer.java
 *
 * @version 1.0
 *
 * It represents the controller to handle
 * the customer model and views.
 */
public class CustomerController extends Controller implements ChangeListener<LocalDate> {

    // Attributes..
    private long days;
    private LocalDate returnDate;

    /**
     * It will initialise all attributes.
     *
     * @param updater to change the view.
     * @param model   current model of controller
     * @param view    current view of controller
     */
    public CustomerController(SceneUpdater updater, Model model, View view) {

        super(updater, model, view);
        this.days = 0;

        // Populating the data.
        getCustomerView().getAvailableCarsTable().getItems().addAll(getCustomerModel().getAvailableCars());

    }

    /**
     * @return customer model
     */
    public CustomerModel getCustomerModel() {

        CustomerModel model = (CustomerModel) getModel();
        return model;

    }

    /**
     * @return customer view
     */
    public CustomerView getCustomerView() {

        CustomerView view = (CustomerView) getView();
        return view;

    }

    @Override
    public void handle(ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();
        switch (button.getId()) {
            case "BOOK_CAR":

                if (returnDate != null) {
                    days = Period.between(LocalDate.now(), returnDate).getDays();
                    Car selectedCar = getCustomerView().getAvailableCarsTable().getSelectionModel().getSelectedItem();
                    if (selectedCar != null && days > 0) {

                        String name = getCustomerView().getNameField().getText();
                        String address = getCustomerView().getAddressField().getText();
                        String license = getCustomerView().getLicenseField().getText();
                        if (name.isEmpty() || address.isEmpty() || license.isEmpty()) {
                            getView().createAlert(Alert.AlertType.ERROR, "No any data field should be empty.");
                        } else {

                            Customer customer = new Customer(name, address, license, selectedCar);
                            selectedCar.setAvailable(false);
                            selectedCar.setCarAvailableDate(returnDate);
                            Booking booking = getCustomerModel().addCustomerBooking(customer);
                            getCustomerView().getAvailableCarsTable().getItems().clear();
                            getCustomerView().getAvailableCarsTable().getItems().addAll(getCustomerModel().getAvailableCars());

                           if (booking != null) {

                               generateReceipt(booking);
                               getView().createAlert(Alert.AlertType.INFORMATION, "Booking is completed!");

                           } else {
                               getView().createAlert(Alert.AlertType.ERROR, "Booking invalid!");
                           }

                        }

                    } else {
                        getView().createAlert(Alert.AlertType.ERROR, "Select correct Car and Date.");
                    }
                } else {
                    getView().createAlert(Alert.AlertType.ERROR, "Select correct Car and Date.");
                }

                break;
            case "LOGOUT_CUSTOMER":
                getUpdater().updateControllerSceneType(SceneType.LOGIN);
                break;
        }

    }

    /**
     * It will generate a receipt here.
     *
     * @param booking to do booking
     */
    private void generateReceipt(Booking booking) {

        Thread thread = new Thread() {

            @Override
            public void run () {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        ReceiptStage receiptStage = new ReceiptStage();
                        receiptStage.setBooking(booking);
                        receiptStage.show();

                    }
                });

            }

        };
        thread.start();

    }

    // When date is changed.
    @Override
    public void changed(ObservableValue<? extends LocalDate> value, LocalDate old, LocalDate update) {

        returnDate = update;

        days = Period.between(LocalDate.now(), returnDate).getDays();
        Car selectedCar = getCustomerView().getAvailableCarsTable().getSelectionModel().getSelectedItem();
        if (selectedCar != null && days > 0) {
            getCustomerView().setTotalCost(days * selectedCar.getCostPerDay());
        } else {
            getCustomerView().setTotalCost(0);
        }

    }

}
