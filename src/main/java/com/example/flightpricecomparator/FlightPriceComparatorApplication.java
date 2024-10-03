package com.example.flightpricecomparator;

import com.example.flightpricecomparator.service.FlightComparatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@SpringBootApplication
public class FlightPriceComparatorApplication implements CommandLineRunner {

    @Autowired
    private FlightComparatorService flightComparatorService;

    public static void main(String[] args) {
        SpringApplication.run(FlightPriceComparatorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Set a fixed date for testing that is 14 days from today (for example)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 14); // Adjust as necessary for testing
        String travelDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());

        try {
            System.out.println(travelDate);
            flightComparatorService.comparePricesCommandLine(travelDate);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
