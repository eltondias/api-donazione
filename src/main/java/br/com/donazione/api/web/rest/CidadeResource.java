package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.Cidade;
import br.com.donazione.api.repository.CidadeRepository;
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
 * REST controller for managing Cidade.
 */
@RestController
@RequestMapping("/api")
public class CidadeResource {

    private final Logger log = LoggerFactory.getLogger(CidadeResource.class);

    private static final String ENTITY_NAME = "cidade";

    private final CidadeRepository cidadeRepository;

    public CidadeResource(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    /**
     * POST  /cidades : Create a new cidade.
     *
     * @param cidade the cidade to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cidade, or with status 400 (Bad Request) if the cidade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cidades")
    public ResponseEntity<Cidade> createCidade(@Valid @RequestBody Cidade cidade) throws URISyntaxException {
        log.debug("REST request to save Cidade : {}", cidade);
        if (cidade.getId() != null) {
            throw new BadRequestAlertException("A new cidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cidade result = cidadeRepository.save(cidade);
        return ResponseEntity.created(new URI("/api/cidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cidades : Updates an existing cidade.
     *
     * @param cidade the cidade to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cidade,
     * or with status 400 (Bad Request) if the cidade is not valid,
     * or with status 500 (Internal Server Error) if the cidade couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cidades")
    public ResponseEntity<Cidade> updateCidade(@Valid @RequestBody Cidade cidade) throws URISyntaxException {
        log.debug("REST request to update Cidade : {}", cidade);
        if (cidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cidade result = cidadeRepository.save(cidade);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cidade.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cidades : get all the cidades.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cidades in body
     */
    @GetMapping("/cidades")
    public List<Cidade> getAllCidades() {
        log.debug("REST request to get all Cidades");
        return cidadeRepository.findAll();
    }

    /**
     * GET  /cidades/:id : get the "id" cidade.
     *
     * @param id the id of the cidade to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cidade, or with status 404 (Not Found)
     */
    @GetMapping("/cidades/{id}")
    public ResponseEntity<Cidade> getCidade(@PathVariable Long id) {
        log.debug("REST request to get Cidade : {}", id);
        Optional<Cidade> cidade = cidadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cidade);
    }

    /**
     * DELETE  /cidades/:id : delete the "id" cidade.
     *
     * @param id the id of the cidade to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cidades/{id}")
    public ResponseEntity<Void> deleteCidade(@PathVariable Long id) {
        log.debug("REST request to delete Cidade : {}", id);
        cidadeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
