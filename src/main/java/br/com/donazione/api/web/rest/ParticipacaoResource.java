package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Participacao;
import br.com.donazione.api.repository.ParticipacaoRepository;
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
 * REST controller for managing Participacao.
 */
@RestController
@RequestMapping("/api")
public class ParticipacaoResource {

    private final Logger log = LoggerFactory.getLogger(ParticipacaoResource.class);

    private static final String ENTITY_NAME = "participacao";

    private final ParticipacaoRepository participacaoRepository;

    public ParticipacaoResource(ParticipacaoRepository participacaoRepository) {
        this.participacaoRepository = participacaoRepository;
    }

    /**
     * POST  /participacaos : Create a new participacao.
     *
     * @param participacao the participacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new participacao, or with status 400 (Bad Request) if the participacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/participacaos")
    public ResponseEntity<Participacao> createParticipacao(@Valid @RequestBody Participacao participacao) throws URISyntaxException {
        log.debug("REST request to save Participacao : {}", participacao);
        if (participacao.getId() != null) {
            throw new BadRequestAlertException("A new participacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Participacao result = participacaoRepository.save(participacao);
        return ResponseEntity.created(new URI("/api/participacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /participacaos : Updates an existing participacao.
     *
     * @param participacao the participacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated participacao,
     * or with status 400 (Bad Request) if the participacao is not valid,
     * or with status 500 (Internal Server Error) if the participacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/participacaos")
    public ResponseEntity<Participacao> updateParticipacao(@Valid @RequestBody Participacao participacao) throws URISyntaxException {
        log.debug("REST request to update Participacao : {}", participacao);
        if (participacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Participacao result = participacaoRepository.save(participacao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, participacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /participacaos : get all the participacaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of participacaos in body
     */
    @GetMapping("/participacaos")
    public List<Participacao> getAllParticipacaos() {
        log.debug("REST request to get all Participacaos");
        return participacaoRepository.findAll();
    }

    /**
     * GET  /participacaos/:id : get the "id" participacao.
     *
     * @param id the id of the participacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the participacao, or with status 404 (Not Found)
     */
    @GetMapping("/participacaos/{id}")
    public ResponseEntity<Participacao> getParticipacao(@PathVariable Long id) {
        log.debug("REST request to get Participacao : {}", id);
        Optional<Participacao> participacao = participacaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(participacao);
    }

    /**
     * DELETE  /participacaos/:id : delete the "id" participacao.
     *
     * @param id the id of the participacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/participacaos/{id}")
    public ResponseEntity<Void> deleteParticipacao(@PathVariable Long id) {
        log.debug("REST request to delete Participacao : {}", id);
        participacaoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
