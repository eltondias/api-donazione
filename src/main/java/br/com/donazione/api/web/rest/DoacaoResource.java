package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Doacao;
import br.com.donazione.api.repository.DoacaoRepository;
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
 * REST controller for managing Doacao.
 */
@RestController
@RequestMapping("/api")
public class DoacaoResource {

    private final Logger log = LoggerFactory.getLogger(DoacaoResource.class);

    private static final String ENTITY_NAME = "doacao";

    private final DoacaoRepository doacaoRepository;

    public DoacaoResource(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    /**
     * POST  /doacaos : Create a new doacao.
     *
     * @param doacao the doacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doacao, or with status 400 (Bad Request) if the doacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doacaos")
    public ResponseEntity<Doacao> createDoacao(@Valid @RequestBody Doacao doacao) throws URISyntaxException {
        log.debug("REST request to save Doacao : {}", doacao);
        if (doacao.getId() != null) {
            throw new BadRequestAlertException("A new doacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Doacao result = doacaoRepository.save(doacao);
        return ResponseEntity.created(new URI("/api/doacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doacaos : Updates an existing doacao.
     *
     * @param doacao the doacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doacao,
     * or with status 400 (Bad Request) if the doacao is not valid,
     * or with status 500 (Internal Server Error) if the doacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doacaos")
    public ResponseEntity<Doacao> updateDoacao(@Valid @RequestBody Doacao doacao) throws URISyntaxException {
        log.debug("REST request to update Doacao : {}", doacao);
        if (doacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Doacao result = doacaoRepository.save(doacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doacaos : get all the doacaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doacaos in body
     */
    @GetMapping("/doacaos")
    public List<Doacao> getAllDoacaos() {
        log.debug("REST request to get all Doacaos");
        return doacaoRepository.findAll();
    }

    /**
     * GET  /doacaos/:id : get the "id" doacao.
     *
     * @param id the id of the doacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doacao, or with status 404 (Not Found)
     */
    @GetMapping("/doacaos/{id}")
    public ResponseEntity<Doacao> getDoacao(@PathVariable Long id) {
        log.debug("REST request to get Doacao : {}", id);
        Optional<Doacao> doacao = doacaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(doacao);
    }

    /**
     * DELETE  /doacaos/:id : delete the "id" doacao.
     *
     * @param id the id of the doacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doacaos/{id}")
    public ResponseEntity<Void> deleteDoacao(@PathVariable Long id) {
        log.debug("REST request to delete Doacao : {}", id);
        doacaoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
