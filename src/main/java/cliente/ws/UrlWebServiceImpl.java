package cliente.ws;

import INF.UsuarioEntity;
import org.hibernate.Session;
import services.UrlService;
import utilities.Urls;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static services.Inicio.acortar;
import static services.Inicio.getSession;

@WebService(endpointInterface = "cliente.ws.UrlWebService")
public class UrlWebServiceImpl implements UrlWebService {
    UrlService urlService = UrlService.getInstance();
    private static Map<Integer, Urls> urls = new HashMap<Integer,Urls>();

    @WebMethod
    public Urls addUrl(String url, int id) {
        EntityManager em = getSession();
        final Session sesion = getSession();
        UsuarioEntity usuarioEntity = sesion.find(UsuarioEntity.class, id);
        if (usuarioEntity == null){
            usuarioEntity = sesion.find(UsuarioEntity.class, 1);
        }
        acortar(em, usuarioEntity, url);
        return urlService.getAllLinks().get(urlService.getAllLinks().size() - 1);
    }

    @WebMethod
    public Urls[] getAllUrls() {
        List<Urls> urlsList = urlService.getAllLinks();
        Urls[] links = new Urls[urlsList.size()];
        for(int i=0; i< urlsList.size(); i++){
            links[i] = urlsList.get(i);
            i++;
        }
        return links;
    }

    @WebMethod
    public Urls[] getUrls(int id) {
        final Session sesion = getSession();
        UsuarioEntity user = sesion.find(UsuarioEntity.class, id);
        List<Urls> urlsList = urlService.getLinks(user);
        Urls[] links = new Urls[urlsList.size()];
        for(int i=0; i< urlsList.size(); i++){
            links[i] = urlsList.get(i);
            i++;
        }
        return links;
    }
}
