package soap;

import cliente.ws.UrlWebServiceImpl;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static services.Inicio.getHerokuAssignedPort;

public class Soap {
    public static void init() throws Exception {
        /*
        //inicializando el servidor
        Server server = new Server(getHerokuAssignedPort());
        ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
        server.setHandler(contextHandlerCollection);
        server.start();

        //El contexto donde estoy agrupando.
        HttpContext context = build(server, "/ws");

        //El o los servicios que estoy agrupando en ese contexto
        UrlWebServiceImpl wsa = new UrlWebServiceImpl();
        Endpoint endpoint = Endpoint.create(wsa);
        endpoint.publish(context);
        // Para acceder al wsdl en http://localhost:9090/ws/urls?wsdl

        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), getHerokuAssignedPort()), 16);
        final Endpoint barEndpoint = Endpoint.create(new UrlWebServiceImpl());
        barEndpoint.publish(httpServer.createContext("/ws"));

        httpServer.start();
         */
        Endpoint.publish("https://acortadorparcial2.herokuapp.com:9090/ws/urls", new UrlWebServiceImpl());
    }

    private static HttpContext build(Server server, String contextString) throws Exception {
        JettyHttpServer jettyHttpServer = new JettyHttpServer(server, true);
        JettyHttpContext ctx = (JettyHttpContext) jettyHttpServer.createContext(contextString);
        Method method = JettyHttpContext.class.getDeclaredMethod("getJettyContextHandler");
        method.setAccessible(true);
        HttpSpiContextHandler contextHandler = (HttpSpiContextHandler) method.invoke(ctx);
        contextHandler.start();
        return ctx;
    }
}
