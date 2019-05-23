package br.com.donazione.api.repository;

import br.com.donazione.api.domain.Voluntario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Voluntario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {

    @Query(value = "select distinct voluntario from Voluntario voluntario left join fetch voluntario.habilidades left join fetch voluntario.profissaos",
        countQuery = "select count(distinct voluntario) from Voluntario voluntario")
    Page<Voluntario> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct voluntario from Voluntario voluntario left join fetch voluntario.habilidades left join fetch voluntario.profissaos")
    List<Voluntario> findAllWithEagerRelationships();

    @Query("select voluntario from Voluntario voluntario left join fetch voluntario.habilidades left join fetch voluntario.profissaos where voluntario.id =:id")
    Optional<Voluntario> findOneWithEagerRelationships(@Param("id") Long id);

}
