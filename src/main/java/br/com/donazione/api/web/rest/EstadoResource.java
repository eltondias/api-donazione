package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Estado;
import br.com.donazione.api.repository.EstadoRepository;
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
 * REST controller for managing Estado.
 */
@RestController
@RequestMapping("/api")
public class EstadoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoResource.class);

    private static final String ENTITY_NAME = "estado";

    private final EstadoRepository estadoRepository;

    public EstadoResource(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    /**
     * POST  /estados : Create a new estado.
     *
     * @param estado the estado to create
     * @return the ResponseEntity with status 201 (Created) and with body the new estado, or with status 400 (Bad Request) if the estado has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/estados")
    public ResponseEntity<Estado> createEstado(@Valid @RequestBody Estado estado) throws URISyntaxException {
        log.debug("REST request to save Estado : {}", estado);
        if (estado.getId() != null) {
            throw new BadRequestAlertException("A new estado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Estado result = estadoRepository.save(estado);
        return ResponseEntity.created(new URI("/api/estados/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estados : Updates an existing estado.
     *
     * @param estado the estado to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated estado,
     * or with status 400 (Bad Request) if the estado is not valid,
     * or with status 500 (Internal Server Error) if the estado couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/estados")
    public ResponseEntity<Estado> updateEstado(@Valid @RequestBody Estado estado) throws URISyntaxException {
        log.debug("REST request to update Estado : {}", estado);
        if (estado.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Estado result = estadoRepository.save(estado);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, estado.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estados : get all the estados.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of estados in body
     */
    @GetMapping("/estados")
    public List<Estado> getAllEstados() {
        log.debug("REST request to get all Estados");
        return estadoRepository.findAll();
    }

    /**
     * GET  /estados/:id : get the "id" estado.
     *
     * @param id the id of the estado to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the estado, or with status 404 (Not Found)
     */
    @GetMapping("/estados/{id}")
    public ResponseEntity<Estado> getEstado(@PathVariable Long id) {
        log.debug("REST request to get Estado : {}", id);
        Optional<Estado> estado = estadoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(estado);
    }

    /**
     * DELETE  /estados/:id : delete the "id" estado.
     *
     * @param id the id of the estado to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/estados/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        log.debug("REST request to delete Estado : {}", id);
        estadoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
