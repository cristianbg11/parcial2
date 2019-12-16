package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("http://192.168.1.1/:8080/ws/urls", new UrlWebServiceImpl());
    }
}
