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

import br.com.donazione.api.domain.enumeration.EstadoVoluntarioEnum;

/**
 * A Voluntario.
 */
@Entity
@Table(name = "voluntario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Voluntario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "url_foto_perfil")
    private String urlFotoPerfil;

    @NotNull
    @Column(name = "cpf", nullable = false)
    private String cpf;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "senha", nullable = false)
    private String senha;

    @NotNull
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Column(name = "data_cadastro")
    private Instant dataCadastro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private EstadoVoluntarioEnum situacao;

    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Disponibilidade> disponibilidades = new HashSet<>();
    @OneToMany(mappedBy = "doador")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Doacao> doacaos = new HashSet<>();
    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Participacao> participacaos = new HashSet<>();
    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Endereco> enderecos = new HashSet<>();
    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Telefone> telefones = new HashSet<>();
    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Email> emails = new HashSet<>();
    @OneToMany(mappedBy = "voluntario")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RedeSocial> redeSocials = new HashSet<>();
    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "voluntario_habilidade",
               joinColumns = @JoinColumn(name = "voluntario_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "habilidade_id", referencedColumnName = "id"))
    private Set<Habilidade> habilidades = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "voluntario_profissao",
               joinColumns = @JoinColumn(name = "voluntario_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "profissao_id", referencedColumnName = "id"))
    private Set<Profissao> profissaos = new HashSet<>();

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

    public Voluntario nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrlFotoPerfil() {
        return urlFotoPerfil;
    }

    public Voluntario urlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
        return this;
    }

    public void setUrlFotoPerfil(String urlFotoPerfil) {
        this.urlFotoPerfil = urlFotoPerfil;
    }

    public String getCpf() {
        return cpf;
    }

    public Voluntario cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public Voluntario login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public Voluntario senha(String senha) {
        this.senha = senha;
        return this;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean isIsAdmin() {
        return isAdmin;
    }

    public Voluntario isAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
        return this;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Instant getDataNascimento() {
        return dataNascimento;
    }

    public Voluntario dataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Instant getDataCadastro() {
        return dataCadastro;
    }

    public Voluntario dataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
        return this;
    }

    public void setDataCadastro(Instant dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public EstadoVoluntarioEnum getSituacao() {
        return situacao;
    }

    public Voluntario situacao(EstadoVoluntarioEnum situacao) {
        this.situacao = situacao;
        return this;
    }

    public void setSituacao(EstadoVoluntarioEnum situacao) {
        this.situacao = situacao;
    }

    public Set<Disponibilidade> getDisponibilidades() {
        return disponibilidades;
    }

    public Voluntario disponibilidades(Set<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
        return this;
    }

    public Voluntario addDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidades.add(disponibilidade);
        disponibilidade.setVoluntario(this);
        return this;
    }

    public Voluntario removeDisponibilidade(Disponibilidade disponibilidade) {
        this.disponibilidades.remove(disponibilidade);
        disponibilidade.setVoluntario(null);
        return this;
    }

    public void setDisponibilidades(Set<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public Set<Doacao> getDoacaos() {
        return doacaos;
    }

    public Voluntario doacaos(Set<Doacao> doacaos) {
        this.doacaos = doacaos;
        return this;
    }

    public Voluntario addDoacao(Doacao doacao) {
        this.doacaos.add(doacao);
        doacao.setDoador(this);
        return this;
    }

    public Voluntario removeDoacao(Doacao doacao) {
        this.doacaos.remove(doacao);
        doacao.setDoador(null);
        return this;
    }

    public void setDoacaos(Set<Doacao> doacaos) {
        this.doacaos = doacaos;
    }

    public Set<Participacao> getParticipacaos() {
        return participacaos;
    }

    public Voluntario participacaos(Set<Participacao> participacaos) {
        this.participacaos = participacaos;
        return this;
    }

    public Voluntario addParticipacao(Participacao participacao) {
        this.participacaos.add(participacao);
        participacao.setVoluntario(this);
        return this;
    }

    public Voluntario removeParticipacao(Participacao participacao) {
        this.participacaos.remove(participacao);
        participacao.setVoluntario(null);
        return this;
    }

    public void setParticipacaos(Set<Participacao> participacaos) {
        this.participacaos = participacaos;
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public Voluntario enderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
        return this;
    }

    public Voluntario addEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
        endereco.setVoluntario(this);
        return this;
    }

    public Voluntario removeEndereco(Endereco endereco) {
        this.enderecos.remove(endereco);
        endereco.setVoluntario(null);
        return this;
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<Telefone> getTelefones() {
        return telefones;
    }

    public Voluntario telefones(Set<Telefone> telefones) {
        this.telefones = telefones;
        return this;
    }

    public Voluntario addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
        telefone.setVoluntario(this);
        return this;
    }

    public Voluntario removeTelefone(Telefone telefone) {
        this.telefones.remove(telefone);
        telefone.setVoluntario(null);
        return this;
    }

    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    public Set<Email> getEmails() {
        return emails;
    }

    public Voluntario emails(Set<Email> emails) {
        this.emails = emails;
        return this;
    }

    public Voluntario addEmail(Email email) {
        this.emails.add(email);
        email.setVoluntario(this);
        return this;
    }

    public Voluntario removeEmail(Email email) {
        this.emails.remove(email);
        email.setVoluntario(null);
        return this;
    }

    public void setEmails(Set<Email> emails) {
        this.emails = emails;
    }

    public Set<RedeSocial> getRedeSocials() {
        return redeSocials;
    }

    public Voluntario redeSocials(Set<RedeSocial> redeSocials) {
        this.redeSocials = redeSocials;
        return this;
    }

    public Voluntario addRedeSocial(RedeSocial redeSocial) {
        this.redeSocials.add(redeSocial);
        redeSocial.setVoluntario(this);
        return this;
    }

    public Voluntario removeRedeSocial(RedeSocial redeSocial) {
        this.redeSocials.remove(redeSocial);
        redeSocial.setVoluntario(null);
        return this;
    }

    public void setRedeSocials(Set<RedeSocial> redeSocials) {
        this.redeSocials = redeSocials;
    }

    public Set<Habilidade> getHabilidades() {
        return habilidades;
    }

    public Voluntario habilidades(Set<Habilidade> habilidades) {
        this.habilidades = habilidades;
        return this;
    }

    public Voluntario addHabilidade(Habilidade habilidade) {
        this.habilidades.add(habilidade);
        habilidade.getVoluntarios().add(this);
        return this;
    }

    public Voluntario removeHabilidade(Habilidade habilidade) {
        this.habilidades.remove(habilidade);
        habilidade.getVoluntarios().remove(this);
        return this;
    }

    public void setHabilidades(Set<Habilidade> habilidades) {
        this.habilidades = habilidades;
    }

    public Set<Profissao> getProfissaos() {
        return profissaos;
    }

    public Voluntario profissaos(Set<Profissao> profissaos) {
        this.profissaos = profissaos;
        return this;
    }

    public Voluntario addProfissao(Profissao profissao) {
        this.profissaos.add(profissao);
        profissao.getVoluntarios().add(this);
        return this;
    }

    public Voluntario removeProfissao(Profissao profissao) {
        this.profissaos.remove(profissao);
        profissao.getVoluntarios().remove(this);
        return this;
    }

    public void setProfissaos(Set<Profissao> profissaos) {
        this.profissaos = profissaos;
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
        Voluntario voluntario = (Voluntario) o;
        if (voluntario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), voluntario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Voluntario{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", urlFotoPerfil='" + getUrlFotoPerfil() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", login='" + getLogin() + "'" +
            ", senha='" + getSenha() + "'" +
            ", isAdmin='" + isIsAdmin() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", situacao='" + getSituacao() + "'" +
            "}";
    }
}
