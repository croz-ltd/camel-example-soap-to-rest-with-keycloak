package net.croz.camel.example.soap.to.rest.repository.impl;

import net.croz.camel.example.soap.to.rest.model.Country;
import net.croz.camel.example.soap.to.rest.model.Currency;
import net.croz.camel.example.soap.to.rest.repository.CountryRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryRepositoryMock implements CountryRepository {

    private static final Map<String, Country> countries = new HashMap<>();
    private static final Map<String, Country> capitals = new HashMap<>();

    @PostConstruct
    public void initData() {
        Country spain = new Country();
        spain.setName("Spain");
        spain.setCapital("Madrid");
        spain.setCurrency(Currency.EUR);
        spain.setPopulation(46704314);

        countries.put(spain.getName(), spain);
        capitals.put(spain.getCapital(), spain);

        Country croatia = new Country();
        croatia.setName("Croatia");
        croatia.setCapital("Zagreb");
        croatia.setCurrency(Currency.HRK);
        croatia.setPopulation(4105493);

        countries.put(croatia.getName(), croatia);
        capitals.put(croatia.getCapital(), croatia);

        Country uk = new Country();
        uk.setName("United Kingdom");
        uk.setCapital("London");
        uk.setCurrency(Currency.GBP);
        uk.setPopulation(63705000);

        countries.put(uk.getName(), uk);
        capitals.put(uk.getCapital(), uk);
    }

    public Country findCountry(String name) {
        Assert.notNull(name, "The country's name must not be null");
        return countries.get(name);
    }

    @Override
    public Currency findCurrency(String name) {
        Assert.notNull(name, "The country's name must not be null");
        return findCountry(name).getCurrency();
    }

    @Override
    public String findCountryNameByCapital(String name) {
        Assert.notNull(name, "The capital's name must not be null");
        return capitals.get(name).getName();
    }

}
