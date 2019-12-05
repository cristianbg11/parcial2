package INF;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ACCESO", schema = "PUBLIC", catalog = "ACORTADOR")
public class AccesoEntity {
    public int id;
    public Timestamp fecha;
    public UsuarioEntity usuarioByIdUsuario;
    public UrlEntity urlByIdUrl;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccesoEntity that = (AccesoEntity) o;
        return id == that.id &&
                Objects.equals(fecha, that.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fecha);
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
