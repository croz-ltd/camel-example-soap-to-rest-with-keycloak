package net.croz.camel.example.soap.to.rest.controller;

import net.croz.camel.example.soap.to.rest.repository.CountryRepository;
import net.croz.camel.example.soap.to.rest.model.GetCountryCurrencyRequest;
import net.croz.camel.example.soap.to.rest.model.GetCountryCurrencyResponse;
import net.croz.camel.example.soap.to.rest.model.GetCountryRequest;
import net.croz.camel.example.soap.to.rest.model.GetCountryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class CountryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryRestController.class);

    private final CountryRepository countryRepository;

    @Autowired
    public CountryRestController(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @PostMapping("country")
    public GetCountryResponse getCountry(
        @RequestBody final GetCountryRequest request,
        @RequestHeader(name = "X-Username", required = false) final String username,
        @RequestHeader(name = "X-Password", required = false) final String password) {
        LOGGER.info("Fetching country for name: '{}'. Username: '{}', Password: '{}'", request.getName(), username,
            password);
        final GetCountryResponse response = new GetCountryResponse();
        response.setCountry(countryRepository.findCountry(request.getName()));
        return response;
    }

    @PostMapping("currency")
    public GetCountryCurrencyResponse getCountryCurrency(@RequestBody final GetCountryCurrencyRequest request) {
        LOGGER.info("Fetching currency for name: '{}'", request.getName());
        final GetCountryCurrencyResponse response = new GetCountryCurrencyResponse();
        response.setCurrency(countryRepository.findCurrency(request.getName()));
        return response;
    }

}
