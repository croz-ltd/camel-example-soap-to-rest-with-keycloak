package net.croz.camel.example.soap.to.rest.route;

import net.croz.camel.example.soap.to.rest.processor.DataChangeProcessor;
import net.croz.camel.example.soap.to.rest.model.GetCountryCurrencyResponse;
import net.croz.camel.example.soap.to.rest.model.GetCountryResponse;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JaxbDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SoapToRestRoute extends RouteBuilder {

    private final DataChangeProcessor dataChangeProcessor;

    @Autowired
    public SoapToRestRoute(final DataChangeProcessor dataChangeProcessor) {
        this.dataChangeProcessor = dataChangeProcessor;
    }

    @Override
    public void configure() {
        final JaxbDataFormat jaxb = new JaxbDataFormat(false);
        jaxb.setContextPath("net.croz.camel.example.soap.to.rest.model");

        final JacksonDataFormat jackson = new JacksonDataFormat();
        jackson.setPrettyPrint(true);

        //** Country with default converter

        // Intercept all messages getCountryRequest
        from(
            "spring-ws:rootqname:{http://croz.net/camel/example/soap/to/rest/model}getCountryRequest?endpointMapping=#camelEndpointMapping")
            // Enable stream caching as we will log stream and consume it later
            .streamCaching()

            // Unmarshal received SOAP message with JAXB
            .unmarshal(jaxb)
            .log("Received SOAP: ${body}")

            // Set Username and Password in Headers
            //                .setHeader("X-Username").method("xPathHelper", "getUsername(${headers.Security})")
            //                .setHeader("X-Password").method("xPathHelper", "getPassword(${headers.Security})")

            // Set Basic Authorization for Keycloak authentication
            .setHeader("Authorization").method("xPathHelper", "getAuthorization(${headers.Security})")

            // Convert message to JSON with Jackson
            .marshal(jackson)
            .log("Converted JSON: ${body}")

            // Add HTTP header as REST endpoint is POST because we have payload
            .setHeader(Exchange.HTTP_METHOD, simple("POST"))

            // Call REST endpoint with payload
            .to("http://localhost:8080/rest/country")

            // Remove Camel HTTP headers from response message
            .removeHeader(Exchange.HTTP_METHOD)
            .removeHeader(Exchange.HTTP_RESPONSE_TEXT)
            .log("Response from REST service: ${body}")

            // Unmarshal received JSON response
            .unmarshal().json(JsonLibrary.Jackson, GetCountryResponse.class)

            // Marshal received JSON response to XML and respond back to caller
            .marshal(jaxb)
            .log("Response from REST service as SOAP: ${body}");

        //** Currency with custom converter

        // Intercept all messages getCountryCurrencyRequest
        from(
            "spring-ws:rootqname:{http://croz.net/camel/example/soap/to/rest/model}getCountryCurrencyRequest?endpointMapping=#camelEndpointMapping")
            // Enable stream caching as we will log stream and consume it later
            .streamCaching()

            // Unmarshal received SOAP message with JAXB
            .unmarshal(jaxb)
            .log("Received SOAP: ${body}")

            // Set Username and Password in Headers
            //                .setHeader("X-Username").method("xPathHelper", "getUsername(${headers.Security})")
            //                .setHeader("X-Password").method("xPathHelper", "getPassword(${headers.Security})")

            // Set Basic Authorization for Keycloak authentication
            .setHeader("Authorization").method("xPathHelper", "getAuthorization(${headers.Security})")

            // Perform custom mapping to message
            .process(dataChangeProcessor)
            .log("Modifed SOAP: ${body}")

            // Convert message to JSON with Jackson
            .marshal(jackson)
            .log("Converted JSON: ${body}")

            // Add HTTP header as REST endpoint is POST because we have payload
            .setHeader(Exchange.HTTP_METHOD, simple("POST"))

            // Call REST endpoint with payload
            .to("http://localhost:8080/rest/currency")

            // Remove Camel HTTP headers from response message
            .removeHeader(Exchange.HTTP_METHOD)
            .removeHeader(Exchange.HTTP_RESPONSE_TEXT)
            .log("Response from REST service: ${body}")

            // Unmarshal received JSON response
            .unmarshal().json(JsonLibrary.Jackson, GetCountryCurrencyResponse.class)

            // Marshal received JSON response to XML and respond back to caller
            .marshal(jaxb)
            .log("Response from REST service as SOAP: ${body}");

    }

}
