package soap;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {

        Endpoint.publish("http://localhost:80/ws/urls", new UrlWebServiceImpl());
    }

}
