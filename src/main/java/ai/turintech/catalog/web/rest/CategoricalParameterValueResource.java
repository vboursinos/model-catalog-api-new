package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.CategoricalParameterValueRepository;
import ai.turintech.catalog.service.CategoricalParameterValueService;
import ai.turintech.catalog.service.dto.CategoricalParameterValueDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.CategoricalParameterValue}.
 */
@RestController
@RequestMapping("/api")
public class CategoricalParameterValueResource {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterValueResource.class);

    private static final String ENTITY_NAME = "modelCatalogCategoricalParameterValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoricalParameterValueService categoricalParameterValueService;

    private final CategoricalParameterValueRepository categoricalParameterValueRepository;

    public CategoricalParameterValueResource(
        CategoricalParameterValueService categoricalParameterValueService,
        CategoricalParameterValueRepository categoricalParameterValueRepository
    ) {
        this.categoricalParameterValueService = categoricalParameterValueService;
        this.categoricalParameterValueRepository = categoricalParameterValueRepository;
    }

    /**
     * {@code POST  /categorical-parameter-values} : Create a new categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the categoricalParameterValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoricalParameterValueDTO, or with status {@code 400 (Bad Request)} if the categoricalParameterValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorical-parameter-values")
    public Mono<ResponseEntity<CategoricalParameterValueDTO>> createCategoricalParameterValue(
        @Valid @RequestBody CategoricalParameterValueDTO categoricalParameterValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CategoricalParameterValue : {}", categoricalParameterValueDTO);
        if (categoricalParameterValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoricalParameterValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return categoricalParameterValueService
            .save(categoricalParameterValueDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/categorical-parameter-values/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /categorical-parameter-values/:id} : Updates an existing categoricalParameterValue.
     *
     * @param id the id of the categoricalParameterValueDTO to save.
     * @param categoricalParameterValueDTO the categoricalParameterValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoricalParameterValueDTO,
     * or with status {@code 400 (Bad Request)} if the categoricalParameterValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoricalParameterValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorical-parameter-values/{id}")
    public Mono<ResponseEntity<CategoricalParameterValueDTO>> updateCategoricalParameterValue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoricalParameterValueDTO categoricalParameterValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoricalParameterValue : {}, {}", id, categoricalParameterValueDTO);
        if (categoricalParameterValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoricalParameterValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return categoricalParameterValueRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return categoricalParameterValueService
                    .update(categoricalParameterValueDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /categorical-parameter-values/:id} : Partial updates given fields of an existing categoricalParameterValue, field will ignore if it is null
     *
     * @param id the id of the categoricalParameterValueDTO to save.
     * @param categoricalParameterValueDTO the categoricalParameterValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoricalParameterValueDTO,
     * or with status {@code 400 (Bad Request)} if the categoricalParameterValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoricalParameterValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoricalParameterValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorical-parameter-values/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CategoricalParameterValueDTO>> partialUpdateCategoricalParameterValue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoricalParameterValueDTO categoricalParameterValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoricalParameterValue partially : {}, {}", id, categoricalParameterValueDTO);
        if (categoricalParameterValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoricalParameterValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return categoricalParameterValueRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CategoricalParameterValueDTO> result = categoricalParameterValueService.partialUpdate(categoricalParameterValueDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /categorical-parameter-values} : get all the categoricalParameterValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoricalParameterValues in body.
     */
    @GetMapping(value = "/categorical-parameter-values", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CategoricalParameterValueDTO>> getAllCategoricalParameterValues() {
        log.debug("REST request to get all CategoricalParameterValues");
        return categoricalParameterValueService.findAll().collectList();
    }

    /**
     * {@code GET  /categorical-parameter-values} : get all the categoricalParameterValues as a stream.
     * @return the {@link Flux} of categoricalParameterValues.
     */
    @GetMapping(value = "/categorical-parameter-values", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CategoricalParameterValueDTO> getAllCategoricalParameterValuesAsStream() {
        log.debug("REST request to get all CategoricalParameterValues as a stream");
        return categoricalParameterValueService.findAll();
    }

    /**
     * {@code GET  /categorical-parameter-values/:id} : get the "id" categoricalParameterValue.
     *
     * @param id the id of the categoricalParameterValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoricalParameterValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorical-parameter-values/{id}")
    public Mono<ResponseEntity<CategoricalParameterValueDTO>> getCategoricalParameterValue(@PathVariable Long id) {
        log.debug("REST request to get CategoricalParameterValue : {}", id);
        Mono<CategoricalParameterValueDTO> categoricalParameterValueDTO = categoricalParameterValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoricalParameterValueDTO);
    }

    /**
     * {@code DELETE  /categorical-parameter-values/:id} : delete the "id" categoricalParameterValue.
     *
     * @param id the id of the categoricalParameterValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorical-parameter-values/{id}")
    public Mono<ResponseEntity<Void>> deleteCategoricalParameterValue(@PathVariable Long id) {
        log.debug("REST request to delete CategoricalParameterValue : {}", id);
        return categoricalParameterValueService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
