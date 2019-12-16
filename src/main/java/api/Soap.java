package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("http://127.0.0.1:9090/ws/urls", new UrlWebServiceImpl());
    }
}
