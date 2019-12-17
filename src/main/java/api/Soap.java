package api;

import cliente.ws.UrlWebServiceImpl;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;

public class Soap {
    public static void init() throws Exception {

        Endpoint.publish("http://localhost:80/ws/urls?wsdl", new UrlWebServiceImpl());
    }

}
