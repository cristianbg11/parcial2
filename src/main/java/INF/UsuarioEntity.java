package INF;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USUARIO", schema = "PUBLIC", catalog = "ACORTADOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsuarioEntity {
    public int id;
    public String nombre;
    public String username;
    public String password;
    public Boolean administrador;
    public String email;
    public Integer edad;
    public String longitud;
    public String latitud;
    public String perfil;
    public List<ComentarioEntity> comentariosById;
    public List<AccesoEntity> accesosById;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NOMBRE", nullable = true, length = 100)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Basic
    @Column(name = "USERNAME", nullable = true, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "ADMINISTRADOR", nullable = true)
    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    @Basic
    @Column(name = "EMAIL", nullable = true, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "EDAD", nullable = true)
    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    @Basic
    @Column(name = "LONGITUD", nullable = true, length = 100)
    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Basic
    @Column(name = "LATITUD", nullable = true, length = 100)
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }



    @Basic
    @Column(name = "PERFIL", nullable = true)
    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return id == that.id &&
                Objects.equals(edad, that.edad) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(administrador, that.administrador) &&
                Objects.equals(email, that.email) &&
                Objects.equals(longitud, that.longitud) &&
                Objects.equals(latitud, that.latitud) &&
                Objects.equals(perfil, that.perfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, username, password, administrador, email, edad, longitud, latitud, perfil);
    }

    @OneToMany(mappedBy = "usuarioByIdUsuario", cascade = CascadeType.ALL)
    public List<ComentarioEntity> getComentariosById() {
        return comentariosById;
    }

    public void setComentariosById(List<ComentarioEntity> comentariosById) {
        this.comentariosById = comentariosById;
    }

    @OneToMany(mappedBy = "usuarioByIdUsuario", cascade = CascadeType.ALL)
    public List<AccesoEntity> getAccesosById() {
        return accesosById;
    }

    public void setAccesosById(List<AccesoEntity> accesosById) {
        this.accesosById = accesosById;
    }
}
