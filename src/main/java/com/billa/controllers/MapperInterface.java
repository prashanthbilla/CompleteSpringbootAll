package com.billa.controllers;

import com.billa.models.Company;
import com.billa.models.PersonAndAddress;
import com.billa.models.PersonDetails;
import com.billa.models.Prashanth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperInterface {

    MapperInterface INSTANCE = Mappers.getMapper(MapperInterface.class);
    Prashanth mapperPrashantFromPersonAndAddress(PersonAndAddress personAndAddress);
    @Mapping(source = "company.companyName1", target = "companyName")
    PersonDetails mapperPersonAndCompnayToPersonDetails(Company company, Prashanth prashanth);
}
