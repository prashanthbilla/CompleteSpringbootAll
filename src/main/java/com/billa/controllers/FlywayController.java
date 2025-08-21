package com.billa.controllers;

import com.billa.repository.FlywayRepository;
import com.billa.jooq.tables.pojos.Orders;
import com.billa.models.Company;
import com.billa.models.PersonAndAddress;
import com.billa.models.PersonDetails;
import com.billa.models.Prashanth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1")
public class FlywayController {

    @Autowired
    private FlywayRepository repository;


    @GetMapping("")
    public ResponseEntity<String> show(){
        PersonAndAddress personAndAddress = new PersonAndAddress();
        personAndAddress.setId((long) 1);
        personAndAddress.setAddressAmount(5000);

        personAndAddress.setPersonId((long) 9);
        personAndAddress.setAddressPersonId((long) 9900);
        personAndAddress.setAmount(55);
        personAndAddress.setAddressId((long) 100);
        Prashanth prashanth = MapperInterface.INSTANCE.mapperPrashantFromPersonAndAddress(personAndAddress);
        prashanth.setName("Prashanth");
        System.out.println(" Person :  "+prashanth);

        Company company = new Company("BillaCompany", "Hyderabad");

        PersonDetails personDetails = MapperInterface.INSTANCE.mapperPersonAndCompnayToPersonDetails(company, prashanth);
        personDetails.setEmail("billa@gmail.com");
        System.out.println(" Person Details :  "+personDetails);




        return new ResponseEntity<>("Welcome to Application", HttpStatus.OK);
    }

    @PostMapping("/save")
    public Orders save(@RequestBody Orders orders){
        return repository.save(orders);
    }
}

