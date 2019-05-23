package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Disponibilidade;
import br.com.donazione.api.repository.DisponibilidadeRepository;
import br.com.donazione.api.web.rest.errors.BadRequestAlertException;
import br.com.donazione.api.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Disponibilidade.
 */
@RestController
@RequestMapping("/api")
public class DisponibilidadeResource {

    private final Logger log = LoggerFactory.getLogger(DisponibilidadeResource.class);

    private static final String ENTITY_NAME = "disponibilidade";

    private final DisponibilidadeRepository disponibilidadeRepository;

    public DisponibilidadeResource(DisponibilidadeRepository disponibilidadeRepository) {
        this.disponibilidadeRepository = disponibilidadeRepository;
    }

    /**
     * POST  /disponibilidades : Create a new disponibilidade.
     *
     * @param disponibilidade the disponibilidade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new disponibilidade, or with status 400 (Bad Request) if the disponibilidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/disponibilidades")
    public ResponseEntity<Disponibilidade> createDisponibilidade(@Valid @RequestBody Disponibilidade disponibilidade) throws URISyntaxException {
        log.debug("REST request to save Disponibilidade : {}", disponibilidade);
        if (disponibilidade.getId() != null) {
            throw new BadRequestAlertException("A new disponibilidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Disponibilidade result = disponibilidadeRepository.save(disponibilidade);
        return ResponseEntity.created(new URI("/api/disponibilidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /disponibilidades : Updates an existing disponibilidade.
     *
     * @param disponibilidade the disponibilidade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated disponibilidade,
     * or with status 400 (Bad Request) if the disponibilidade is not valid,
     * or with status 500 (Internal Server Error) if the disponibilidade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/disponibilidades")
    public ResponseEntity<Disponibilidade> updateDisponibilidade(@Valid @RequestBody Disponibilidade disponibilidade) throws URISyntaxException {
        log.debug("REST request to update Disponibilidade : {}", disponibilidade);
        if (disponibilidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Disponibilidade result = disponibilidadeRepository.save(disponibilidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, disponibilidade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /disponibilidades : get all the disponibilidades.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of disponibilidades in body
     */
    @GetMapping("/disponibilidades")
    public List<Disponibilidade> getAllDisponibilidades() {
        log.debug("REST request to get all Disponibilidades");
        return disponibilidadeRepository.findAll();
    }

    /**
     * GET  /disponibilidades/:id : get the "id" disponibilidade.
     *
     * @param id the id of the disponibilidade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the disponibilidade, or with status 404 (Not Found)
     */
    @GetMapping("/disponibilidades/{id}")
    public ResponseEntity<Disponibilidade> getDisponibilidade(@PathVariable Long id) {
        log.debug("REST request to get Disponibilidade : {}", id);
        Optional<Disponibilidade> disponibilidade = disponibilidadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(disponibilidade);
    }

    /**
     * DELETE  /disponibilidades/:id : delete the "id" disponibilidade.
     *
     * @param id the id of the disponibilidade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/disponibilidades/{id}")
    public ResponseEntity<Void> deleteDisponibilidade(@PathVariable Long id) {
        log.debug("REST request to delete Disponibilidade : {}", id);
        disponibilidadeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
