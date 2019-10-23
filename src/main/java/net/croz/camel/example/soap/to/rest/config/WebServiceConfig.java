package net.croz.camel.example.soap.to.rest.config;

import net.croz.camel.example.soap.to.rest.helper.XPathHelper;
import org.apache.camel.component.spring.ws.bean.CamelEndpointMapping;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointAdapter;
import org.springframework.ws.server.endpoint.adapter.MessageEndpointAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean springWs(final ApplicationContext applicationContext) {
        final MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(final XsdSchema countriesSchema) {
        final DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri("/");
        wsdl11Definition.setTargetNamespace("http://croz.net/camel/example/soap/to/rest/model");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/countries.xsd"));
    }

    @Bean
    public CamelEndpointMapping camelEndpointMapping() {
        return new CamelEndpointMapping();
    }

    @Bean
    public XPathHelper xPathHelper() {
        return new XPathHelper();
    }

    @Bean
    public EndpointAdapter messageEndpointAdapter() {
        return new MessageEndpointAdapter();
    }

}
