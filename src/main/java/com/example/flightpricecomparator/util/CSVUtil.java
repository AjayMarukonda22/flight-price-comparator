package com.example.flightpricecomparator.util;

import com.example.flightpricecomparator.model.FlightDetails;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {

    public static void writeToCsvFile(List<FlightDetails> flightDetailsList) throws IOException {
        FileWriter fileWriter = new FileWriter("flight_comparison.csv");
        fileWriter.write("Flight Operator, Flight Number, Price on Cleartrip, Price on Paytm\n");
        for (FlightDetails flight : flightDetailsList) {
            fileWriter.write(String.format("%s, %s, %s, %s\n",
                    flight.getOperator(), flight.getFlightNumber(),
                    flight.getCleartripPrice(), flight.getPaytmPrice()));
        }
        fileWriter.flush();
        fileWriter.close();
    }
}