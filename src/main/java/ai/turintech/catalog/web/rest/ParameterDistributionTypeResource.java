package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ParameterDistributionTypeRepository;
import ai.turintech.catalog.service.ParameterDistributionTypeService;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
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
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.ParameterDistributionType}.
 */
@RestController
@RequestMapping("/api")
public class ParameterDistributionTypeResource {

    private final Logger log = LoggerFactory.getLogger(ParameterDistributionTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogParameterDistributionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParameterDistributionTypeService parameterDistributionTypeService;

    private final ParameterDistributionTypeRepository parameterDistributionTypeRepository;

    public ParameterDistributionTypeResource(
        ParameterDistributionTypeService parameterDistributionTypeService,
        ParameterDistributionTypeRepository parameterDistributionTypeRepository
    ) {
        this.parameterDistributionTypeService = parameterDistributionTypeService;
        this.parameterDistributionTypeRepository = parameterDistributionTypeRepository;
    }

    /**
     * {@code POST  /parameter-distribution-types} : Create a new parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the parameterDistributionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parameterDistributionTypeDTO, or with status {@code 400 (Bad Request)} if the parameterDistributionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parameter-distribution-types")
    public Mono<ResponseEntity<ParameterDistributionTypeDTO>> createParameterDistributionType(
        @Valid @RequestBody ParameterDistributionTypeDTO parameterDistributionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ParameterDistributionType : {}", parameterDistributionTypeDTO);
        if (parameterDistributionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new parameterDistributionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        parameterDistributionTypeDTO.setId(UUID.randomUUID());
        return parameterDistributionTypeService
            .save(parameterDistributionTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parameter-distribution-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /parameter-distribution-types/:id} : Updates an existing parameterDistributionType.
     *
     * @param id the id of the parameterDistributionTypeDTO to save.
     * @param parameterDistributionTypeDTO the parameterDistributionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterDistributionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the parameterDistributionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parameterDistributionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parameter-distribution-types/{id}")
    public Mono<ResponseEntity<ParameterDistributionTypeDTO>> updateParameterDistributionType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ParameterDistributionTypeDTO parameterDistributionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParameterDistributionType : {}, {}", id, parameterDistributionTypeDTO);
        if (parameterDistributionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterDistributionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parameterDistributionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return parameterDistributionTypeService
                    .update(parameterDistributionTypeDTO)
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
     * {@code PATCH  /parameter-distribution-types/:id} : Partial updates given fields of an existing parameterDistributionType, field will ignore if it is null
     *
     * @param id the id of the parameterDistributionTypeDTO to save.
     * @param parameterDistributionTypeDTO the parameterDistributionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterDistributionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the parameterDistributionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parameterDistributionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parameterDistributionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parameter-distribution-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ParameterDistributionTypeDTO>> partialUpdateParameterDistributionType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ParameterDistributionTypeDTO parameterDistributionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParameterDistributionType partially : {}, {}", id, parameterDistributionTypeDTO);
        if (parameterDistributionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterDistributionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return parameterDistributionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ParameterDistributionTypeDTO> result = parameterDistributionTypeService.partialUpdate(parameterDistributionTypeDTO);

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
     * {@code GET  /parameter-distribution-types} : get all the parameterDistributionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parameterDistributionTypes in body.
     */
    @GetMapping(value = "/parameter-distribution-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ParameterDistributionTypeDTO>> getAllParameterDistributionTypes() {
        log.debug("REST request to get all ParameterDistributionTypes");
        return parameterDistributionTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /parameter-distribution-types} : get all the parameterDistributionTypes as a stream.
     * @return the {@link Flux} of parameterDistributionTypes.
     */
    @GetMapping(value = "/parameter-distribution-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ParameterDistributionTypeDTO> getAllParameterDistributionTypesAsStream() {
        log.debug("REST request to get all ParameterDistributionTypes as a stream");
        return parameterDistributionTypeService.findAll();
    }

    /**
     * {@code GET  /parameter-distribution-types/:id} : get the "id" parameterDistributionType.
     *
     * @param id the id of the parameterDistributionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parameterDistributionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parameter-distribution-types/{id}")
    public Mono<ResponseEntity<ParameterDistributionTypeDTO>> getParameterDistributionType(@PathVariable UUID id) {
        log.debug("REST request to get ParameterDistributionType : {}", id);
        Mono<ParameterDistributionTypeDTO> parameterDistributionTypeDTO = parameterDistributionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parameterDistributionTypeDTO);
    }

    /**
     * {@code DELETE  /parameter-distribution-types/:id} : delete the "id" parameterDistributionType.
     *
     * @param id the id of the parameterDistributionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parameter-distribution-types/{id}")
    public Mono<ResponseEntity<Void>> deleteParameterDistributionType(@PathVariable UUID id) {
        log.debug("REST request to delete ParameterDistributionType : {}", id);
        return parameterDistributionTypeService
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
