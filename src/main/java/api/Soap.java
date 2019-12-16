package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("http://10.0.0.11/ws/urls", new UrlWebServiceImpl());
    }
}
