package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Habilidade;
import br.com.donazione.api.repository.HabilidadeRepository;
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
 * REST controller for managing Habilidade.
 */
@RestController
@RequestMapping("/api")
public class HabilidadeResource {

    private final Logger log = LoggerFactory.getLogger(HabilidadeResource.class);

    private static final String ENTITY_NAME = "habilidade";

    private final HabilidadeRepository habilidadeRepository;

    public HabilidadeResource(HabilidadeRepository habilidadeRepository) {
        this.habilidadeRepository = habilidadeRepository;
    }

    /**
     * POST  /habilidades : Create a new habilidade.
     *
     * @param habilidade the habilidade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new habilidade, or with status 400 (Bad Request) if the habilidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/habilidades")
    public ResponseEntity<Habilidade> createHabilidade(@Valid @RequestBody Habilidade habilidade) throws URISyntaxException {
        log.debug("REST request to save Habilidade : {}", habilidade);
        if (habilidade.getId() != null) {
            throw new BadRequestAlertException("A new habilidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Habilidade result = habilidadeRepository.save(habilidade);
        return ResponseEntity.created(new URI("/api/habilidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /habilidades : Updates an existing habilidade.
     *
     * @param habilidade the habilidade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated habilidade,
     * or with status 400 (Bad Request) if the habilidade is not valid,
     * or with status 500 (Internal Server Error) if the habilidade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/habilidades")
    public ResponseEntity<Habilidade> updateHabilidade(@Valid @RequestBody Habilidade habilidade) throws URISyntaxException {
        log.debug("REST request to update Habilidade : {}", habilidade);
        if (habilidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Habilidade result = habilidadeRepository.save(habilidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, habilidade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /habilidades : get all the habilidades.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of habilidades in body
     */
    @GetMapping("/habilidades")
    public List<Habilidade> getAllHabilidades() {
        log.debug("REST request to get all Habilidades");
        return habilidadeRepository.findAll();
    }

    /**
     * GET  /habilidades/:id : get the "id" habilidade.
     *
     * @param id the id of the habilidade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the habilidade, or with status 404 (Not Found)
     */
    @GetMapping("/habilidades/{id}")
    public ResponseEntity<Habilidade> getHabilidade(@PathVariable Long id) {
        log.debug("REST request to get Habilidade : {}", id);
        Optional<Habilidade> habilidade = habilidadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(habilidade);
    }

    /**
     * DELETE  /habilidades/:id : delete the "id" habilidade.
     *
     * @param id the id of the habilidade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/habilidades/{id}")
    public ResponseEntity<Void> deleteHabilidade(@PathVariable Long id) {
        log.debug("REST request to delete Habilidade : {}", id);
        habilidadeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
