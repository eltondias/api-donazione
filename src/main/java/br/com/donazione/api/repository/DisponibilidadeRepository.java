package br.com.donazione.api.repository;

import br.com.donazione.api.domain.Disponibilidade;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Disponibilidade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {

}
