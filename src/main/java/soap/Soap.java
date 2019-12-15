package soap;

import cliente.ws.UrlWebServiceImpl;
import com.sun.net.httpserver.HttpServer;

import javax.xml.ws.Endpoint;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Soap {
    public static void init() throws Exception {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 80), 16);
        final Endpoint barEndpoint = Endpoint.create(new UrlWebServiceImpl());
        barEndpoint.publish(httpServer.createContext("/ws"));

        httpServer.start();
        //Endpoint.publish("http://0.0.0.0:"+getHerokuAssignedPort()+"/ws/urls", new UrlWebServiceImpl());
    }

}
