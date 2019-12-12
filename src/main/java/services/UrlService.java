package services;

import INF.*;
import org.hibernate.query.Query;
import utilities.Accesos;
import utilities.Urls;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class UrlService {
    EntityManager em = Inicio.getSession();
    private static List<UrlEntity> listaUrls = new ArrayList<>();
    private static List<Urls> Urls = new ArrayList<>();

    public List<Urls> getAllLinks(){
        Urls.clear();
        Query query = (Query) em.createQuery("select u from UrlEntity u");
        listaUrls = query.getResultList();
        for (UrlEntity url: listaUrls){
            Urls link = new Urls();
            link.id = url.id;
            link.cantidad = url.cantidad;
            link.code = url.code;
            link.fecha = url.fecha;
            link.url = url.url.substring(0, 14);
            link.usuario = url.usuarioByIdUsuario.username;
            for (int i = 0; i<url.accesosById.size(); i++){
                Accesos dato = new Accesos();
                dato.id = url.accesosById.get(i).id;
                dato.fecha = url.accesosById.get(i).fecha;
                dato.usuario = url.accesosById.get(i).usuarioByIdUsuario.username;
                dato.navegador = url.accesosById.get(i).navegador;
                dato.ip = url.accesosById.get(i).ip;
                dato.sistema = url.accesosById.get(i).sistema;
                link.datos.add(dato);
            }
            //link.accesosById = url.accesosById;
            Urls.add(link);
        }
        return Urls;
    }

    public List<Urls> getLinks(UsuarioEntity user){
        Urls.clear();
        Query query = (Query) em.createQuery("select u from UrlEntity u where u.usuarioByIdUsuario =:user");
        query.setParameter("user", user);
        listaUrls = query.getResultList();
        for (UrlEntity url: listaUrls){
            Urls link = new Urls();
            link.id = url.id;
            link.cantidad = url.cantidad;
            link.code = url.code;
            link.fecha = url.fecha;
            link.url = url.url.substring(0, 14);
            link.usuario = url.usuarioByIdUsuario.username;
            for (int i = 0; i<url.accesosById.size(); i++){
                Accesos dato = new Accesos();
                dato.id = url.accesosById.get(i).id;
                dato.fecha = url.accesosById.get(i).fecha;
                dato.usuario = url.accesosById.get(i).usuarioByIdUsuario.username;
                dato.navegador = url.accesosById.get(i).navegador;
                dato.ip = url.accesosById.get(i).ip;
                dato.sistema = url.accesosById.get(i).sistema;
                link.datos.add(dato);
            }
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
