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

import br.com.donazione.api.domain.enumeration.SituacaoAcaoEnum;

/**
 * A Acao.
 */
@Entity
@Table(name = "acao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Acao implements Serializable {

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

    @Column(name = "meta")
    private String meta;

    @Column(name = "banner")
    private String banner;

    @NotNull
    @Column(name = "data_hora_inicio", nullable = false)
    private Instant dataHoraInicio;

    @NotNull
    @Column(name = "data_hora_fim", nullable = false)
    private Instant dataHoraFim;

    @Column(name = "custos")
    private Double custos;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_acao", nullable = false)
    private SituacaoAcaoEnum situacaoAcao;

    @OneToMany(mappedBy = "acao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos = new HashSet<>();
    @OneToMany(mappedBy = "acao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Participacao> participacaos = new HashSet<>();
    @OneToMany(mappedBy = "acao")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Endereco> enderecos = new HashSet<>();
    @OneToMany(mappedBy = "acao")
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

    public Acao nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Acao descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMeta() {
        return meta;
    }

    public Acao meta(String meta) {
        this.meta = meta;
        return this;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getBanner() {
        return banner;
    }

    public Acao banner(String banner) {
        this.banner = banner;
        return this;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public Instant getDataHoraInicio() {
        return dataHoraInicio;
    }

    public Acao dataHoraInicio(Instant dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
        return this;
    }

    public void setDataHoraInicio(Instant dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public Instant getDataHoraFim() {
        return dataHoraFim;
    }

    public Acao dataHoraFim(Instant dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
        return this;
    }

    public void setDataHoraFim(Instant dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Double getCustos() {
        return custos;
    }

    public Acao custos(Double custos) {
        this.custos = custos;
        return this;
    }

    public void setCustos(Double custos) {
        this.custos = custos;
    }

    public SituacaoAcaoEnum getSituacaoAcao() {
        return situacaoAcao;
    }

    public Acao situacaoAcao(SituacaoAcaoEnum situacaoAcao) {
        this.situacaoAcao = situacaoAcao;
        return this;
    }

    public void setSituacaoAcao(SituacaoAcaoEnum situacaoAcao) {
        this.situacaoAcao = situacaoAcao;
    }

    public Set<ProfissaoNecessariaAcao> getProfissaoNecessariaAcaos() {
        return profissaoNecessariaAcaos;
    }

    public Acao profissaoNecessariaAcaos(Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos) {
        this.profissaoNecessariaAcaos = profissaoNecessariaAcaos;
        return this;
    }

    public Acao addProfissaoNecessariaAcao(ProfissaoNecessariaAcao profissaoNecessariaAcao) {
        this.profissaoNecessariaAcaos.add(profissaoNecessariaAcao);
        profissaoNecessariaAcao.setAcao(this);
        return this;
    }

    public Acao removeProfissaoNecessariaAcao(ProfissaoNecessariaAcao profissaoNecessariaAcao) {
        this.profissaoNecessariaAcaos.remove(profissaoNecessariaAcao);
        profissaoNecessariaAcao.setAcao(null);
        return this;
    }

    public void setProfissaoNecessariaAcaos(Set<ProfissaoNecessariaAcao> profissaoNecessariaAcaos) {
        this.profissaoNecessariaAcaos = profissaoNecessariaAcaos;
    }

    public Set<Participacao> getParticipacaos() {
        return participacaos;
    }

    public Acao participacaos(Set<Participacao> participacaos) {
        this.participacaos = participacaos;
        return this;
    }

    public Acao addParticipacao(Participacao participacao) {
        this.participacaos.add(participacao);
        participacao.setAcao(this);
        return this;
    }

    public Acao removeParticipacao(Participacao participacao) {
        this.participacaos.remove(participacao);
        participacao.setAcao(null);
        return this;
    }

    public void setParticipacaos(Set<Participacao> participacaos) {
        this.participacaos = participacaos;
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public Acao enderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
        return this;
    }

    public Acao addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setAcao(this);
        return this;
    }

    public Acao removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setAcao(null);
        return this;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<RecursoNecessario> getRecursoNecessarios() {
        return recursoNecessarios;
    }

    public Acao recursoNecessarios(Set<RecursoNecessario> recursoNecessarios) {
        this.recursoNecessarios = recursoNecessarios;
        return this;
    }

    public Acao addRecursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessarios.add(recursoNecessario);
        recursoNecessario.setAcao(this);
        return this;
    }

    public Acao removeRecursoNecessario(RecursoNecessario recursoNecessario) {
        this.recursoNecessarios.remove(recursoNecessario);
        recursoNecessario.setAcao(null);
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
        Acao acao = (Acao) o;
        if (acao.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), acao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Acao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", meta='" + getMeta() + "'" +
            ", banner='" + getBanner() + "'" +
            ", dataHoraInicio='" + getDataHoraInicio() + "'" +
            ", dataHoraFim='" + getDataHoraFim() + "'" +
            ", custos=" + getCustos() +
            ", situacaoAcao='" + getSituacaoAcao() + "'" +
            "}";
    }
}
