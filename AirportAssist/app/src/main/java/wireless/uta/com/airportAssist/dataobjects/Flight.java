package wireless.uta.com.airportAssist.dataobjects;

import java.io.Serializable;

/**
 * Class: Flight
 * Flight data object used to hold the details of an individual flight.
 *
 * Author: Shreyas
 */
public class Flight implements Serializable {

    //Properties
    private String flightNo;
    private String carrierCode;
    private String status;
    private String departureGate;
    private String departureTerminal;

    //Constructors
    public Flight(String flightNo,String carrierCode, String status){
        this.setFlightNo(flightNo);
        this.setCarrierCode(carrierCode);
        this.setStatus(status);
    }

    public Flight(String flightNo,String carrierCode, String status,String departureTerminal,String departureGate){
        this.setFlightNo(flightNo);
        this.setCarrierCode(carrierCode);
        this.setStatus(status);
        this.setDepartureTerminal(departureTerminal);
        this.setDepartureGate(departureGate);
    }

    //Getters and Setters
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartureGate() {
        return departureGate;
    }

    public void setDepartureGate(String departureGate) {
        this.departureGate = departureGate;
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
    }
}
