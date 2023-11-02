package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.service.BooleanParameterService;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.BooleanParameter}.
 */
@RestController
@RequestMapping("/api")
public class BooleanParameterResource {

    private final Logger log = LoggerFactory.getLogger(BooleanParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogBooleanParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private BooleanParameterService booleanParameterService;

    private BooleanParameterRepository booleanParameterRepository;

//    public BooleanParameterResource(
//        BooleanParameterService booleanParameterService,
//        BooleanParameterRepository booleanParameterRepository
//    ) {
//        this.booleanParameterService = booleanParameterService;
//        this.booleanParameterRepository = booleanParameterRepository;
//    }

    /**
     * {@code POST  /boolean-parameters} : Create a new booleanParameter.
     *
     * @param booleanParameterDTO the booleanParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booleanParameterDTO, or with status {@code 400 (Bad Request)} if the booleanParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/boolean-parameters")
    public Mono<ResponseEntity<BooleanParameterDTO>> createBooleanParameter(@RequestBody BooleanParameterDTO booleanParameterDTO)
        throws URISyntaxException {
        log.debug("REST request to save BooleanParameter : {}", booleanParameterDTO);
        if (booleanParameterDTO.getParameterTypeDefinitionId() != null) {
            throw new BadRequestAlertException("A new booleanParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BooleanParameterDTO result = booleanParameterService.save(booleanParameterDTO);
        return Mono.just(ResponseEntity
                .created(new URI("/api/boolean-parameters/" + result.getParameterTypeDefinitionId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getParameterTypeDefinitionId().toString()))
                .body(result));
    }

    /**
     * {@code PUT  /boolean-parameters/:id} : Updates an existing booleanParameter.
     *
     * @param id the id of the booleanParameterDTO to save.
     * @param booleanParameterDTO the booleanParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booleanParameterDTO,
     * or with status {@code 400 (Bad Request)} if the booleanParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booleanParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/boolean-parameters/{id}")
    public Mono<ResponseEntity<BooleanParameterDTO>> updateBooleanParameter(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody BooleanParameterDTO booleanParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BooleanParameter : {}, {}", id, booleanParameterDTO);

        if (!booleanParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BooleanParameterDTO result = booleanParameterService.update(booleanParameterDTO);
        return Mono.justOrEmpty(ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getParameterTypeDefinitionId().toString()))
                .body(result));
    }

    /**
     * {@code PATCH  /boolean-parameters/:id} : Partial updates given fields of an existing booleanParameter, field will ignore if it is null
     *
     * @param id the id of the booleanParameterDTO to save.
     * @param booleanParameterDTO the booleanParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booleanParameterDTO,
     * or with status {@code 400 (Bad Request)} if the booleanParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the booleanParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the booleanParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/boolean-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BooleanParameterDTO>> partialUpdateBooleanParameter(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody BooleanParameterDTO booleanParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BooleanParameter partially : {}, {}", id, booleanParameterDTO);

        if (!booleanParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BooleanParameterDTO> result = booleanParameterService.partialUpdate(booleanParameterDTO);

        return Mono.justOrEmpty(tech.jhipster.web.util.ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, booleanParameterDTO.getParameterTypeDefinitionId().toString())
        ));
    }

    /**
     * {@code GET  /boolean-parameters} : get all the booleanParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of booleanParameters in body.
     */
    @GetMapping(value = "/boolean-parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<BooleanParameterDTO>> getAllBooleanParameters() {
        log.debug("REST request to get all BooleanParameters");
        return Mono.justOrEmpty(booleanParameterService.findAll());
    }

    /**
     * {@code GET  /boolean-parameters} : get all the booleanParameters as a stream.
     * @return the {@link Flux} of booleanParameters.
     */
    @GetMapping(value = "/boolean-parameters", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<BooleanParameterDTO> getAllBooleanParametersAsStream() {
        log.debug("REST request to get all BooleanParameters as a stream");
        return Flux.fromIterable(booleanParameterService.findAll());
    }

    /**
     * {@code GET  /boolean-parameters/:id} : get the "id" booleanParameter.
     *
     * @param id the id of the booleanParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booleanParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/boolean-parameters/{id}")
    public Mono<ResponseEntity<BooleanParameterDTO>> getBooleanParameter(@PathVariable UUID id) {
        log.debug("REST request to get BooleanParameter : {}", id);
        Optional<BooleanParameterDTO> booleanParameterDTO = booleanParameterService.findOne(id);
        return Mono.justOrEmpty(ResponseUtil.wrapOrNotFound(booleanParameterDTO));
    }

    /**
     * {@code DELETE  /boolean-parameters/:id} : delete the "id" booleanParameter.
     *
     * @param id the id of the booleanParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/boolean-parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteBooleanParameter(@PathVariable UUID id) {
        log.debug("REST request to delete BooleanParameter : {}", id);
        booleanParameterService.delete(id);
        return Mono.justOrEmpty(ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build());
    }
}
