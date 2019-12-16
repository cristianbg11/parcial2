package api;

import INF.UsuarioEntity;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;
import services.JWebToken;
import services.UrlService;
import utilities.UserUrl;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.h2.util.StringUtils.isNullOrEmpty;
import static services.Inicio.acortar;
import static services.Inicio.getSession;
import static spark.Spark.*;
import static utilities.JsonUtil.json;

public class Rest {

    public Rest(){
        EntityManager em = getSession();
        UrlService urlService = UrlService.getInstance();
        path("/rest", ()->{

            after("/*", (req, res) -> {
                res.type("application/json");
            });
            get("/", (request, response) -> "RUTA API REST");

            path("/login", () -> {
                post("/", "application/json", (request, response) -> {

                    UsuarioEntity usuarioEntity = new Gson().fromJson(request.body(), UsuarioEntity.class);
                    List<UsuarioEntity> users = em.createQuery("select u from UsuarioEntity u", UsuarioEntity.class).getResultList();
                    for(UsuarioEntity usuario : users){
                        if (usuario.username.equals(usuarioEntity.username) && usuario.password.equals(usuarioEntity.password)){
                            JSONObject payload = new JSONObject();
                            JSONArray auds = new JSONArray();
                            JSONObject aud = new JSONObject();
                            aud.put("role", "Users");
                            auds.put(aud);
                            payload.put("sub", usuarioEntity.username);
                            payload.put("aud", auds);

                            payload.put("exp", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)+60);
                            String bearerToken =  new JWebToken(payload).toString();
                            return bearerToken;
                        }
                    }
                    return "Usuario y contrasena incorrectos";
                });

            });
            path("/links", () -> {

                get("", (request, response) ->{

                    if (isNullOrEmpty(request.headers("token"))){
                        response.redirect("/404");
                        return "fallo";
                    }
                    JWebToken incomingToken = new JWebToken(request.headers("token"));

                    if (incomingToken.isValid()) {
                        return urlService.getAllLinks();
                    }
                    else{
                        return "Token expirado o incorrecto";
                    }
                }, json());
                post("/user/", "application/json", (request, response) -> {

                    if (isNullOrEmpty(request.headers("token"))){
                        response.redirect("/404");
                        return "fallo";
                    }
                    JWebToken incomingToken = new JWebToken(request.headers("token"));

                    if (incomingToken.isValid()) {
                        UsuarioEntity usuarioEntity = new Gson().fromJson(request.body(), UsuarioEntity.class);
                        final Session sesion = getSession();
                        UsuarioEntity persona = sesion.find(UsuarioEntity.class, usuarioEntity.id);
                        return urlService.getLinks(persona);
                    } else{
                        return "Token expirado o incorrecto";
                    }
                }, json());

            });

            path("/url", () -> {
                post("/crear/", "application/json", (request, response) -> {
                    if (isNullOrEmpty(request.headers("token"))){
                        response.redirect("/404");
                        return "fallo";
                    }
                    JWebToken incomingToken = new JWebToken(request.headers("token"));

                    if (incomingToken.isValid()) {
                        final Session sesion = getSession();
                        UserUrl userUrl = new Gson().fromJson(request.body(), UserUrl.class);
                        UsuarioEntity usuarioEntity = sesion.find(UsuarioEntity.class, userUrl.id);
                        if (usuarioEntity == null){
                            usuarioEntity = sesion.find(UsuarioEntity.class, 1);
                        }
                        acortar(em, usuarioEntity, userUrl.url);
                        return urlService.getAllLinks().get(urlService.getAllLinks().size() - 1);
                    } else{
                        return "Token expirado o incorrecto";
                    }
                }, json());
            });
        });
    }

}
