package br.com.donazione.api.web.rest;
import br.com.donazione.api.domain.FormaPagamento;
import br.com.donazione.api.repository.FormaPagamentoRepository;
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
 * REST controller for managing FormaPagamento.
 */
@RestController
@RequestMapping("/api")
public class FormaPagamentoResource {

    private final Logger log = LoggerFactory.getLogger(FormaPagamentoResource.class);

    private static final String ENTITY_NAME = "formaPagamento";

    private final FormaPagamentoRepository formaPagamentoRepository;

    public FormaPagamentoResource(FormaPagamentoRepository formaPagamentoRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    /**
     * POST  /forma-pagamentos : Create a new formaPagamento.
     *
     * @param formaPagamento the formaPagamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new formaPagamento, or with status 400 (Bad Request) if the formaPagamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/forma-pagamentos")
    public ResponseEntity<FormaPagamento> createFormaPagamento(@Valid @RequestBody FormaPagamento formaPagamento) throws URISyntaxException {
        log.debug("REST request to save FormaPagamento : {}", formaPagamento);
        if (formaPagamento.getId() != null) {
            throw new BadRequestAlertException("A new formaPagamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormaPagamento result = formaPagamentoRepository.save(formaPagamento);
        return ResponseEntity.created(new URI("/api/forma-pagamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /forma-pagamentos : Updates an existing formaPagamento.
     *
     * @param formaPagamento the formaPagamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated formaPagamento,
     * or with status 400 (Bad Request) if the formaPagamento is not valid,
     * or with status 500 (Internal Server Error) if the formaPagamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/forma-pagamentos")
    public ResponseEntity<FormaPagamento> updateFormaPagamento(@Valid @RequestBody FormaPagamento formaPagamento) throws URISyntaxException {
        log.debug("REST request to update FormaPagamento : {}", formaPagamento);
        if (formaPagamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormaPagamento result = formaPagamentoRepository.save(formaPagamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, formaPagamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /forma-pagamentos : get all the formaPagamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of formaPagamentos in body
     */
    @GetMapping("/forma-pagamentos")
    public List<FormaPagamento> getAllFormaPagamentos() {
        log.debug("REST request to get all FormaPagamentos");
        return formaPagamentoRepository.findAll();
    }

    /**
     * GET  /forma-pagamentos/:id : get the "id" formaPagamento.
     *
     * @param id the id of the formaPagamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the formaPagamento, or with status 404 (Not Found)
     */
    @GetMapping("/forma-pagamentos/{id}")
    public ResponseEntity<FormaPagamento> getFormaPagamento(@PathVariable Long id) {
        log.debug("REST request to get FormaPagamento : {}", id);
        Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formaPagamento);
    }

    /**
     * DELETE  /forma-pagamentos/:id : delete the "id" formaPagamento.
     *
     * @param id the id of the formaPagamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/forma-pagamentos/{id}")
    public ResponseEntity<Void> deleteFormaPagamento(@PathVariable Long id) {
        log.debug("REST request to delete FormaPagamento : {}", id);
        formaPagamentoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
