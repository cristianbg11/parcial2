package INF;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ACCESO", schema = "PUBLIC", catalog = "ACORTADOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccesoEntity {
    public int id;
    public Timestamp fecha;
    public UsuarioEntity usuarioByIdUsuario;
    public UrlEntity urlByIdUrl;
    public String navegador;
    public String ip;
    public String sistema;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FECHA", nullable = true)
    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    @Basic
    @Column(name = "NAVEGADOR", nullable = true, length = 255)
    public String getNavegador() {
        return navegador;
    }

    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }

    @Basic
    @Column(name = "IP", nullable = true, length = 50)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Basic
    @Column(name = "SISTEMA", nullable = true, length = 255)
    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccesoEntity that = (AccesoEntity) o;
        return id == that.id &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(navegador, that.navegador) &&
                Objects.equals(ip, that.ip) &&
                Objects.equals(sistema, that.sistema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha, navegador, ip, sistema);
    }

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    public UsuarioEntity getUsuarioByIdUsuario() {
        return usuarioByIdUsuario;
    }

    public void setUsuarioByIdUsuario(UsuarioEntity usuarioByIdUsuario) {
        this.usuarioByIdUsuario = usuarioByIdUsuario;
    }

    @ManyToOne
    @JoinColumn(name = "ID_URL", referencedColumnName = "ID")
    public UrlEntity getUrlByIdUrl() {
        return urlByIdUrl;
    }

    public void setUrlByIdUrl(UrlEntity urlByIdUrl) {
        this.urlByIdUrl = urlByIdUrl;
    }
}
