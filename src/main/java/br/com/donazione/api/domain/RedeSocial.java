package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import br.com.donazione.api.domain.enumeration.TipoRedeSocial;

/**
 * A RedeSocial.
 */
@Entity
@Table(name = "rede_social")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RedeSocial implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rede_social")
    private TipoRedeSocial tipoRedeSocial;

    @Column(name = "url")
    private String url;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("redeSocials")
    private Voluntario voluntario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRedeSocial getTipoRedeSocial() {
        return tipoRedeSocial;
    }

    public RedeSocial tipoRedeSocial(TipoRedeSocial tipoRedeSocial) {
        this.tipoRedeSocial = tipoRedeSocial;
        return this;
    }

    public void setTipoRedeSocial(TipoRedeSocial tipoRedeSocial) {
        this.tipoRedeSocial = tipoRedeSocial;
    }

    public String getUrl() {
        return url;
    }

    public RedeSocial url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public RedeSocial voluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
        return this;
    }

    public void setVoluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedeSocial redeSocial = (RedeSocial) o;
        if (redeSocial.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), redeSocial.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RedeSocial{" +
            "id=" + getId() +
            ", tipoRedeSocial='" + getTipoRedeSocial() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
