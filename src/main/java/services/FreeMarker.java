package services;

import INF.AccesoEntity;
import INF.UrlEntity;
import INF.UsuarioEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import utilities.UserUrl;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.*;

import static services.Inicio.*;
import static services.Inicio.Askuser;
import static spark.Spark.get;

public class FreeMarker {
    public FreeMarker(){
        EntityManager em = getSession();
        get("/", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            final Session sesion = getSession();
            if (request.cookie("CookieUsuario") != null){
                int id = Integer.parseInt((request.cookie("CookieUsuario")));
                UsuarioEntity usuarioEntity = sesion.find(UsuarioEntity.class, id);
                spark.Session session=request.session(true);
                session.attribute("usuario", usuarioEntity);
                response.redirect("/index");
            }
            String ok = "ok";
            attributes.put("ok", ok);
            return new ModelAndView(attributes,"login.ftl");
        } , new FreeMarkerEngine());

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
            ArrayList<UserUrl> usuarios = new ArrayList<>();
            for(Object o: objects) {
                UserUrl userUrl1 = new UserUrl();
                for(int i = 0; i < objects.size(); i++) {
                    if(i==0){
                        userUrl1.username=((Object[])o)[i].toString();
                    }
                    else{
                        userUrl1.cant = Integer.parseInt(((Object[])o)[i].toString());
                    }
                }
                usuarios.add(userUrl1);
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

        get("/404", (request, response)-> {
            Map<String, Object> attributes = new HashMap<>();
            spark.Session session=request.session(true);
            UsuarioEntity usuario = (UsuarioEntity)(session.attribute("usuario"));
            usuario = Askuser(usuario, session);
            attributes.put("usuario",usuario);
            return new ModelAndView(attributes, "404.ftl");
        } , new FreeMarkerEngine());
    }
}
