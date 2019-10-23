package net.croz.camel.example.soap.to.rest.processor;

import net.croz.camel.example.soap.to.rest.model.GetCountryCurrencyRequest;
import net.croz.camel.example.soap.to.rest.repository.CountryRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataChangeProcessor implements Processor {

    private final CountryRepository countryRepository;

    @Autowired
    public DataChangeProcessor(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void process(final Exchange exchange) {
        final String capitalName = ((GetCountryCurrencyRequest) exchange.getIn().getBody()).getName();
        String countryName = countryRepository.findCountryNameByCapital(capitalName);

        final GetCountryCurrencyRequest request = new GetCountryCurrencyRequest();
        request.setName(countryName);

        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
        exchange.getOut().setBody(request);
    }

}
