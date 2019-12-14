import INF.AccesoEntity;
import INF.UrlEntity;
import INF.UsuarioEntity;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.UrlService;
import soap.Soap;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import utilities.Usuarios;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import static services.Inicio.*;
import static spark.Spark.*;
import static utilities.JsonUtil.json;

public class Main {

    public static void main(final String[] args) throws Exception {
        Class.forName("org.h2.Driver");

        port(getHerokuAssignedPort());
        startDb();
        Soap.init();
        final Session secion = getSession();
        staticFiles.location("/publico");
        EntityManager em = getSession();
        InetAddress ip = InetAddress.getLocalHost();
        UrlService urlService = UrlService.getInstance();

        if (secion.find(UsuarioEntity.class, 1)==null){
            IniciarUsuario(em);
        }

        path("/rest", ()->{

            after("/*", (req, res) -> {
                res.type("application/json");
            });
            get("/", (request, response) -> "RUTA API REST");
            path("/links", () -> {

                get("", (request, response) -> urlService.getAllLinks(), json());
                get("/user/:id", (request, response) -> {
                    final Session sesion = getSession();
                    UsuarioEntity persona = sesion.find(UsuarioEntity.class, Integer.parseInt(request.params(":id")));
                    return urlService.getLinks(persona);
                }, json());

            });

            path("/url", () -> {
                post("/crear/:id", "application/json", (request, response) -> {
                    final Session sesion = getSession();
                    UsuarioEntity usuarioEntity = sesion.find(UsuarioEntity.class, Integer.parseInt(request.params("id")));
                    if (usuarioEntity == null){
                        usuarioEntity = sesion.find(UsuarioEntity.class, 1);
                    }
                    UrlEntity urlEntity = new Gson().fromJson(request.body(), UrlEntity.class);
                    acortar(em, usuarioEntity, urlEntity.url);
                    return urlService.getAllLinks().get(urlService.getAllLinks().size() - 1);
                }, json());
            });


        });

        get("/", (request, response)-> {
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
            final Session sesion = getSession();
            UsuarioEntity usuario = sesion.find(UsuarioEntity.class, 1);
            spark.Session session=request.session(true);
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
                    if (request.queryParams("recordatorio") !=null && request.queryParams("recordatorio").equals("si") ){
                        Map<String, String> cookies=request.cookies();
                        response.cookie("/", "CookieUsuario", String.valueOf(usuario.id), 604800, false);
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
                    return 0;
                }
            }
            response.redirect("/");
            return 0;
        });

