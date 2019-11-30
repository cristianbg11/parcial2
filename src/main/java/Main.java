import INF.*;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.*;

import static spark.Spark.*;

public class Main {
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

    public static void main(final String[] args) throws Exception {
        final Session secion = getSession();
        port(getHerokuAssignedPort());

        staticFiles.location("/publico");
        EntityManager em = getSession();
        InetAddress ip = InetAddress.getLocalHost();

        if (secion.find(UsuarioEntity.class, 1)==null){
            UsuarioEntity anonimo = new UsuarioEntity();
            anonimo.nombre = "Anonimo";
            anonimo.username = "anonimo";
            anonimo.password = "1234";
            anonimo.administrador = false;
            anonimo.email ="anonimo@gmail.com";
            anonimo.edad = 0;
            anonimo.ip = ip.getHostAddress();
            //anonimo.sistema = req.userAgent();
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
            admin.ip = ip.getHostAddress();
            //admin.sistema = req.userAgent();
            em.getTransaction().begin();
            em.persist(admin);
            em.getTransaction().commit();
        }

        get("/", (request, response)-> {
            //response.redirect("/login.html");
            final Session sesion = getSession();
            if (request.cookie("CookieUsuario") != null){
                int id = Integer.parseInt((request.cookie("CookieUsuario")));
                UsuarioEntity usuarioEntity = sesion.find(UsuarioEntity.class, id);
                spark.Session session=request.session(true);
                session.attribute("usuario", usuarioEntity);
                response.redirect("/index");
            }
            return renderContent("publico/login.html");
        });

        get("/visitar", (request, response)-> {
            //response.redirect("/login.html");
            final Session sesion = getSession();
            UsuarioEntity usuario = sesion.find(UsuarioEntity.class, 1);
            spark.Session session=request.session(true);
            session.attribute("usuario", usuario);
            em.getTransaction().begin();
            usuario.setSistema(request.userAgent());
            em.merge(usuario);
            em.getTransaction().commit();
            session.attribute("usuario", usuario);
            response.redirect("/index");
            return "anonimo";
        });

        post("/sesion", (request, response)-> {
            List<UsuarioEntity> users = em.createQuery("select u from UsuarioEntity u", UsuarioEntity.class).getResultList();
            String username = request.queryParams("user");
            String password = request.queryParams("pass");
            spark.Session session=request.session(true);

            for(UsuarioEntity usuario : users){
                if (usuario.username.equals(username) && usuario.password.equals(password)){
                    session.attribute("usuario", usuario);
                    em.getTransaction().begin();
                    usuario.setSistema(request.userAgent());
                    em.merge(usuario);
                    em.getTransaction().commit();
                    if (request.queryParams("recordatorio") !=null && request.queryParams("recordatorio").equals("si") ){
                        Map<String, String> cookies=request.cookies();
                        response.cookie("/", "CookieUsuario", String.valueOf(usuario.id), 604800, true);
                        /*
                        for (String key : cookies.keySet()) {
                            if (key != null) {
                                response.removeCookie(key);
                                response.cookie("/", "CookieUsuario", cookies.get(key), 604800, false);
                            }
                        }
                        */

                    }
                    response.redirect("/index");
                }
            }
            response.redirect("/");
            return 0;
        });

        post("/insertar", (request, response) -> {
            em.getTransaction().begin();
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.username = request.queryParams("username");
            usuario.nombre = request.queryParams("nombre");
            usuario.password = request.queryParams("password");
            usuario.administrador = Boolean.parseBoolean(request.queryParams("administrador"));
            usuario.email = request.queryParams("email");
            usuario.edad = Integer.valueOf(request.queryParams("edad"));
            usuario.ip = ip.getHostAddress();
            usuario.sistema = request.userAgent();
            em.persist(usuario);
            em.getTransaction().commit();
            response.redirect("/");
            return "Usuario Creado";
        }); // Crea un usuario

        get("/index", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario==null){
                final Session sesion = getSession();
                usuario = sesion.find(UsuarioEntity.class, 1);
                session.attribute("usuario", usuario);
            }else {
                session.attribute("usuario", usuario);
            }
            Query query = (Query) em.createQuery("select u from UrlEntity u");
            List<UrlEntity> urls = query.getResultList();
            attributes.put("usuario",usuario);
            attributes.put("urls", urls);
            return new ModelAndView(attributes, "index.ftl");
        } , new FreeMarkerEngine());

        get("/perfil", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario==null){
                final Session sesion = getSession();
                usuario = sesion.find(UsuarioEntity.class, 1);
                session.attribute("usuario", usuario);
            }else {
                session.attribute("usuario", usuario);
            }
            Query query = (Query) em.createQuery("select u from UrlUsuarioEntity u where u.usuarioByIdUsuario = :user");
            query.setParameter("user", usuario);
            List<UrlUsuarioEntity> urls = query.getResultList();
            Timestamp fecha = usuario.urlUsuariosById.get(usuario.urlUsuariosById.size()-1).fecha;
            attributes.put("usuario",usuario);
            attributes.put("urls", urls);
            attributes.put("fecha", fecha);
            return new ModelAndView(attributes, "profile.ftl");
        } , new FreeMarkerEngine());


        get("/usuarios", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario.administrador==false){
                response.redirect("/index");
            }
            Query query = (Query) em.createQuery("select u from UsuarioEntity u");
            List<UsuarioEntity> usuarios = query.getResultList();
            //urls.get().urlByIdUrl.code
            attributes.put("usuario",usuario);
            attributes.put("usuarios", usuarios);
            return new ModelAndView(attributes, "table.ftl");
        } , new FreeMarkerEngine());

        get("/urls", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario.administrador==false){
                response.redirect("/index");
            }
            Query query = (Query) em.createQuery("select u from UrlUsuarioEntity u");
            List<UrlUsuarioEntity> urls = query.getResultList();
            //urls.get().urlByIdUrl.code
            attributes.put("usuario",usuario);
            attributes.put("urls", urls);
            return new ModelAndView(attributes, "urls.ftl");
        } , new FreeMarkerEngine());

        post("/acortar", (request, response) -> {
            em.getTransaction().begin();
            int n = 100000 + new Random().nextInt(900000);
            UrlEntity url = new UrlEntity();
            url.code = idToShortURL(n);
            url.url = request.queryParams("url");
            url.cantidad = 0;
            em.persist(url);
            em.getTransaction().commit();
            response.redirect("/index");
            return "Usuario Creado";
        });
        /*
        get("/desviar", (request, response) -> {
            final Session sesion = getSession();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario==null){
                usuario = sesion.find(UsuarioEntity.class, 1);
                session.attribute("usuario", usuario);
            }else {
                session.attribute("usuario", usuario);
            }
            Map<String, Object> attributes = new HashMap<>();
            int id = Integer.parseInt(request.queryParams("id"));
            UrlEntity url = sesion.find(UrlEntity.class, id);
            UrlUserInsert(em, response, usuario, url);
            return "Desvio";
        });
        */

        get("/salir", (request, response)->{
            spark.Session session=request.session(true);
            session.invalidate();
            response.removeCookie("CookieUsuario");
            response.redirect("/");
            return "Sesion finalizada";
        }); //Finaliza Sesión

        get("/delete", (request, response)-> {
            final Session sesion = getSession();
            int id = Integer.parseInt(request.queryParams("id_url"));
            UrlEntity url = sesion.find(UrlEntity.class, id);
            sesion.getTransaction().begin();
            sesion.remove(url);
            sesion.getTransaction().commit();
            response.redirect("/urls");
            return "Url Borrado";
        });

        get("/deleteuser", (request, response)-> {
            final Session sesion = getSession();
            int id = Integer.parseInt(request.queryParams("id_user"));
            UsuarioEntity usuario = sesion.find(UsuarioEntity.class, id);
            sesion.getTransaction().begin();
            sesion.remove(usuario);
            sesion.getTransaction().commit();
            response.redirect("/usuarios");
            return "Url Borrado";
        });

        post("/update", (request, response)-> {
            final Session sesion = getSession();
            int id = Integer.parseInt(request.queryParams("id_user"));
            UsuarioEntity usuario = sesion.find(UsuarioEntity.class, id);
            em.getTransaction().begin();
            usuario.setAdministrador(true);
            em.merge(usuario);
            em.getTransaction().commit();
            response.redirect("/usuarios");
            return "Usuario Actualizado";
        });

        get("/stats", (request, response)-> {
            final Session sesion = getSession();
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario==null){
                usuario = sesion.find(UsuarioEntity.class, 1);
                session.attribute("usuario", usuario);
            }else {
                session.attribute("usuario", usuario);
            }
            if (usuario.administrador==false){
                response.redirect("/index");
            }
            int id = Integer.parseInt(request.queryParams("id_url"));
            UrlEntity url = sesion.find(UrlEntity.class, id);
            Query query = (Query) em.createQuery("select u from UrlUsuarioEntity u where u.urlByIdUrl = :url");
            query.setParameter("url", url);
            List<UrlUsuarioEntity> urlusers = query.getResultList();
            attributes.put("urlusers", urlusers);
            attributes.put("url",url);
            attributes.put("usuario",usuario);
            return new ModelAndView(attributes, "blank.ftl");
        } , new FreeMarkerEngine());

        get("/:code", ((request, response) -> {
            final Session sesion = getSession();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            if (usuario==null){
                usuario = sesion.find(UsuarioEntity.class, 1);
                session.attribute("usuario", usuario);
            }
            em.getTransaction().begin();
            usuario.setSistema(request.userAgent());
            em.merge(usuario);
            em.getTransaction().commit();
            String codigo = request.params("code");
            Query<UrlEntity> query = (Query<UrlEntity>) em.createQuery("select u from UrlEntity u where u.code=:code", UrlEntity.class);
            query.setParameter("code", codigo);
            UrlEntity url = query.uniqueResult();
            UrlUserInsert(em, response, usuario, url);
            return "redirecionado";
        }));
    }

    private static void UrlUserInsert(EntityManager em, Response response, UsuarioEntity usuario, UrlEntity url) {
        url.cantidad++;
        em.getTransaction().begin();
        em.merge(url);
        em.getTransaction().commit();
        em.getTransaction().begin();
        UrlUsuarioEntity urlUsuario = new UrlUsuarioEntity();
        Date date = new Date();
        urlUsuario.fecha = new Timestamp(date.getTime());
        urlUsuario.urlByIdUrl = url;
        urlUsuario.usuarioByIdUsuario = usuario;
        em.persist(urlUsuario);
        em.getTransaction().commit();
        response.redirect(url.url);
    }


    private static String renderContent(String htmlFile) throws IOException, URISyntaxException {
        URL url = Main.class.getResource(htmlFile);
        Path path = Paths.get(url.toURI());
        return new String(Files.readAllBytes(path), Charset.defaultCharset());
    }

    static int getHerokuAssignedPort() {
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