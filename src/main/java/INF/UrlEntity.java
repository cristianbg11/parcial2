package INF;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "URL", schema = "PUBLIC", catalog = "ACORTADOR")
public class UrlEntity {
    public int id;
    public String code;
    public String url;
    public Integer cantidad;
    public Collection<UrlUsuarioEntity> urlUsuariosById;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlEntity urlEntity = (UrlEntity) o;
        return id == urlEntity.id &&
                Objects.equals(cantidad, urlEntity.cantidad) &&
                Objects.equals(code, urlEntity.code) &&
                Objects.equals(url, urlEntity.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, url, cantidad);
    }

    @OneToMany(mappedBy = "urlByIdUrl", cascade = CascadeType.ALL)
    public Collection<UrlUsuarioEntity> getUrlUsuariosById() {
        return urlUsuariosById;
    }

    public void setUrlUsuariosById(Collection<UrlUsuarioEntity> urlUsuariosById) {
        this.urlUsuariosById = urlUsuariosById;
    }
}
