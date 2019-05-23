package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.RedeSocial;
import br.com.donazione.api.repository.RedeSocialRepository;
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
 * REST controller for managing RedeSocial.
 */
@RestController
@RequestMapping("/api")
public class RedeSocialResource {

    private final Logger log = LoggerFactory.getLogger(RedeSocialResource.class);

    private static final String ENTITY_NAME = "redeSocial";

    private final RedeSocialRepository redeSocialRepository;

    public RedeSocialResource(RedeSocialRepository redeSocialRepository) {
        this.redeSocialRepository = redeSocialRepository;
    }

    /**
     * POST  /rede-socials : Create a new redeSocial.
     *
     * @param redeSocial the redeSocial to create
     * @return the ResponseEntity with status 201 (Created) and with body the new redeSocial, or with status 400 (Bad Request) if the redeSocial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/rede-socials")
    public ResponseEntity<RedeSocial> createRedeSocial(@Valid @RequestBody RedeSocial redeSocial) throws URISyntaxException {
        log.debug("REST request to save RedeSocial : {}", redeSocial);
        if (redeSocial.getId() != null) {
            throw new BadRequestAlertException("A new redeSocial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RedeSocial result = redeSocialRepository.save(redeSocial);
        return ResponseEntity.created(new URI("/api/rede-socials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rede-socials : Updates an existing redeSocial.
     *
     * @param redeSocial the redeSocial to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated redeSocial,
     * or with status 400 (Bad Request) if the redeSocial is not valid,
     * or with status 500 (Internal Server Error) if the redeSocial couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/rede-socials")
    public ResponseEntity<RedeSocial> updateRedeSocial(@Valid @RequestBody RedeSocial redeSocial) throws URISyntaxException {
        log.debug("REST request to update RedeSocial : {}", redeSocial);
        if (redeSocial.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RedeSocial result = redeSocialRepository.save(redeSocial);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, redeSocial.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rede-socials : get all the redeSocials.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of redeSocials in body
     */
    @GetMapping("/rede-socials")
    public List<RedeSocial> getAllRedeSocials() {
        log.debug("REST request to get all RedeSocials");
        return redeSocialRepository.findAll();
    }

    /**
     * GET  /rede-socials/:id : get the "id" redeSocial.
     *
     * @param id the id of the redeSocial to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the redeSocial, or with status 404 (Not Found)
     */
    @GetMapping("/rede-socials/{id}")
    public ResponseEntity<RedeSocial> getRedeSocial(@PathVariable Long id) {
        log.debug("REST request to get RedeSocial : {}", id);
        Optional<RedeSocial> redeSocial = redeSocialRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(redeSocial);
    }

    /**
     * DELETE  /rede-socials/:id : delete the "id" redeSocial.
     *
     * @param id the id of the redeSocial to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/rede-socials/{id}")
    public ResponseEntity<Void> deleteRedeSocial(@PathVariable Long id) {
        log.debug("REST request to delete RedeSocial : {}", id);
        redeSocialRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
