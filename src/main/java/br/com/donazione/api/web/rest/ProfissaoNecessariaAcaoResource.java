package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.ProfissaoNecessariaAcao;
import br.com.donazione.api.repository.ProfissaoNecessariaAcaoRepository;
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
 * REST controller for managing ProfissaoNecessariaAcao.
 */
@RestController
@RequestMapping("/api")
public class ProfissaoNecessariaAcaoResource {

    private final Logger log = LoggerFactory.getLogger(ProfissaoNecessariaAcaoResource.class);

    private static final String ENTITY_NAME = "profissaoNecessariaAcao";

    private final ProfissaoNecessariaAcaoRepository profissaoNecessariaAcaoRepository;

    public ProfissaoNecessariaAcaoResource(ProfissaoNecessariaAcaoRepository profissaoNecessariaAcaoRepository) {
        this.profissaoNecessariaAcaoRepository = profissaoNecessariaAcaoRepository;
    }

    /**
     * POST  /profissao-necessaria-acaos : Create a new profissaoNecessariaAcao.
     *
     * @param profissaoNecessariaAcao the profissaoNecessariaAcao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profissaoNecessariaAcao, or with status 400 (Bad Request) if the profissaoNecessariaAcao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profissao-necessaria-acaos")
    public ResponseEntity<ProfissaoNecessariaAcao> createProfissaoNecessariaAcao(@Valid @RequestBody ProfissaoNecessariaAcao profissaoNecessariaAcao) throws URISyntaxException {
        log.debug("REST request to save ProfissaoNecessariaAcao : {}", profissaoNecessariaAcao);
        if (profissaoNecessariaAcao.getId() != null) {
            throw new BadRequestAlertException("A new profissaoNecessariaAcao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfissaoNecessariaAcao result = profissaoNecessariaAcaoRepository.save(profissaoNecessariaAcao);
        return ResponseEntity.created(new URI("/api/profissao-necessaria-acaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profissao-necessaria-acaos : Updates an existing profissaoNecessariaAcao.
     *
     * @param profissaoNecessariaAcao the profissaoNecessariaAcao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profissaoNecessariaAcao,
     * or with status 400 (Bad Request) if the profissaoNecessariaAcao is not valid,
     * or with status 500 (Internal Server Error) if the profissaoNecessariaAcao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profissao-necessaria-acaos")
    public ResponseEntity<ProfissaoNecessariaAcao> updateProfissaoNecessariaAcao(@Valid @RequestBody ProfissaoNecessariaAcao profissaoNecessariaAcao) throws URISyntaxException {
        log.debug("REST request to update ProfissaoNecessariaAcao : {}", profissaoNecessariaAcao);
        if (profissaoNecessariaAcao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProfissaoNecessariaAcao result = profissaoNecessariaAcaoRepository.save(profissaoNecessariaAcao);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profissaoNecessariaAcao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profissao-necessaria-acaos : get all the profissaoNecessariaAcaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of profissaoNecessariaAcaos in body
     */
    @GetMapping("/profissao-necessaria-acaos")
    public List<ProfissaoNecessariaAcao> getAllProfissaoNecessariaAcaos() {
        log.debug("REST request to get all ProfissaoNecessariaAcaos");
        return profissaoNecessariaAcaoRepository.findAll();
    }

    /**
     * GET  /profissao-necessaria-acaos/:id : get the "id" profissaoNecessariaAcao.
     *
     * @param id the id of the profissaoNecessariaAcao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profissaoNecessariaAcao, or with status 404 (Not Found)
     */
    @GetMapping("/profissao-necessaria-acaos/{id}")
    public ResponseEntity<ProfissaoNecessariaAcao> getProfissaoNecessariaAcao(@PathVariable Long id) {
        log.debug("REST request to get ProfissaoNecessariaAcao : {}", id);
        Optional<ProfissaoNecessariaAcao> profissaoNecessariaAcao = profissaoNecessariaAcaoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(profissaoNecessariaAcao);
    }

    /**
     * DELETE  /profissao-necessaria-acaos/:id : delete the "id" profissaoNecessariaAcao.
     *
     * @param id the id of the profissaoNecessariaAcao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profissao-necessaria-acaos/{id}")
    public ResponseEntity<Void> deleteProfissaoNecessariaAcao(@PathVariable Long id) {
        log.debug("REST request to delete ProfissaoNecessariaAcao : {}", id);
        profissaoNecessariaAcaoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