        post("/insertar", (request, response) -> {
            InsertarUsuario(em, request);
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
            } else if (usuario.id==1){
                attributes.put("usuario",usuario);
                attributes.put("urls",urlList);
            } else {
                session.attribute("usuario", usuario);
                Query query = (Query) em.createQuery("select u from UrlEntity u where u.usuarioByIdUsuario=:user");
                query.setParameter("user", usuario);
                List<UrlEntity> urls = query.getResultList();
                attributes.put("usuario",usuario);
                attributes.put("urls", urls);
            }
            return new ModelAndView(attributes, "index.ftl");
        } , new FreeMarkerEngine());

        get("/perfil", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            Query query = (Query) em.createQuery("select distinct a.urlByIdUrl from AccesoEntity a where a.usuarioByIdUsuario = :user");
            query.setParameter("user", usuario);
            List<UrlEntity> urls = query.getResultList();
            Query query1 = (Query) em.createQuery("select u from UrlEntity u where u.usuarioByIdUsuario = :user");
            query1.setParameter("user", usuario);
            List<UrlEntity> acortados = query1.getResultList();
            boolean profile = true;
            attributes.put("profile", profile);
            attributes.put("usuario",usuario);
            attributes.put("urls", urls);
            attributes.put("acortados", acortados);
            return new ModelAndView(attributes, "profile.ftl");
        } , new FreeMarkerEngine());

        get("/ver", (request, response) -> {
            final Session sesion = getSession();
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            int id = Integer.parseInt(request.queryParams("id_user"));
            boolean profile = false;
            UsuarioEntity user = sesion.find(UsuarioEntity.class, id);
            Query query = (Query) em.createQuery("select distinct a.urlByIdUrl from AccesoEntity a where a.usuarioByIdUsuario = :user");
            query.setParameter("user", user);
            List<AccesoEntity> urls = query.getResultList();

            Query query1 = (Query) em.createQuery("select u from UrlEntity u where u.usuarioByIdUsuario = :user");
            query1.setParameter("user", user);
            List<UrlEntity> acortados = query1.getResultList();

            attributes.put("usuario",usuario);
            attributes.put("profile", profile);
            attributes.put("user", user);
            attributes.put("urls", urls);
            attributes.put("acortados", acortados);
            return new ModelAndView(attributes, "profile.ftl");
        }, new FreeMarkerEngine());

        get("/usuarios", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);

            if (usuario.administrador==false){
                response.redirect("/index");
            }
            Query query = (Query) em.createQuery("select u from UsuarioEntity u");
            List<UsuarioEntity> usuarios = query.getResultList();
            attributes.put("usuario",usuario);
            attributes.put("usuarios", usuarios);
            return new ModelAndView(attributes, "table.ftl");
        } , new FreeMarkerEngine());

        get("/urls", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            if (usuario.administrador==false){
                response.redirect("/index");
            }
            Query query = (Query) em.createQuery("select u from UrlEntity u");
            List<AccesoEntity> urls = query.getResultList();
            attributes.put("usuario",usuario);
            attributes.put("urls", urls);
            return new ModelAndView(attributes, "urls.ftl");
        } , new FreeMarkerEngine());

        post("/acortar", (request, response) -> {
            if (request.queryParams("url").equals("")) {
                response.redirect("/index");
            } else {
                spark.Session session=request.session(true);
                UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
                String url = request.queryParams("url");
                acortar(em, usuario, url);
            }
            response.redirect("/index");
            return "Usuario Creado";
        });

        get("/salir", (request, response)->{
            spark.Session session=request.session(true);
            session.invalidate();
            response.removeCookie("CookieUsuario");
            urlList.clear();
            response.redirect("/");
            return "Sesion finalizada";
        }); //Finaliza Sesión

        get("/delete", (request, response)-> {
            borrarUrl(request);
            response.redirect("/urls");
            return "Url Borrado";
        });

        get("/deleteuser", (request, response)-> {
            borrarUsuario(request);
            response.redirect("/usuarios");
            return "Url Borrado";
        });

        get("/update", (request, response)-> {
            makeAdmin(em, request);
            response.redirect("/usuarios");
            return "Usuario Actualizado";
        });

        get("/stats", (request, response)-> {
            final Session sesion = getSession();
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            if (usuario.id == 1){
                response.redirect("/index");
            }
            int id = Integer.parseInt(request.queryParams("id_url"));
            UrlEntity url = sesion.find(UrlEntity.class, id);
            Query query = (Query) em.createQuery("select a from AccesoEntity a where a.urlByIdUrl = :url order by fecha");
            query.setParameter("url", url);
            List<AccesoEntity> accesos = query.getResultList();

            //Object[] objects;
            Query query1 = (Query) em.createQuery("select a.usuarioByIdUsuario.username, count(*) as cant from AccesoEntity a where a.urlByIdUrl = :url group by a.usuarioByIdUsuario.username order by cant");
            query1.setParameter("url", url);
            List<Object> objects = query1.getResultList();
            ArrayList<Usuarios> usuarios = new ArrayList<>();
            for(Object o: objects) {
                Usuarios usuarios1 = new Usuarios();
                for(int i = 0; i < objects.size(); i++) {
                    if(i==0){
                        usuarios1.username=((Object[])o)[i].toString();
                    }
                    else{
                        usuarios1.cant = Integer.parseInt(((Object[])o)[i].toString());
                    }
                }
                usuarios.add(usuarios1);
            }
            Iterator it = accesos.iterator();

            HashMap graf_data=new HashMap();
            while(it.hasNext()) {
                AccesoEntity data=(AccesoEntity)it.next();
                String hora=new SimpleDateFormat("yyyy-MM-dd HH:").format(data.fecha)+":00";
                if(graf_data.containsKey(hora)){
                    graf_data.put(hora,(int)graf_data.get(hora)+1);
                }else{
                    graf_data.put(hora,1);
                }
            }

            attributes.put("accesos", accesos);
            attributes.put("url",url);
            attributes.put("usuario",usuario);
            attributes.put("usuarios", usuarios);
            attributes.put("graf_data",graf_data);

            return new ModelAndView(attributes, "blank.ftl");
        } , new FreeMarkerEngine());

        get("/deleteacceso", (request, response)-> {
            final Session sesion = getSession();
            int id = Integer.parseInt(request.queryParams("id_acceso"));
            AccesoEntity acceso = sesion.find(AccesoEntity.class, id);
            borrarAcceso(em, sesion, acceso);
            response.redirect("/stats?id_url="+acceso.urlByIdUrl.id);
            return "acceso Borrado";
        });

        path("/r", ()->{
            get("/:code", ((request, response) -> {
                String codigo = request.params("code");
                em.getTransaction().begin();
                Query<UrlEntity> query = (Query<UrlEntity>) em.createQuery("select u from UrlEntity u where u.code=:code", UrlEntity.class);
                query.setParameter("code", codigo);
                UrlEntity url = query.uniqueResult();
                if (url==null){
                    response.redirect("/404");
                    em.getTransaction().commit();
                    return "Url eliminada o no existe";
                } else {
                    spark.Session session=request.session(true);
                    UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
                    usuario = Askuser(usuario, session);
                    em.getTransaction().commit();
                    AccesoInsert(em, response, usuario, url, request, ip);
                    return "redirecionado";
                }
            }));
        });

        get("/404", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            attributes.put("usuario",usuario);
            return new ModelAndView(attributes, "404.ftl");
        } , new FreeMarkerEngine());

        get("*", (request, response) -> {
            response.redirect("/404");
            return "404!!";
        });

    }

    public static String renderContent(String htmlFile) throws IOException, URISyntaxException {
        URL url = Main.class.getResource(htmlFile);
        Path path = Paths.get(url.toURI());
        return new String(Files.readAllBytes(path), Charset.defaultCharset());
    }
}