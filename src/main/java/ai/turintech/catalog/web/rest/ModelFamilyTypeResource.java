package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.ModelFamilyTypeService;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Autowired
    private ModelFamilyTypeService modelFamilyTypeService;

    @Autowired
    private ModelFamilyTypeRepository modelFamilyTypeRepository;

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
        Mono<ModelFamilyTypeDTO> result = modelFamilyTypeService.save(modelFamilyTypeDTO);
        return result
                .map(
                        res ->
                                ResponseEntity
                                        .created(URI.create("/api/model-family-types/" + res.getId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                        .body(res)
                );
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

        if (!modelFamilyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ModelFamilyTypeDTO> result = modelFamilyTypeService.update(modelFamilyTypeDTO);
        return result
                .map(
                        res ->
                                ResponseEntity
                                        .ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                        .body(res)
                );
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

        if (!modelFamilyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ModelFamilyTypeDTO> result = modelFamilyTypeService.partialUpdate(modelFamilyTypeDTO);

        return result
                .map(
                        res ->
                                ResponseEntity
                                        .ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                        .body(res)
                );
    }

    /**
     * {@code GET  /model-family-types} : get all the modelFamilyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelFamilyTypes in body.
     */
    @GetMapping(value = "/model-family-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ModelFamilyTypeDTO>> getAllModelFamilyTypes() {
        log.debug("REST request to get all ModelFamilyTypes");
        return modelFamilyTypeService.findAll();
    }

    /**
     * {@code GET  /model-family-types} : get all the modelFamilyTypes as a stream.
     * @return the {@link Flux} of modelFamilyTypes.
     */
    @GetMapping(value = "/model-family-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModelFamilyTypeDTO> getAllModelFamilyTypesAsStream() {
        log.debug("REST request to get all ModelFamilyTypes as a stream");
        return modelFamilyTypeService.findAllStream();
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
        return modelFamilyTypeDTO.map(
                modelGroupType -> ResponseEntity.ok().body(modelGroupType)
        );
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
        modelFamilyTypeService.delete(id);
        return Mono.just(
                ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
        );
    }
}
