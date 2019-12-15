package soap;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("http://soap.com/ws/urls", new UrlWebServiceImpl());
    }

}
