package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A RecursoNecessario.
 */
@Entity
@Table(name = "recurso_necessario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RecursoNecessario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

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
    @Column(name = "valor", nullable = false)
    private Double valor;

    @OneToMany(mappedBy = "recursoNecessario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Doacao> doacaos = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("recursoNecessarios")
    private Acao acao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("recursoNecessarios")
    private Campanha campanha;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public RecursoNecessario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public RecursoNecessario descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public RecursoNecessario quantidade(Integer quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Boolean isIsFinanceiro() {
        return isFinanceiro;
    }

    public RecursoNecessario isFinanceiro(Boolean isFinanceiro) {
        this.isFinanceiro = isFinanceiro;
        return this;
    }

    public void setIsFinanceiro(Boolean isFinanceiro) {
        this.isFinanceiro = isFinanceiro;
    }

    public Double getValor() {
        return valor;
    }

    public RecursoNecessario valor(Double valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Set<Doacao> getDoacaos() {
        return doacaos;
    }

    public RecursoNecessario doacaos(Set<Doacao> doacaos) {
        this.doacaos = doacaos;
        return this;
    }

    public RecursoNecessario addDoacao(Doacao doacao) {
        this.doacaos.add(doacao);
        doacao.setRecursoNecessario(this);
        return this;
    }

    public RecursoNecessario removeDoacao(Doacao doacao) {
        this.doacaos.remove(doacao);
        doacao.setRecursoNecessario(null);
        return this;
    }

    public void setDoacaos(Set<Doacao> doacaos) {
        this.doacaos = doacaos;
    }

    public Acao getAcao() {
        return acao;
    }

    public RecursoNecessario acao(Acao acao) {
        this.acao = acao;
        return this;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public RecursoNecessario campanha(Campanha campanha) {
        this.campanha = campanha;
        return this;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
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
        RecursoNecessario recursoNecessario = (RecursoNecessario) o;
        if (recursoNecessario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recursoNecessario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RecursoNecessario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", quantidade=" + getQuantidade() +
            ", isFinanceiro='" + isIsFinanceiro() + "'" +
            ", valor=" + getValor() +
            "}";
    }
}
