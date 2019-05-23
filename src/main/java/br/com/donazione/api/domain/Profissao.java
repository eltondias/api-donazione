package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Profissao.
 */
@Entity
@Table(name = "profissao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Profissao implements Serializable {

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

    @OneToMany(mappedBy = "profissao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos = new HashSet<>();
    @ManyToMany(mappedBy = "profissaos")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Voluntario> voluntarios = new HashSet<>();

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

    public Profissao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Profissao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<ProfissaoNecessariaAcao> getProfissaoNecessariaAcaos() {
        return profissaoNecessariaAcaos;
    }

    public Profissao profissaoNecessariaAcaos(Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos) {
        this.profissaoNecessariaAcaos = profissaoNecessariaAcaos;
        return this;
    }

    public Profissao addProfissaoNecessariaAcao(ProfissaoNecessariaAcao profissaoNecessariaAcao) {
        this.profissaoNecessariaAcaos.add(profissaoNecessariaAcao);
        profissaoNecessariaAcao.setProfissao(this);
        return this;
    }

    public Profissao removeProfissaoNecessariaAcao(ProfissaoNecessariaAcao profissaoNecessariaAcao) {
        this.profissaoNecessariaAcaos.remove(profissaoNecessariaAcao);
        profissaoNecessariaAcao.setProfissao(null);
        return this;
    }

    public void setProfissaoNecessariaAcaos(Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos) {
        this.profissaoNecessariaAcaos = profissaoNecessariaAcaos;
    }

    public Set<Voluntario> getVoluntarios() {
        return voluntarios;
    }

    public Profissao voluntarios(Set<Voluntario> voluntarios) {
        this.voluntarios = voluntarios;
        return this;
    }

    public Profissao addVoluntario(Voluntario voluntario) {
        this.voluntarios.add(voluntario);
        voluntario.getProfissaos().add(this);
        return this;
    }

    public Profissao removeVoluntario(Voluntario voluntario) {
        this.voluntarios.remove(voluntario);
        voluntario.getProfissaos().remove(this);
        return this;
    }

    public void setVoluntarios(Set<Voluntario> voluntarios) {
        this.voluntarios = voluntarios;
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
        Profissao profissao = (Profissao) o;
        if (profissao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profissao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Profissao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
