package INF;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "URL", schema = "PUBLIC", catalog = "ACORTADOR")
@XmlAccessorType(XmlAccessType.FIELD)
public class UrlEntity implements Serializable {
    private static final long serialVersionUID = -5577579081118070434L;
    public int id;
    public String code;
    public String url;
    public Integer cantidad;
    public List<AccesoEntity> accesosById;
    public UsuarioEntity usuarioByIdUsuario;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    public Timestamp fecha;
    public String preview;

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

    @Basic
    @Column(name = "PREVIEW", nullable = true)
    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
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
                Objects.equals(fecha, urlEntity.fecha)&&
                Objects.equals(preview,urlEntity.preview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, url, cantidad, fecha, preview);
    }

    @OneToMany(mappedBy = "urlByIdUrl", cascade = CascadeType.ALL)
    public List<AccesoEntity> getAccesosById() {
        return accesosById;
    }

    public void setAccesosById(List<AccesoEntity> accesosById) {
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
