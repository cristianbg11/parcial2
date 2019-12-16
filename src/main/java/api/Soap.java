package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {
        Endpoint.publish("http://0.0.0.0:9090/ws/urls", new UrlWebServiceImpl());
    }

}
