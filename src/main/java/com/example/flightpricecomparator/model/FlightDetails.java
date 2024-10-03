package com.example.flightpricecomparator.model;


public class FlightDetails {

    private String operator;
    private String flightNumber;
    private String cleartripPrice;
    private String paytmPrice;

    public FlightDetails(String operator, String flightNumber, String cleartripPrice, String paytmPrice) {
        this.operator = operator;
        this.flightNumber = flightNumber;
        this.cleartripPrice = cleartripPrice;
        this.paytmPrice = paytmPrice;
    }

    // Getters and Setters

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCleartripPrice() {
        return cleartripPrice;
    }

    public void setCleartripPrice(String cleartripPrice) {
        this.cleartripPrice = cleartripPrice;
    }

    public String getPaytmPrice() {
        return paytmPrice;
    }

    public void setPaytmPrice(String paytmPrice) {
        this.paytmPrice = paytmPrice;
    }
}
