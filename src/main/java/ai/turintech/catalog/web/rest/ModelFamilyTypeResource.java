package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.ModelFamilyTypeService;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
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
 * REST controller for managing {@link ai.turintech.catalog.domain.ModelFamilyType}.
 */
@RestController
@RequestMapping("/api")
public class ModelFamilyTypeResource {

    private final Logger log = LoggerFactory.getLogger(ModelFamilyTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogModelFamilyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelFamilyTypeService modelFamilyTypeService;

    private final ModelFamilyTypeRepository modelFamilyTypeRepository;

    public ModelFamilyTypeResource(ModelFamilyTypeService modelFamilyTypeService, ModelFamilyTypeRepository modelFamilyTypeRepository) {
        this.modelFamilyTypeService = modelFamilyTypeService;
        this.modelFamilyTypeRepository = modelFamilyTypeRepository;
    }

    /**
     * {@code POST  /model-family-types} : Create a new modelFamilyType.
     *
     * @param modelFamilyTypeDTO the modelFamilyTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelFamilyTypeDTO, or with status {@code 400 (Bad Request)} if the modelFamilyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/model-family-types")
    public Mono<ResponseEntity<ModelFamilyTypeDTO>> createModelFamilyType(@Valid @RequestBody ModelFamilyTypeDTO modelFamilyTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ModelFamilyType : {}", modelFamilyTypeDTO);
        if (modelFamilyTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new modelFamilyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelFamilyTypeDTO.setId(UUID.randomUUID());
        return modelFamilyTypeService
            .save(modelFamilyTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/model-family-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /model-family-types/:id} : Updates an existing modelFamilyType.
     *
     * @param id the id of the modelFamilyTypeDTO to save.
     * @param modelFamilyTypeDTO the modelFamilyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelFamilyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelFamilyTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelFamilyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/model-family-types/{id}")
    public Mono<ResponseEntity<ModelFamilyTypeDTO>> updateModelFamilyType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ModelFamilyTypeDTO modelFamilyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModelFamilyType : {}, {}", id, modelFamilyTypeDTO);
        if (modelFamilyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelFamilyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelFamilyTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return modelFamilyTypeService
                    .update(modelFamilyTypeDTO)
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
     * {@code PATCH  /model-family-types/:id} : Partial updates given fields of an existing modelFamilyType, field will ignore if it is null
     *
     * @param id the id of the modelFamilyTypeDTO to save.
     * @param modelFamilyTypeDTO the modelFamilyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelFamilyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelFamilyTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modelFamilyTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelFamilyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/model-family-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModelFamilyTypeDTO>> partialUpdateModelFamilyType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ModelFamilyTypeDTO modelFamilyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModelFamilyType partially : {}, {}", id, modelFamilyTypeDTO);
        if (modelFamilyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelFamilyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelFamilyTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModelFamilyTypeDTO> result = modelFamilyTypeService.partialUpdate(modelFamilyTypeDTO);

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
     * {@code GET  /model-family-types} : get all the modelFamilyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelFamilyTypes in body.
     */
    @GetMapping(value = "/model-family-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ModelFamilyTypeDTO>> getAllModelFamilyTypes() {
        log.debug("REST request to get all ModelFamilyTypes");
        return modelFamilyTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /model-family-types} : get all the modelFamilyTypes as a stream.
     * @return the {@link Flux} of modelFamilyTypes.
     */
    @GetMapping(value = "/model-family-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModelFamilyTypeDTO> getAllModelFamilyTypesAsStream() {
        log.debug("REST request to get all ModelFamilyTypes as a stream");
        return modelFamilyTypeService.findAll();
    }

    /**
     * {@code GET  /model-family-types/:id} : get the "id" modelFamilyType.
     *
     * @param id the id of the modelFamilyTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelFamilyTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/model-family-types/{id}")
    public Mono<ResponseEntity<ModelFamilyTypeDTO>> getModelFamilyType(@PathVariable UUID id) {
        log.debug("REST request to get ModelFamilyType : {}", id);
        Mono<ModelFamilyTypeDTO> modelFamilyTypeDTO = modelFamilyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelFamilyTypeDTO);
    }

    /**
     * {@code DELETE  /model-family-types/:id} : delete the "id" modelFamilyType.
     *
     * @param id the id of the modelFamilyTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/model-family-types/{id}")
    public Mono<ResponseEntity<Void>> deleteModelFamilyType(@PathVariable UUID id) {
        log.debug("REST request to delete ModelFamilyType : {}", id);
        return modelFamilyTypeService
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
