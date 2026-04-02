# CarHiring

The whole project of Car Rental System is developed using the complete Model-View-Controller pattern.

I have used the 2 patterns to support the MVC which are given below.

1: Observer Pattern
   It is used to connect the main application with the controllers and further used to pass the specific data between views and controllers.
   
2: Factory Pattern
   It is used to create the controllers with full package including model and views. It makes them ready to use and return them.
   
As I used the serialized database in our system, so there are 3 different files which will be generated through system.

1: cars.ser
   It contains all of the serialized Car class objects.

2: customers.ser
   It contains all of the serialized Customer class objects.

3: bookings.ser
   It contains all of the serialized Booking class objects.
