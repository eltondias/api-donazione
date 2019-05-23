package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Campanha;
import br.com.donazione.api.repository.CampanhaRepository;
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
 * REST controller for managing Campanha.
 */
@RestController
@RequestMapping("/api")
public class CampanhaResource {

    private final Logger log = LoggerFactory.getLogger(CampanhaResource.class);

    private static final String ENTITY_NAME = "campanha";

    private final CampanhaRepository campanhaRepository;

    public CampanhaResource(CampanhaRepository campanhaRepository) {
        this.campanhaRepository = campanhaRepository;
    }

    /**
     * POST  /campanhas : Create a new campanha.
     *
     * @param campanha the campanha to create
     * @return the ResponseEntity with status 201 (Created) and with body the new campanha, or with status 400 (Bad Request) if the campanha has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/campanhas")
    public ResponseEntity<Campanha> createCampanha(@Valid @RequestBody Campanha campanha) throws URISyntaxException {
        log.debug("REST request to save Campanha : {}", campanha);
        if (campanha.getId() != null) {
            throw new BadRequestAlertException("A new campanha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Campanha result = campanhaRepository.save(campanha);
        return ResponseEntity.created(new URI("/api/campanhas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /campanhas : Updates an existing campanha.
     *
     * @param campanha the campanha to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated campanha,
     * or with status 400 (Bad Request) if the campanha is not valid,
     * or with status 500 (Internal Server Error) if the campanha couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/campanhas")
    public ResponseEntity<Campanha> updateCampanha(@Valid @RequestBody Campanha campanha) throws URISyntaxException {
        log.debug("REST request to update Campanha : {}", campanha);
        if (campanha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Campanha result = campanhaRepository.save(campanha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, campanha.getId().toString()))
            .body(result);
    }

    /**
     * GET  /campanhas : get all the campanhas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of campanhas in body
     */
    @GetMapping("/campanhas")
    public List<Campanha> getAllCampanhas() {
        log.debug("REST request to get all Campanhas");
        return campanhaRepository.findAll();
    }

    /**
     * GET  /campanhas/:id : get the "id" campanha.
     *
     * @param id the id of the campanha to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the campanha, or with status 404 (Not Found)
     */
    @GetMapping("/campanhas/{id}")
    public ResponseEntity<Campanha> getCampanha(@PathVariable Long id) {
        log.debug("REST request to get Campanha : {}", id);
        Optional<Campanha> campanha = campanhaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(campanha);
    }

    /**
     * DELETE  /campanhas/:id : delete the "id" campanha.
     *
     * @param id the id of the campanha to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/campanhas/{id}")
    public ResponseEntity<Void> deleteCampanha(@PathVariable Long id) {
        log.debug("REST request to delete Campanha : {}", id);
        campanhaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
