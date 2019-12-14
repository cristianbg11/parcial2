package cliente.ws;

import INF.UrlEntity;
import utilities.Urls;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface UrlWebService {

    @WebMethod
    public boolean addUrl(UrlEntity u);

    @WebMethod
    public UrlEntity getUrl(int id);

    @WebMethod
    public UrlEntity[] getAllUrls();
}
