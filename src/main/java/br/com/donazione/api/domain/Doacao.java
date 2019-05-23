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
 * A Doacao.
 */
@Entity
@Table(name = "doacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Doacao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    @NotNull
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @NotNull
    @Column(name = "is_financeiro", nullable = false)
    private Boolean isFinanceiro;

    @NotNull
    @Column(name = "is_anomina", nullable = false)
    private Boolean isAnomina;

    @NotNull
    @Column(name = "data_hora", nullable = false)
    private Instant dataHora;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("doacaos")
    private Voluntario doador;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("doacaos")
    private RecursoNecessario recursoNecessario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public Doacao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Doacao quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean isIsFinanceiro() {
        return isFinanceiro;
    }

    public Doacao isFinanceiro(Boolean isFinanceiro) {
        this.isFinanceiro = isFinanceiro;
        return this;
    }

    public void setIsFinanceiro(Boolean isFinanceiro) {
        this.isFinanceiro = isFinanceiro;
    }

    public Boolean isIsAnomina() {
        return isAnomina;
    }

    public Doacao isAnomina(Boolean isAnomina) {
        this.isAnomina = isAnomina;
        return this;
    }

    public void setIsAnomina(Boolean isAnomina) {
        this.isAnomina = isAnomina;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public Doacao dataHora(Instant dataHora) {
        this.dataHora = dataHora;
        return this;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public Voluntario getDoador() {
        return doador;
    }

    public Doacao doador(Voluntario voluntario) {
        this.doador = voluntario;
        return this;
    }

    public void setDoador(Voluntario voluntario) {
        this.doador = voluntario;
    }

    public RecursoNecessario getRecursoNecessario() {
        return recursoNecessario;
    }

    public Doacao recursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessario = recursoNecessario;
        return this;
    }

    public void setRecursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessario = recursoNecessario;
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
        Doacao doacao = (Doacao) o;
        if (doacao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), doacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Doacao{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", quantidade=" + getQuantidade() +
            ", isFinanceiro='" + isIsFinanceiro() + "'" +
            ", isAnomina='" + isIsAnomina() + "'" +
            ", dataHora='" + getDataHora() + "'" +
            "}";
    }
}
