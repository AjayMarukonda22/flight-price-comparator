package com.example.flightpricecomparator.service;

import com.example.flightpricecomparator.model.FlightDetails;
import com.example.flightpricecomparator.util.CSVUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightComparatorService {

    private final String CLEARTRIP_URL = "https://www.cleartrip.com/flights/results?adults=1&childs=0&infants=0&depart_date=%s&return_date=&intl=n&from=BLR&to=DEL&airline=&carrier=&sd=1727870041088&page=&sellingCountry=IN&ssfi=&flexi_search=&ssfc=&origin=BLR%%20-%20Bangalore,%%20IN&destination=DEL%%20-%20New%%20Delhi,%%20IN&class=Economy&sft=";
    private final String PAYTM_URL = "https://tickets.paytm.com/flights/flightSearch/BLR-Bengaluru/DEL-Delhi/1/0/0/E/%s?stops=nonstop";// URL

    public void comparePricesCommandLine(String travelDate) throws IOException {
        System.out.println("starting intializing of driver");
        WebDriver driver = initializeDriver(); // Use the new method to initialize the driver
        System.out.println("The driver is intialized");
        List<FlightDetails> flightDetailsList = new ArrayList<>();

        try {
            fetchCleartripData(driver, travelDate, flightDetailsList);
            fetchPaytmData(driver, travelDate, flightDetailsList);
        } catch (NoSuchElementException e) {
            System.out.println("Element not found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred while fetching flight data: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit(); // Ensure driver is quit properly
            }
        }

        if (flightDetailsList.isEmpty()) {
            System.out.println("No non-stop flights found for the given date.");
        } else {
            try {
                CSVUtil.writeToCsvFile(flightDetailsList);
                System.out.println("Flight comparison saved to flight_comparison.csv");
            } catch (IOException e) {
                System.out.println("Error writing to CSV: " + e.getMessage());
            }
        }
    }

    private static WebDriver initializeDriver() {
        // Set the path to your local ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe");  // Windows Example
        // For Linux/Mac, use the correct path: "/path/to/chromedriver"

        // Set ChromeOptions for headless mode (optional)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode to avoid opening a window
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Initialize and return the ChromeDriver instance
        return new ChromeDriver(options);
    }
    private void fetchCleartripData(WebDriver driver, String travelDate, List<FlightDetails> flightDetailsList) {
        driver.get(String.format(CLEARTRIP_URL, travelDate));
        List<WebElement> flights = driver.findElements(By.cssSelector(".flightDetails"));

        if (flights.isEmpty()) {
            System.out.println("No flights found on Cleartrip.");
            return;
        }

        for (WebElement flight : flights) {
            String stops = flight.findElement(By.cssSelector(".stops")).getText().toLowerCase();
            if (stops.contains("non-stop")) {
                String flightNumber = flight.findElement(By.cssSelector(".flightNumber")).getText();
                String operator = flight.findElement(By.cssSelector(".flightOperator")).getText();
                String price = flight.findElement(By.cssSelector(".flightPrice")).getText();
                flightDetailsList.add(new FlightDetails(operator, flightNumber, price, "No Data Found"));
            }
        }
    }

    private void fetchPaytmData(WebDriver driver, String travelDate, List<FlightDetails> flightDetailsList) {
        driver.get(String.format(PAYTM_URL, travelDate));
        List<WebElement> flights = driver.findElements(By.cssSelector(".flightCard"));

        if (flights.isEmpty()) {
            System.out.println("No flights found on Paytm.");
            return;
        }

        for (WebElement flight : flights) {
            String stops = flight.findElement(By.cssSelector(".stops")).getText().toLowerCase();
            if (stops.contains("non-stop")) {
                String flightNumber = flight.findElement(By.cssSelector(".flightNumber")).getText();
                String operator = flight.findElement(By.cssSelector(".flightOperator")).getText();
                String price = flight.findElement(By.cssSelector(".flightPrice")).getText();

                flightDetailsList.stream()
                        .filter(f -> f.getFlightNumber().equals(flightNumber))
                        .forEach(f -> f.setPaytmPrice(price));
            }
        }
    }
}
