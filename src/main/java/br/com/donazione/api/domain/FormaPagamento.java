package br.com.donazione.api.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A FormaPagamento.
 */
@Entity
@Table(name = "forma_pagamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FormaPagamento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private Instant descricao;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Doacao doacao;

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

    public FormaPagamento nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Instant getDescricao() {
        return descricao;
    }

    public FormaPagamento descricao(Instant descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(Instant descricao) {
        this.descricao = descricao;
    }

    public Doacao getDoacao() {
        return doacao;
    }

    public FormaPagamento doacao(Doacao doacao) {
        this.doacao = doacao;
        return this;
    }

    public void setDoacao(Doacao doacao) {
        this.doacao = doacao;
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
        FormaPagamento formaPagamento = (FormaPagamento) o;
        if (formaPagamento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formaPagamento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormaPagamento{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
