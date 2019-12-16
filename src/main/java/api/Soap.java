package api;

import cliente.ws.UrlWebServiceImpl;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {

        Server server = new Server(9090);
        JettyHttpServer jettyServer = new JettyHttpServer(server, true);
        Endpoint endpoint = Endpoint.create(new UrlWebServiceImpl());
        ContextHandlerCollection collection = new ContextHandlerCollection();
        server.setHandler(collection);

        endpoint.publish( jettyServer.createContext("/ws") );
        server.start();
        //Endpoint.publish("http://0.0.0.0:8080/ws/urls", new UrlWebServiceImpl());
    }

}
