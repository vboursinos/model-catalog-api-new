package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ModelTypeRepository;
import ai.turintech.catalog.service.ModelTypeService;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
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
 * REST controller for managing {@link ai.turintech.catalog.domain.ModelType}.
 */
@RestController
@RequestMapping("/api")
public class ModelTypeResource {

    private final Logger log = LoggerFactory.getLogger(ModelTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogModelType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelTypeService modelTypeService;

    private final ModelTypeRepository modelTypeRepository;

    public ModelTypeResource(ModelTypeService modelTypeService, ModelTypeRepository modelTypeRepository) {
        this.modelTypeService = modelTypeService;
        this.modelTypeRepository = modelTypeRepository;
    }

    /**
     * {@code POST  /model-types} : Create a new modelType.
     *
     * @param modelTypeDTO the modelTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelTypeDTO, or with status {@code 400 (Bad Request)} if the modelType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/model-types")
    public Mono<ResponseEntity<ModelTypeDTO>> createModelType(@Valid @RequestBody ModelTypeDTO modelTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ModelType : {}", modelTypeDTO);
        if (modelTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new modelType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelTypeDTO.setId(UUID.randomUUID());
        return modelTypeService
            .save(modelTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/model-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /model-types/:id} : Updates an existing modelType.
     *
     * @param id the id of the modelTypeDTO to save.
     * @param modelTypeDTO the modelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/model-types/{id}")
    public Mono<ResponseEntity<ModelTypeDTO>> updateModelType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ModelTypeDTO modelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModelType : {}, {}", id, modelTypeDTO);
        if (modelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return modelTypeService
                    .update(modelTypeDTO)
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
     * {@code PATCH  /model-types/:id} : Partial updates given fields of an existing modelType, field will ignore if it is null
     *
     * @param id the id of the modelTypeDTO to save.
     * @param modelTypeDTO the modelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modelTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/model-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModelTypeDTO>> partialUpdateModelType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ModelTypeDTO modelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModelType partially : {}, {}", id, modelTypeDTO);
        if (modelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModelTypeDTO> result = modelTypeService.partialUpdate(modelTypeDTO);

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
     * {@code GET  /model-types} : get all the modelTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelTypes in body.
     */
    @GetMapping(value = "/model-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ModelTypeDTO>> getAllModelTypes() {
        log.debug("REST request to get all ModelTypes");
        return modelTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /model-types} : get all the modelTypes as a stream.
     * @return the {@link Flux} of modelTypes.
     */
    @GetMapping(value = "/model-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModelTypeDTO> getAllModelTypesAsStream() {
        log.debug("REST request to get all ModelTypes as a stream");
        return modelTypeService.findAll();
    }

    /**
     * {@code GET  /model-types/:id} : get the "id" modelType.
     *
     * @param id the id of the modelTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/model-types/{id}")
    public Mono<ResponseEntity<ModelTypeDTO>> getModelType(@PathVariable UUID id) {
        log.debug("REST request to get ModelType : {}", id);
        Mono<ModelTypeDTO> modelTypeDTO = modelTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelTypeDTO);
    }

    /**
     * {@code DELETE  /model-types/:id} : delete the "id" modelType.
     *
     * @param id the id of the modelTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/model-types/{id}")
    public Mono<ResponseEntity<Void>> deleteModelType(@PathVariable UUID id) {
        log.debug("REST request to delete ModelType : {}", id);
        return modelTypeService
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
