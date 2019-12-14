package services;

import INF.AccesoEntity;
import INF.UrlEntity;
import INF.UsuarioEntity;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.h2.tools.Server;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import spark.Request;
import spark.Response;

import javax.persistence.EntityManager;
import java.net.InetAddress;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Inicio {
    public static ArrayList<UrlEntity> urlList = new ArrayList<UrlEntity>();

    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void IniciarUsuario(EntityManager em) {
        UsuarioEntity anonimo = new UsuarioEntity();
        anonimo.nombre = "Anonimo";
        anonimo.username = "anonimo";
        anonimo.password = "1234";
        anonimo.administrador = false;
        anonimo.email ="anonimo@gmail.com";
        anonimo.edad = 0;
        em.getTransaction().begin();
        em.persist(anonimo);
        em.getTransaction().commit();
        UsuarioEntity admin = new UsuarioEntity();
        admin.nombre = "Cristian";
        admin.username = "admin";
        admin.password = "1234";
        admin.administrador = true;
        admin.email ="cristianbg011@gmail.com";
        admin.edad = 22;
        em.getTransaction().begin();
        em.persist(admin);
        em.getTransaction().commit();
    }

    public static void InsertarUsuario(EntityManager em, Request request) {
        em.getTransaction().begin();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.username = request.queryParams("username");
        usuario.nombre = request.queryParams("nombre");
        usuario.password = request.queryParams("password");
        usuario.administrador = Boolean.parseBoolean(request.queryParams("administrador"));
        usuario.email = request.queryParams("email");
        usuario.edad = Integer.valueOf(request.queryParams("edad"));
        em.persist(usuario);
        em.getTransaction().commit();
    }

    public static void acortar(EntityManager em, UsuarioEntity usuario, String link) {
        em.getTransaction().begin();
        int n = 100000 + new Random().nextInt(900000);
        UrlEntity url = new UrlEntity();
        url.code = idToShortURL(n);
        url.url = link;
        url.cantidad = 0;
        url.usuarioByIdUsuario = usuario;
        Date date = new Date();
        url.fecha = new Timestamp(date.getTime());
        em.persist(url);
        em.getTransaction().commit();
        if(usuario.id==1){
            urlList.add(url);
        }
    }

    public static void AccesoInsert(EntityManager em, Response response, UsuarioEntity usuario, UrlEntity url, Request request, InetAddress ip) {
        url.cantidad++;
        em.getTransaction().begin();
        em.merge(url);
        em.getTransaction().commit();
        em.getTransaction().begin();
        AccesoEntity acceso = new AccesoEntity();
        Date date = new Date();
        acceso.fecha = new Timestamp(date.getTime());
        acceso.urlByIdUrl = url;
        acceso.usuarioByIdUsuario = usuario;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.userAgent());
        Browser browser = userAgent.getBrowser();
        acceso.navegador = browser.getName();
        acceso.sistema = System.getProperty("os.name");
        acceso.ip = ip.getHostAddress();
        em.persist(acceso);
        em.getTransaction().commit();
        response.redirect(url.url);
    }

    public static void makeAdmin(EntityManager em, Request request) {
        final Session sesion = getSession();
        int id = Integer.parseInt(request.queryParams("id_user"));
        UsuarioEntity usuario = sesion.find(UsuarioEntity.class, id);
        em.getTransaction().begin();
        usuario.setAdministrador(true);
        em.merge(usuario);
        em.getTransaction().commit();
    }

    public static void borrarUsuario(Request request) {
        final Session sesion = getSession();
        int id = Integer.parseInt(request.queryParams("id_user"));
        UsuarioEntity usuario = sesion.find(UsuarioEntity.class, id);
        sesion.getTransaction().begin();
        sesion.remove(usuario);
        sesion.getTransaction().commit();
    }

    public static void borrarUrl(Request request) {
        final Session sesion = getSession();
        int id = Integer.parseInt(request.queryParams("id_url"));
        UrlEntity url = sesion.find(UrlEntity.class, id);
        sesion.getTransaction().begin();
        sesion.remove(url);
        sesion.getTransaction().commit();
    }

    public static void borrarAcceso(EntityManager em, Session sesion, AccesoEntity acceso) {
        UrlEntity url = sesion.find(UrlEntity.class, acceso.urlByIdUrl.id);
        url.cantidad--;
        em.getTransaction().begin();
        em.merge(url);
        em.getTransaction().commit();
        sesion.getTransaction().begin();
        sesion.remove(acceso);
        sesion.getTransaction().commit();
    }

    public static UsuarioEntity Askuser(UsuarioEntity usuario, spark.Session session){
        if (usuario==null){
            final Session sesion = Inicio.getSession();
            usuario = sesion.find(UsuarioEntity.class, 1);
            session.attribute("usuario", usuario);
        }else {
            session.attribute("usuario", usuario);
        }
        return (UsuarioEntity)(session.attribute("usuario"));
    }

    public static void startDb() {
        try {
            Server.createTcpServer("-tcpPort",
                    "8081",
                    "-tcpAllowOthers",
                    "-tcpDaemon").start();
        }catch (SQLException ex){
            System.out.println("Problema con la base de datos: "+ex.getMessage());
        }
    }

    public static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080; //Retorna el puerto por defecto en caso de no estar en Heroku.
    }
    public static String idToShortURL(int n)
    {
        // Map to store 62 possible characters
        char map[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        StringBuffer shorturl = new StringBuffer();

        // Convert given integer id to a base 62 number
        while (n > 0)
        {
            // use above map to store actual character
            // in short url
            shorturl.append(map[n % 62]);
            n = n / 62;
        }

        // Reverse shortURL to complete base conversion
        return shorturl.reverse().toString();
    }

    // Function to get integer ID back from a short url
    static int shortURLtoID(String shortURL)
    {
        int id = 0; // initialize result

        // A simple base conversion logic
        for (int i = 0; i < shortURL.length(); i++)
        {
            if ('a' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'z')
                id = id * 62 + shortURL.charAt(i) - 'a';
            if ('A' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= 'Z')
                id = id * 62 + shortURL.charAt(i) - 'A' + 26;
            if ('0' <= shortURL.charAt(i) &&
                    shortURL.charAt(i) <= '9')
                id = id * 62 + shortURL.charAt(i) - '0' + 52;
        }
        return id;
    }
}
