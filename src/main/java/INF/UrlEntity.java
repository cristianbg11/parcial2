package INF;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "URL", schema = "PUBLIC", catalog = "ACORTADOR")
public class UrlEntity {
    public int id;
    public String code;
    public String url;
    public Integer cantidad;
    public Collection<AccesoEntity> accesosById;
    public UsuarioEntity usuarioByIdUsuario;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    public Timestamp fecha;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "CODE", nullable = true, length = 10)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "URL", nullable = true)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "CANTIDAD", nullable = true)
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        UrlEntity urlEntity = (UrlEntity) o;
        return id == urlEntity.id &&
                Objects.equals(cantidad, urlEntity.cantidad) &&
                Objects.equals(code, urlEntity.code) &&
                Objects.equals(url, urlEntity.url)&&
                Objects.equals(fecha, urlEntity.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, url, cantidad, fecha);
    }

    @OneToMany(mappedBy = "urlByIdUrl", cascade = CascadeType.ALL)
    public Collection<AccesoEntity> getAccesosById() {
        return accesosById;
    }

    public void setAccesosById(Collection<AccesoEntity> accesosById) {
        this.accesosById = accesosById;
    }

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    public UsuarioEntity getUsuarioByIdUsuario() {
        return usuarioByIdUsuario;
    }

    public void setUsuarioByIdUsuario(UsuarioEntity usuarioByIdUsuario) {
        this.usuarioByIdUsuario = usuarioByIdUsuario;
    }
}
