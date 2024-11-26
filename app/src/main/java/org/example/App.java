package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



@SpringBootApplication
@RestController
public class App {
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "admin";

    @GetMapping("/getCars")
    public List<String> getCarsByMake(@RequestParam(value = "make") String make) {
        List<String> cars = new ArrayList<>();

        // Corrected SQL query with a placeholder for the "make" parameter
        String query = "SELECT * FROM public.cars WHERE make = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the parameter for the "make" value
            statement.setString(1, make);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Assuming columns are "model" and "year" in your "cars" table.
                String car = "Model: " + resultSet.getString("model") + ", Year: " + resultSet.getInt("year");
                cars.add(car);
            }

        } catch (Exception e) {
            e.printStackTrace();  // Log the exception or handle appropriately
        }

        return cars;  // This will be automatically converted to JSON by Spring Boot
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);  // Launch the Spring Boot application
    }
}
