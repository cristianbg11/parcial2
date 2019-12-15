package soap;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {
        Endpoint.publish("https://acortadorparcial2.herokuapp.com/ws/urls", new UrlWebServiceImpl());
    }

}
