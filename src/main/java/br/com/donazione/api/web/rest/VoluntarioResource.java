package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Voluntario;
import br.com.donazione.api.repository.VoluntarioRepository;
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
 * REST controller for managing Voluntario.
 */
@RestController
@RequestMapping("/api")
public class VoluntarioResource {

    private final Logger log = LoggerFactory.getLogger(VoluntarioResource.class);

    private static final String ENTITY_NAME = "voluntario";

    private final VoluntarioRepository voluntarioRepository;

    public VoluntarioResource(VoluntarioRepository voluntarioRepository) {
        this.voluntarioRepository = voluntarioRepository;
    }

    /**
     * POST  /voluntarios : Create a new voluntario.
     *
     * @param voluntario the voluntario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voluntario, or with status 400 (Bad Request) if the voluntario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voluntarios")
    public ResponseEntity<Voluntario> createVoluntario(@Valid @RequestBody Voluntario voluntario) throws URISyntaxException {
        log.debug("REST request to save Voluntario : {}", voluntario);
        if (voluntario.getId() != null) {
            throw new BadRequestAlertException("A new voluntario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voluntario result = voluntarioRepository.save(voluntario);
        return ResponseEntity.created(new URI("/api/voluntarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


     /**
     * POST  /voluntarios : Create a new voluntario.
     *
     * @param voluntario the voluntario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new voluntario, or with status 400 (Bad Request) if the voluntario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/voluntarios/login")
    public ResponseEntity<Voluntario> loginVoluntario(@RequestBody Voluntario voluntario) throws URISyntaxException {
        log.debug("REST request to save Voluntario : {}", voluntario);       
        Optional<Voluntario> result = voluntarioRepository.loginVoluntario(voluntario.getLogin(), voluntario.getSenha());
        return ResponseUtil.wrapOrNotFound(result);
    }




    /**
     * PUT  /voluntarios : Updates an existing voluntario.
     *
     * @param voluntario the voluntario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated voluntario,
     * or with status 400 (Bad Request) if the voluntario is not valid,
     * or with status 500 (Internal Server Error) if the voluntario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/voluntarios")
    public ResponseEntity<Voluntario> updateVoluntario(@Valid @RequestBody Voluntario voluntario) throws URISyntaxException {
        log.debug("REST request to update Voluntario : {}", voluntario);
        if (voluntario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Voluntario result = voluntarioRepository.save(voluntario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, voluntario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /voluntarios : get all the voluntarios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of voluntarios in body
     */
    @GetMapping("/voluntarios")
    public List<Voluntario> getAllVoluntarios(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Voluntarios");
        return voluntarioRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /voluntarios/:id : get the "id" voluntario.
     *
     * @param id the id of the voluntario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the voluntario, or with status 404 (Not Found)
     */
    @GetMapping("/voluntarios/{id}")
    public ResponseEntity<Voluntario> getVoluntario(@PathVariable Long id) {
        log.debug("REST request to get Voluntario : {}", id);
        Optional<Voluntario> voluntario = voluntarioRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(voluntario);
    }

    /**
     * DELETE  /voluntarios/:id : delete the "id" voluntario.
     *
     * @param id the id of the voluntario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/voluntarios/{id}")
    public ResponseEntity<Void> deleteVoluntario(@PathVariable Long id) {
        log.debug("REST request to delete Voluntario : {}", id);
        voluntarioRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
