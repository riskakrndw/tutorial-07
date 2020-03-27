package com.apap.tu07.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apap.tu07.model.FlightModel;
import com.apap.tu07.model.PilotModel;
import com.apap.tu07.service.FlightService;
import com.apap.tu07.service.PilotService;

/**
 * FlightController
 */
@RestController
@RequestMapping("/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private PilotService pilotService;

    
    @PostMapping(value = "/add/{licenseNumber}")
    private FlightModel addFlight(@PathVariable(value = "licenseNumber") String licenseNumber,
    		@RequestBody FlightModel flight, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		flight.setPilot(pilot);
        return flightService.addFlight(flight);
    }

    @GetMapping(value = "/view/{flightNumber}")
    private FlightModel flightView(@PathVariable("flightNumber") String flightNumber) {
        FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        return flight;
    }

    @GetMapping(value = "/all")
    private List<FlightModel> flightViewAll(@ModelAttribute FlightModel flight) {
        return flightService.getAllFlight();
    }
    
    @DeleteMapping(value = "/{flightNumber}")
    private String deleteFlight(@PathVariable("flightNumber") String flightNumber) {
        FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        flightService.deleteFlight(flight);
        return "flight has been deleted";
    }
    
    @PutMapping(value = "/update/{flightNumber}")
    private String updateFlight(@PathVariable("flightNumber") String flightNumber,
    		@RequestParam("destination") String destination,
    		@RequestParam("origin") String origin,
    		@RequestParam("time") Date time) {
        FlightModel flight = flightService.getFlightDetailByFlightNumber(flightNumber).get();
        if(flight.equals(null)) {
        	return "Couldn't find your flight";
        }
        flight.setDestination(destination);
        flight.setOrigin(origin);
        flight.setTime(time);
        flightService.updateFlight(flightNumber, flight);
        return "flight update success";
    }
}