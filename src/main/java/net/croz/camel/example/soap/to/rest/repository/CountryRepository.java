package net.croz.camel.example.soap.to.rest.repository;

import net.croz.camel.example.soap.to.rest.model.Country;
import net.croz.camel.example.soap.to.rest.model.Currency;

public interface CountryRepository {

    Country findCountry(String name);

    Currency findCurrency(String name);

    String findCountryNameByCapital(String name);

}
