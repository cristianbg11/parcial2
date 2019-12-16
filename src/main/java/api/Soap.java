package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("http://1.2.3.4:50/ws/urls", new UrlWebServiceImpl());
    }
}
