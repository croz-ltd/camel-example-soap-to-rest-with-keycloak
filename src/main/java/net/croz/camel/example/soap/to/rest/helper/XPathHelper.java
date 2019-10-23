package net.croz.camel.example.soap.to.rest.helper;

import org.springframework.ws.soap.SoapHeaderElement;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Base64;

public class XPathHelper {

    private static final String USERNAME_XPATH = "//*[local-name()='UsernameToken']/*[local-name()='Username']/text()";
    private static final String PASSWORD_XPATH = "//*[local-name()='UsernameToken']/*[local-name()='Password']/text()";

    private static Document convertStringToXMLDocument(String xmlString) {
        if (xmlString != null) {
            try {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                return builder.parse(new InputSource(new StringReader(xmlString)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUsername(final SoapHeaderElement soapHeader) {
        return getValue(soapHeader, USERNAME_XPATH);
    }

    public String getPassword(final SoapHeaderElement soapHeader) {
        return getValue(soapHeader, PASSWORD_XPATH);
    }

    public String getAuthorization(final SoapHeaderElement soapHeader) {
        return "Basic " + base64(getValue(soapHeader, USERNAME_XPATH) + ":" + getValue(soapHeader, PASSWORD_XPATH));
    }

    private String base64(final String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    private String getValue(final SoapHeaderElement soapHeader, final String xpathString) {
        Document doc = convertHeaderToDocument(soapHeader);
        if (doc != null) {
            try {

                XPathFactory factory = XPathFactory.newInstance();
                XPath xpath = factory.newXPath();
                XPathExpression expr = xpath.compile(xpathString);

                return (String) expr.evaluate(doc, XPathConstants.STRING);
            } catch (XPathExpressionException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    private Document convertHeaderToDocument(SoapHeaderElement soapHeader) {
        if (soapHeader != null) {
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                Source source = soapHeader.getSource();
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                transformer.transform(source, xmlOutput);
                return convertStringToXMLDocument(xmlOutput.getWriter().toString());
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
