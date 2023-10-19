package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.service.MlTaskTypeService;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
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
 * REST controller for managing {@link ai.turintech.catalog.domain.MlTaskType}.
 */
@RestController
@RequestMapping("/api")
public class MlTaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(MlTaskTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogMlTaskType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MlTaskTypeService mlTaskTypeService;

    private final MlTaskTypeRepository mlTaskTypeRepository;

    public MlTaskTypeResource(MlTaskTypeService mlTaskTypeService, MlTaskTypeRepository mlTaskTypeRepository) {
        this.mlTaskTypeService = mlTaskTypeService;
        this.mlTaskTypeRepository = mlTaskTypeRepository;
    }

    /**
     * {@code POST  /ml-task-types} : Create a new mlTaskType.
     *
     * @param mlTaskTypeDTO the mlTaskTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mlTaskTypeDTO, or with status {@code 400 (Bad Request)} if the mlTaskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ml-task-types")
    public Mono<ResponseEntity<MlTaskTypeDTO>> createMlTaskType(@Valid @RequestBody MlTaskTypeDTO mlTaskTypeDTO) throws URISyntaxException {
        log.debug("REST request to save MlTaskType : {}", mlTaskTypeDTO);
        if (mlTaskTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new mlTaskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mlTaskTypeDTO.setId(UUID.randomUUID());
        return mlTaskTypeService
            .save(mlTaskTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ml-task-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ml-task-types/:id} : Updates an existing mlTaskType.
     *
     * @param id the id of the mlTaskTypeDTO to save.
     * @param mlTaskTypeDTO the mlTaskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mlTaskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the mlTaskTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mlTaskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ml-task-types/{id}")
    public Mono<ResponseEntity<MlTaskTypeDTO>> updateMlTaskType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MlTaskTypeDTO mlTaskTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MlTaskType : {}, {}", id, mlTaskTypeDTO);
        if (mlTaskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlTaskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mlTaskTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return mlTaskTypeService
                    .update(mlTaskTypeDTO)
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
     * {@code PATCH  /ml-task-types/:id} : Partial updates given fields of an existing mlTaskType, field will ignore if it is null
     *
     * @param id the id of the mlTaskTypeDTO to save.
     * @param mlTaskTypeDTO the mlTaskTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mlTaskTypeDTO,
     * or with status {@code 400 (Bad Request)} if the mlTaskTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mlTaskTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mlTaskTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ml-task-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MlTaskTypeDTO>> partialUpdateMlTaskType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MlTaskTypeDTO mlTaskTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MlTaskType partially : {}, {}", id, mlTaskTypeDTO);
        if (mlTaskTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlTaskTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mlTaskTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MlTaskTypeDTO> result = mlTaskTypeService.partialUpdate(mlTaskTypeDTO);

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
     * {@code GET  /ml-task-types} : get all the mlTaskTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mlTaskTypes in body.
     */
    @GetMapping(value = "/ml-task-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MlTaskTypeDTO>> getAllMlTaskTypes() {
        log.debug("REST request to get all MlTaskTypes");
        return mlTaskTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /ml-task-types} : get all the mlTaskTypes as a stream.
     * @return the {@link Flux} of mlTaskTypes.
     */
    @GetMapping(value = "/ml-task-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MlTaskTypeDTO> getAllMlTaskTypesAsStream() {
        log.debug("REST request to get all MlTaskTypes as a stream");
        return mlTaskTypeService.findAll();
    }

    /**
     * {@code GET  /ml-task-types/:id} : get the "id" mlTaskType.
     *
     * @param id the id of the mlTaskTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mlTaskTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ml-task-types/{id}")
    public Mono<ResponseEntity<MlTaskTypeDTO>> getMlTaskType(@PathVariable UUID id) {
        log.debug("REST request to get MlTaskType : {}", id);
        Mono<MlTaskTypeDTO> mlTaskTypeDTO = mlTaskTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mlTaskTypeDTO);
    }

    /**
     * {@code DELETE  /ml-task-types/:id} : delete the "id" mlTaskType.
     *
     * @param id the id of the mlTaskTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ml-task-types/{id}")
    public Mono<ResponseEntity<Void>> deleteMlTaskType(@PathVariable UUID id) {
        log.debug("REST request to delete MlTaskType : {}", id);
        return mlTaskTypeService
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
