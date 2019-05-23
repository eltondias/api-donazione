package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Participacao.
 */
@Entity
@Table(name = "participacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Participacao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora_emissao_certificado")
    private Instant dataHoraEmissaoCertificado;

    @Column(name = "carga_horaria")
    private Double cargaHoraria;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("participacaos")
    private Voluntario voluntario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("participacaos")
    private Acao acao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataHoraEmissaoCertificado() {
        return dataHoraEmissaoCertificado;
    }

    public Participacao dataHoraEmissaoCertificado(Instant dataHoraEmissaoCertificado) {
        this.dataHoraEmissaoCertificado = dataHoraEmissaoCertificado;
        return this;
    }

    public void setDataHoraEmissaoCertificado(Instant dataHoraEmissaoCertificado) {
        this.dataHoraEmissaoCertificado = dataHoraEmissaoCertificado;
    }

    public Double getCargaHoraria() {
        return cargaHoraria;
    }

    public Participacao cargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
        return this;
    }

    public void setCargaHoraria(Double cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public Participacao voluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
        return this;
    }

    public void setVoluntario(Voluntario voluntario) {
        this.voluntario = voluntario;
    }

    public Acao getAcao() {
        return acao;
    }

    public Participacao acao(Acao acao) {
        this.acao = acao;
        return this;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
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
        Participacao participacao = (Participacao) o;
        if (participacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), participacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Participacao{" +
            "id=" + getId() +
            ", dataHoraEmissaoCertificado='" + getDataHoraEmissaoCertificado() + "'" +
            ", cargaHoraria=" + getCargaHoraria() +
            "}";
    }
}
