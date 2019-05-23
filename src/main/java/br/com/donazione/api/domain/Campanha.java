package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Campanha.
 */
@Entity
@Table(name = "campanha")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Campanha implements Serializable {

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

    @Column(name = "slogan")
    private String slogan;

    @NotNull
    @Column(name = "data_hora_inicio", nullable = false)
    private Instant dataHoraInicio;

    @NotNull
    @Column(name = "data_hora_fim", nullable = false)
    private Instant dataHoraFim;

    @OneToOne(optional = false)    @NotNull

    @JoinColumn(unique = true)
    private Voluntario coordenador;

    @OneToMany(mappedBy = "campanha")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RecursoNecessario> recursoNecessarios = new HashSet<>();
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

    public Campanha nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Campanha descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSlogan() {
        return slogan;
    }

    public Campanha slogan(String slogan) {
        this.slogan = slogan;
        return this;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Instant getDataHoraInicio() {
        return dataHoraInicio;
    }

    public Campanha dataHoraInicio(Instant dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
        return this;
    }

    public void setDataHoraInicio(Instant dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Instant getDataHoraFim() {
        return dataHoraFim;
    }

    public Campanha dataHoraFim(Instant dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
        return this;
    }

    public void setDataHoraFim(Instant dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Voluntario getCoordenador() {
        return coordenador;
    }

    public Campanha coordenador(Voluntario voluntario) {
        this.coordenador = voluntario;
        return this;
    }

    public void setCoordenador(Voluntario voluntario) {
        this.coordenador = voluntario;
    }

    public Set<RecursoNecessario> getRecursoNecessarios() {
        return recursoNecessarios;
    }

    public Campanha recursoNecessarios(Set<RecursoNecessario> recursoNecessarios) {
        this.recursoNecessarios = recursoNecessarios;
        return this;
    }

    public Campanha addRecursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessarios.add(recursoNecessario);
        recursoNecessario.setCampanha(this);
        return this;
    }

    public Campanha removeRecursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessarios.remove(recursoNecessario);
        recursoNecessario.setCampanha(null);
        return this;
    }

    public void setRecursoNecessarios(Set<RecursoNecessario> recursoNecessarios) {
        this.recursoNecessarios = recursoNecessarios;
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
        Campanha campanha = (Campanha) o;
        if (campanha.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), campanha.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Campanha{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", slogan='" + getSlogan() + "'" +
            ", dataHoraInicio='" + getDataHoraInicio() + "'" +
            ", dataHoraFim='" + getDataHoraFim() + "'" +
            "}";
    }
}
