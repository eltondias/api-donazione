package br.com.donazione.api.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import br.com.donazione.api.domain.enumeration.DiaSemanaEnum;

import br.com.donazione.api.domain.enumeration.TurnoEnum;

/**
 * A Disponibilidade.
 */
@Entity
@Table(name = "disponibilidade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Disponibilidade implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private Instant horaInicio;

    @NotNull
    @Column(name = "hora_fim", nullable = false)
    private Instant horaFim;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemanaEnum diaSemana;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false)
    private TurnoEnum turno;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("disponibilidades")
    private Voluntario voluntario;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHoraInicio() {
        return horaInicio;
    }

    public Disponibilidade horaInicio(Instant horaInicio) {
        this.horaInicio = horaInicio;
        return this;
    }

    public void setHoraInicio(Instant horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Instant getHoraFim() {
        return horaFim;
    }

    public Disponibilidade horaFim(Instant horaFim) {
        this.horaFim = horaFim;
        return this;
    }

    public void setHoraFim(Instant horaFim) {
        this.horaFim = horaFim;
    }

    public DiaSemanaEnum getDiaSemana() {
        return diaSemana;
    }

    public Disponibilidade diaSemana(DiaSemanaEnum diaSemana) {
        this.diaSemana = diaSemana;
        return this;
    }

    public void setDiaSemana(DiaSemanaEnum diaSemana) {
        this.diaSemana = diaSemana;
    }

    public TurnoEnum getTurno() {
        return turno;
    }

    public Disponibilidade turno(TurnoEnum turno) {
        this.turno = turno;
        return this;
    }

    public void setTurno(TurnoEnum turno) {
        this.turno = turno;
    }

    public Voluntario getVoluntario() {
        return voluntario;
    }

    public Disponibilidade voluntario(Voluntario voluntario) {
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
        Disponibilidade disponibilidade = (Disponibilidade) o;
        if (disponibilidade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), disponibilidade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Disponibilidade{" +
            "id=" + getId() +
            ", horaInicio='" + getHoraInicio() + "'" +
            ", horaFim='" + getHoraFim() + "'" +
            ", diaSemana='" + getDiaSemana() + "'" +
            ", turno='" + getTurno() + "'" +
            "}";
    }
}
