package br.com.donazione.api.repository;

import br.com.donazione.api.domain.RecursoNecessario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RecursoNecessario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecursoNecessarioRepository extends JpaRepository<RecursoNecessario, Long> {

}
