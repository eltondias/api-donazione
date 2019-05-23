package br.com.donazione.api.repository;

import br.com.donazione.api.domain.ProfissaoNecessariaAcao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProfissaoNecessariaAcao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfissaoNecessariaAcaoRepository extends JpaRepository<ProfissaoNecessariaAcao, Long> {

}
