package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.FloatParameterRangeRepository;
import ai.turintech.catalog.service.FloatParameterRangeService;
import ai.turintech.catalog.service.dto.FloatParameterRangeDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ai.turintech.catalog.domain.FloatParameterRange}.
 */
@RestController
@RequestMapping("/api")
public class FloatParameterRangeResource {

    private final Logger log = LoggerFactory.getLogger(FloatParameterRangeResource.class);

    private static final String ENTITY_NAME = "modelCatalogFloatParameterRange";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private FloatParameterRangeService floatParameterRangeService;

    private FloatParameterRangeRepository floatParameterRangeRepository;

//    public FloatParameterRangeResource(
//        FloatParameterRangeService floatParameterRangeService,
//        FloatParameterRangeRepository floatParameterRangeRepository
//    ) {
//        this.floatParameterRangeService = floatParameterRangeService;
//        this.floatParameterRangeRepository = floatParameterRangeRepository;
//    }

    /**
     * {@code POST  /float-parameter-ranges} : Create a new floatParameterRange.
     *
     * @param floatParameterRangeDTO the floatParameterRangeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floatParameterRangeDTO, or with status {@code 400 (Bad Request)} if the floatParameterRange has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/float-parameter-ranges")
    public Mono<ResponseEntity<FloatParameterRangeDTO>> createFloatParameterRange(
        @Valid @RequestBody FloatParameterRangeDTO floatParameterRangeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FloatParameterRange : {}", floatParameterRangeDTO);
        if (floatParameterRangeDTO.getId() != null) {
            throw new BadRequestAlertException("A new floatParameterRange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FloatParameterRangeDTO result = floatParameterRangeService.save(floatParameterRangeDTO);
        return Mono.justOrEmpty(ResponseEntity
                .created(new URI("/api/float-parameter-ranges/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result));
    }

    /**
     * {@code PUT  /float-parameter-ranges/:id} : Updates an existing floatParameterRange.
     *
     * @param id the id of the floatParameterRangeDTO to save.
     * @param floatParameterRangeDTO the floatParameterRangeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floatParameterRangeDTO,
     * or with status {@code 400 (Bad Request)} if the floatParameterRangeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floatParameterRangeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/float-parameter-ranges/{id}")
    public Mono<ResponseEntity<FloatParameterRangeDTO>> updateFloatParameterRange(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody FloatParameterRangeDTO floatParameterRangeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FloatParameterRange : {}, {}", id, floatParameterRangeDTO);
        if (floatParameterRangeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floatParameterRangeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floatParameterRangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FloatParameterRangeDTO result = floatParameterRangeService.update(floatParameterRangeDTO);
        return Mono.justOrEmpty(ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, floatParameterRangeDTO.getId().toString()))
                .body(result));
    }

    /**
     * {@code PATCH  /float-parameter-ranges/:id} : Partial updates given fields of an existing floatParameterRange, field will ignore if it is null
     *
     * @param id the id of the floatParameterRangeDTO to save.
     * @param floatParameterRangeDTO the floatParameterRangeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floatParameterRangeDTO,
     * or with status {@code 400 (Bad Request)} if the floatParameterRangeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the floatParameterRangeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the floatParameterRangeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/float-parameter-ranges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FloatParameterRangeDTO>> partialUpdateFloatParameterRange(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody FloatParameterRangeDTO floatParameterRangeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FloatParameterRange partially : {}, {}", id, floatParameterRangeDTO);
        if (floatParameterRangeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floatParameterRangeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floatParameterRangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FloatParameterRangeDTO> result = floatParameterRangeService.partialUpdate(floatParameterRangeDTO);

        return Mono.justOrEmpty(tech.jhipster.web.util.ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, floatParameterRangeDTO.getId().toString()))
        );
    }

    /**
     * {@code GET  /float-parameter-ranges} : get all the floatParameterRanges.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floatParameterRanges in body.
     */
    @GetMapping(value = "/float-parameter-ranges", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FloatParameterRangeDTO>> getAllFloatParameterRanges() {
        log.debug("REST request to get all FloatParameterRanges");
        return Mono.justOrEmpty(floatParameterRangeService.findAll());
    }

    /**
     * {@code GET  /float-parameter-ranges} : get all the floatParameterRanges as a stream.
     * @return the {@link Flux} of floatParameterRanges.
     */
    @GetMapping(value = "/float-parameter-ranges", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FloatParameterRangeDTO> getAllFloatParameterRangesAsStream() {
        log.debug("REST request to get all FloatParameterRanges as a stream");
        return Flux.fromIterable(floatParameterRangeService.findAll());
    }

    /**
     * {@code GET  /float-parameter-ranges/:id} : get the "id" floatParameterRange.
     *
     * @param id the id of the floatParameterRangeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floatParameterRangeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/float-parameter-ranges/{id}")
    public Mono<ResponseEntity<FloatParameterRangeDTO>> getFloatParameterRange(@PathVariable UUID id) {
        log.debug("REST request to get FloatParameterRange : {}", id);
        Optional<FloatParameterRangeDTO> floatParameterRangeDTO = floatParameterRangeService.findOne(id);
        return Mono.justOrEmpty(ResponseUtil.wrapOrNotFound(floatParameterRangeDTO));
    }

    /**
     * {@code DELETE  /float-parameter-ranges/:id} : delete the "id" floatParameterRange.
     *
     * @param id the id of the floatParameterRangeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/float-parameter-ranges/{id}")
    public Mono<ResponseEntity<Void>> deleteFloatParameterRange(@PathVariable UUID id) {
        log.debug("REST request to delete FloatParameterRange : {}", id);
        floatParameterRangeService.delete(id);
        return Mono.justOrEmpty(ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build());
    }
}
