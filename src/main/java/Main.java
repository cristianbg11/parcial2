import INF.AccesoEntity;
import INF.UrlEntity;
import INF.UsuarioEntity;
import api.Rest;
import api.Soap;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.FreeMarker;
import spark.utils.IOUtils;

import javax.persistence.EntityManager;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;

import static services.Inicio.*;
import static spark.Spark.*;

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
        File uploadDir = new File("src/main/resources/publico/assets/img/avatars");
        uploadDir.mkdir(); // create the upload directory if it doesn't exist

        if (secion.find(UsuarioEntity.class, 1)==null){
            IniciarUsuario(em);
        }

        new Rest();
        new FreeMarker();
        get("/host", (request, response) -> {
            return request.host();
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

        get("/deleteacceso", (request, response)-> {
            final Session sesion = getSession();
            int id = Integer.parseInt(request.queryParams("id_acceso"));
            AccesoEntity acceso = sesion.find(AccesoEntity.class, id);
            borrarAcceso(em, sesion, acceso);
            response.redirect("/stats?id_url="+acceso.urlByIdUrl.id);
            return "acceso Borrado";
        });

        post("/upload", (request, response) -> {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(uploadDir.getPath()));
            Part filePart = request.raw().getPart("user_logo");

            try (InputStream inputStream = filePart.getInputStream()) {
                OutputStream outputStream = new FileOutputStream(uploadDir.getPath() + "/" + filePart.getSubmittedFileName());
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            }
            int usuarioId = Integer.parseInt(request.queryParams("usuario_id"));
            final Session sesion = getSession();
            UsuarioEntity usuario = sesion.find(UsuarioEntity.class, usuarioId);
            em.getTransaction().begin();
            usuario.setPerfil("assets/img/avatars/" + filePart.getSubmittedFileName());
            em.merge(usuario);
            em.getTransaction().commit();

            response.redirect("/perfil");
            return "subido";
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

        get("*", (request, response) -> {
            response.redirect("/404");
            return "404!!";
        });

    }
}