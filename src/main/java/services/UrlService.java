package services;

import INF.*;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class UrlService {
    EntityManager em = Inicio.getSession();
    private static List<UrlEntity> listaUrls = new ArrayList<>();
    private static List<UrlEntity> Urls = new ArrayList<>();

    public List<UrlEntity> getAllLinks(){
        Urls.clear();
        Query query = (Query) em.createQuery("select u from UrlEntity u");
        listaUrls = query.getResultList();
        for (UrlEntity url: listaUrls){
            UrlEntity link = new UrlEntity();
            link.id = url.id;
            link.cantidad = url.cantidad;
            link.code = url.code;
            link.fecha = url.fecha;
            link.url = url.url.substring(0, 14);
            //link.usuarioByIdUsuario.nombre = url.usuarioByIdUsuario.nombre;
            //link.accesosById = url.accesosById;
            Urls.add(link);
        }
        return Urls;
    }

    public List<UrlEntity> getLinks(UsuarioEntity user){
        Urls.clear();
        Query query = (Query) em.createQuery("select u from UrlEntity u where u.usuarioByIdUsuario =:user");
        query.setParameter("user", user);
        listaUrls = query.getResultList();
        for (UrlEntity url: listaUrls){
            UrlEntity link = new UrlEntity();
            link.id = url.id;
            link.cantidad = url.cantidad;
            link.code = url.code;
            link.fecha = url.fecha;
            link.url = url.url.substring(0, 14);
            //link.usuarioByIdUsuario.nombre = url.usuarioByIdUsuario.nombre;
            //link.accesosById = url.accesosById;
            Urls.add(link);
        }
        return Urls;
    }
    private static UrlService instancia;
    public static UrlService getInstance(){
        if(instancia==null){
            instancia = new UrlService();
        }
        return instancia;
    }
    /*public  String getAllLinks(){
        return "Hola";
    }*/
}
