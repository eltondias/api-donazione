package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Acao;
import br.com.donazione.api.repository.AcaoRepository;
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
 * REST controller for managing Acao.
 */
@RestController
@RequestMapping("/api")
public class AcaoResource {

    private final Logger log = LoggerFactory.getLogger(AcaoResource.class);

    private static final String ENTITY_NAME = "acao";

    private final AcaoRepository acaoRepository;

    public AcaoResource(AcaoRepository acaoRepository) {
        this.acaoRepository = acaoRepository;
    }

    /**
     * POST  /acaos : Create a new acao.
     *
     * @param acao the acao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new acao, or with status 400 (Bad Request) if the acao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/acaos")
    public ResponseEntity<Acao> createAcao(@Valid @RequestBody Acao acao) throws URISyntaxException {
        log.debug("REST request to save Acao : {}", acao);
        if (acao.getId() != null) {
            throw new BadRequestAlertException("A new acao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Acao result = acaoRepository.save(acao);
        return ResponseEntity.created(new URI("/api/acaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /acaos : Updates an existing acao.
     *
     * @param acao the acao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated acao,
     * or with status 400 (Bad Request) if the acao is not valid,
     * or with status 500 (Internal Server Error) if the acao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/acaos")
    public ResponseEntity<Acao> updateAcao(@Valid @RequestBody Acao acao) throws URISyntaxException {
        log.debug("REST request to update Acao : {}", acao);
        if (acao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Acao result = acaoRepository.save(acao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, acao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /acaos : get all the acaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of acaos in body
     */
    @GetMapping("/acaos")
    public List<Acao> getAllAcaos() {
        log.debug("REST request to get all Acaos");
        return acaoRepository.findAll();
    }

    /**
     * GET  /acaos/:id : get the "id" acao.
     *
     * @param id the id of the acao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the acao, or with status 404 (Not Found)
     */
    @GetMapping("/acaos/{id}")
    public ResponseEntity<Acao> getAcao(@PathVariable Long id) {
        log.debug("REST request to get Acao : {}", id);
        Optional<Acao> acao = acaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(acao);
    }

    /**
     * DELETE  /acaos/:id : delete the "id" acao.
     *
     * @param id the id of the acao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/acaos/{id}")
    public ResponseEntity<Void> deleteAcao(@PathVariable Long id) {
        log.debug("REST request to delete Acao : {}", id);
        acaoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
