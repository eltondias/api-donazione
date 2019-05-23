package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.RecursoNecessario;
import br.com.donazione.api.repository.RecursoNecessarioRepository;
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
 * REST controller for managing RecursoNecessario.
 */
@RestController
@RequestMapping("/api")
public class RecursoNecessarioResource {

    private final Logger log = LoggerFactory.getLogger(RecursoNecessarioResource.class);

    private static final String ENTITY_NAME = "recursoNecessario";

    private final RecursoNecessarioRepository recursoNecessarioRepository;

    public RecursoNecessarioResource(RecursoNecessarioRepository recursoNecessarioRepository) {
        this.recursoNecessarioRepository = recursoNecessarioRepository;
    }

    /**
     * POST  /recurso-necessarios : Create a new recursoNecessario.
     *
     * @param recursoNecessario the recursoNecessario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recursoNecessario, or with status 400 (Bad Request) if the recursoNecessario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recurso-necessarios")
    public ResponseEntity<RecursoNecessario> createRecursoNecessario(@Valid @RequestBody RecursoNecessario recursoNecessario) throws URISyntaxException {
        log.debug("REST request to save RecursoNecessario : {}", recursoNecessario);
        if (recursoNecessario.getId() != null) {
            throw new BadRequestAlertException("A new recursoNecessario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecursoNecessario result = recursoNecessarioRepository.save(recursoNecessario);
        return ResponseEntity.created(new URI("/api/recurso-necessarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recurso-necessarios : Updates an existing recursoNecessario.
     *
     * @param recursoNecessario the recursoNecessario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recursoNecessario,
     * or with status 400 (Bad Request) if the recursoNecessario is not valid,
     * or with status 500 (Internal Server Error) if the recursoNecessario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recurso-necessarios")
    public ResponseEntity<RecursoNecessario> updateRecursoNecessario(@Valid @RequestBody RecursoNecessario recursoNecessario) throws URISyntaxException {
        log.debug("REST request to update RecursoNecessario : {}", recursoNecessario);
        if (recursoNecessario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RecursoNecessario result = recursoNecessarioRepository.save(recursoNecessario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recursoNecessario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recurso-necessarios : get all the recursoNecessarios.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recursoNecessarios in body
     */
    @GetMapping("/recurso-necessarios")
    public List<RecursoNecessario> getAllRecursoNecessarios() {
        log.debug("REST request to get all RecursoNecessarios");
        return recursoNecessarioRepository.findAll();
    }

    /**
     * GET  /recurso-necessarios/:id : get the "id" recursoNecessario.
     *
     * @param id the id of the recursoNecessario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recursoNecessario, or with status 404 (Not Found)
     */
    @GetMapping("/recurso-necessarios/{id}")
    public ResponseEntity<RecursoNecessario> getRecursoNecessario(@PathVariable Long id) {
        log.debug("REST request to get RecursoNecessario : {}", id);
        Optional<RecursoNecessario> recursoNecessario = recursoNecessarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recursoNecessario);
    }

    /**
     * DELETE  /recurso-necessarios/:id : delete the "id" recursoNecessario.
     *
     * @param id the id of the recursoNecessario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recurso-necessarios/{id}")
    public ResponseEntity<Void> deleteRecursoNecessario(@PathVariable Long id) {
        log.debug("REST request to delete RecursoNecessario : {}", id);
        recursoNecessarioRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
