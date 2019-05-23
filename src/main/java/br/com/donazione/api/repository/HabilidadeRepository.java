package br.com.donazione.api.repository;

import br.com.donazione.api.domain.Habilidade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Habilidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HabilidadeRepository extends JpaRepository<Habilidade, Long> {

}
