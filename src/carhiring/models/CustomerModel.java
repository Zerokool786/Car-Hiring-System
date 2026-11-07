package carhiring.models;

import carhiring.models.data.Booking;
import carhiring.models.data.Car;
import carhiring.models.data.Customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerModel.java
 *
 * @version 1.0
 *
 * It represents the model for the
 * customer related data
 */
public class CustomerModel extends Model {

    // Attributes..
    private List<Car> carList;
    private List<Customer> customerList;
    private List<Booking> bookingList;

    @Override
    public void initData() {

        // Initialising the data from files.
        try {
            carList = (List<Car>) readData(Car.FILENAME);
        } catch (Exception e) {
            carList = new ArrayList<>();
        }

        try {
            customerList = (List<Customer>) readData(Customer.FILENAME);
        } catch (Exception e) {
            customerList = new ArrayList<>();
        }

        try {
            bookingList = (List<Booking>) readData(Booking.FILENAME);
        } catch (Exception e) {
            bookingList = new ArrayList<>();
        }

    }

    /**
     * @return list of available cars
     */
    public List<Car> getAvailableCars() {

        List<Car> list = new ArrayList<>();
        for (Car car: carList) {
            if (car.isAvailable()) {
                list.add(car);
            }
        }
        return list;

    }

    /**
     * To add the customer booking in
     * the database.
     *
     * @param customer booking
     *
     * @return booking done, or null if the car is not found.
     */
    public Booking addCustomerBooking(Customer customer) {

        // The customer picks from the list of available cars only,
        // so its position does not match the full car list.
        // Find the car in the full list by name instead.
        int index = findCarIndex(customer.getBookedCar());
        if (index == -1) {
            return null;
        }
        Booking booking = new Booking(customer, customer.getBookedCar().getCarAvailableDate());
        this.carList.set(index, customer.getBookedCar());
        this.customerList.add(customer);
        this.bookingList.add(booking);
        this.updateCarDetails();
        this.updateCustomerDetails();
        this.updateBookingDetails();
        return booking;

    }

    /**
     * Finds the position of a car in the full car list.
     *
     * @param car to look for
     *
     * @return position of the car, or -1 if it is not in the list.
     */
    private int findCarIndex(Car car) {

        if (car == null) {
            return -1;
        }
        for (int i = 0; i < carList.size(); i++) {
            if (carList.get(i).getName().equals(car.getName())) {
                return i;
            }
        }
        return -1;

    }

    /**
     * Updating the customer details.
     */
    private void updateCustomerDetails() {

        try {
            saveData(Customer.FILENAME, customerList);
        } catch (IOException e) {
            // unable to write the data to file.
        }

    }

    /**
     * Updating the booking details.
     */
    private void updateBookingDetails() {

        try {
            saveData(Booking.FILENAME, bookingList);
        } catch (IOException e) {
            // unable to write the data to file.
        }

    }

    /**
     * Updating the car details.
     */
    private void updateCarDetails() {

        try {
            saveData(Car.FILENAME, carList);
        } catch (IOException e) {
            // unable to write the data to file.
        }

    }

}
