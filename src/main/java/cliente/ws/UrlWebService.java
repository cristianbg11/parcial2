package cliente.ws;

import utilities.Urls;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface UrlWebService {

    @WebMethod
    public Urls addUrl(String url, int id);

    @WebMethod
    public Urls[] getAllUrls();

    @WebMethod
    public Urls[] getUrls(int id);
}
