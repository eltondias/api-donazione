package br.com.donazione.api.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(br.com.donazione.api.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(br.com.donazione.api.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Estado.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Estado.class.getName() + ".cidades", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Cidade.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Cidade.class.getName() + ".enderecos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Endereco.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".disponibilidades", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".doacaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".participacaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".enderecos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".telefones", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".emails", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".redeSocials", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".habilidades", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Voluntario.class.getName() + ".profissaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Telefone.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Email.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.RedeSocial.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Habilidade.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Habilidade.class.getName() + ".voluntarios", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Profissao.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Profissao.class.getName() + ".profissaoNecessariaAcaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Profissao.class.getName() + ".voluntarios", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Disponibilidade.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Campanha.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Campanha.class.getName() + ".recursoNecessarios", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.RecursoNecessario.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.RecursoNecessario.class.getName() + ".doacaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Doacao.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.FormaPagamento.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Acao.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Acao.class.getName() + ".profissaoNecessariaAcaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Acao.class.getName() + ".participacaos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Acao.class.getName() + ".enderecos", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Acao.class.getName() + ".recursoNecessarios", jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.Participacao.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.donazione.api.domain.ProfissaoNecessariaAcao.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
