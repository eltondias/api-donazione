package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ProfissaoNecessariaAcao.
 */
@Entity
@Table(name = "profissao_necessaria_acao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfissaoNecessariaAcao implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("profissaoNecessariaAcaos")
    private Profissao profissao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("profissaoNecessariaAcaos")
    private Acao acao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public ProfissaoNecessariaAcao quantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
        return this;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public ProfissaoNecessariaAcao profissao(Profissao profissao) {
        this.profissao = profissao;
        return this;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public Acao getAcao() {
        return acao;
    }

    public ProfissaoNecessariaAcao acao(Acao acao) {
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
        ProfissaoNecessariaAcao profissaoNecessariaAcao = (ProfissaoNecessariaAcao) o;
        if (profissaoNecessariaAcao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profissaoNecessariaAcao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfissaoNecessariaAcao{" +
            "id=" + getId() +
            ", quantidadeMinima=" + getQuantidadeMinima() +
            "}";
    }
}
