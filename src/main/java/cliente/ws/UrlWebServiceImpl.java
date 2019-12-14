package cliente.ws;

import INF.UrlEntity;
import utilities.Urls;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebService(endpointInterface = "cliente.ws.UrlWebService")
public class UrlWebServiceImpl implements UrlWebService {

    private static Map<Integer, UrlEntity> urls = new HashMap<Integer,UrlEntity>();

    @WebMethod
    public boolean addUrl(UrlEntity u) {
        if(urls.get(u.getId()) != null) return false;
        urls.put(u.getId(), u);
        return true;
    }

    @WebMethod
    public UrlEntity getUrl(int id) {
        return urls.get(id);
    }

    @WebMethod
    public UrlEntity[] getAllUrls() {
        Set<Integer> ids = urls.keySet();
        UrlEntity[] u = new UrlEntity[ids.size()];
        int i=0;
        for(Integer id : ids){
            u[i] = urls.get(id);
            i++;
        }
        return u;
    }
}
