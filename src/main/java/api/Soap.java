package api;

import cliente.ws.UrlWebServiceImpl;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {
        Endpoint.publish("http://soap.lospelones.me/ws/urls", new UrlWebServiceImpl());
    }

}
