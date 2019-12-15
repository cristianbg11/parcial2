package soap;

import cliente.ws.UrlWebServiceImpl;
import com.sun.net.httpserver.HttpContext;
import org.eclipse.jetty.http.spi.HttpSpiContextHandler;
import org.eclipse.jetty.http.spi.JettyHttpContext;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.server.Server;

import javax.xml.ws.Endpoint;
import java.lang.reflect.Method;

public class Soap {
    public static void init() throws Exception {
        Endpoint.publish("http://0.0.0.0/ws/urls", new UrlWebServiceImpl());
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
