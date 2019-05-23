package br.com.donazione.api.repository;

import br.com.donazione.api.domain.Doacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Doacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

}
