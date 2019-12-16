package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() {

        Endpoint.publish("www.soapurl.me/ws/urls", new UrlWebServiceImpl());
    }
}
