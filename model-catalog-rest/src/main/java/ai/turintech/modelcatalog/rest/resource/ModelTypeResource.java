package ai.turintech.modelcatalog.rest.resource;

import ai.turintech.modelcatalog.dto.ModelTypeDTO;
import ai.turintech.modelcatalog.repository.ModelTypeRepository;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.service.ModelTypeService;
import ai.turintech.modelcatalog.to.ModelTypeTO;
import ai.turintech.modelcatalog.entity.ModelType;
import ai.turintech.modelcatalog.todtomapper.ModelTypeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * REST controller for managing {@link ModelType}.
 */
@RestController
@RequestMapping("/api")
public class ModelTypeResource {

    private final Logger log = LoggerFactory.getLogger(ModelTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogModelType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ModelTypeService modelTypeService;

    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Autowired
    private ModelTypeMapper modelTypeMapper;
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
        Mono<ModelTypeDTO> result = modelTypeService.save(modelTypeDTO);
        return result
            .map(
                res -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/model-types/" + res.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res);
                    } catch (URISyntaxException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
                    }
                }
            );
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
    public Mono<ResponseEntity<ModelTypeTO>> updateModelType(
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

        if (!modelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ModelTypeDTO> result = modelTypeService.update(modelTypeDTO);
        Mono<ModelTypeTO> resultTO = result.map(modelTypeMapper::toTO);
        return resultTO
            .map(
                res -> ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
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

        if (!modelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ModelTypeDTO> result = modelTypeService.partialUpdate(modelTypeDTO);

        return result
            .map(
                res -> ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
    }

    /**
     * {@code GET  /model-types} : get all the modelTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelTypes in body.
     */
    @GetMapping(value = "/model-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ModelTypeTO>>> getAllModelTypes() {
        log.debug("REST request to get all ModelTypes");
        return modelTypeService.findAll()
                .map(modelTypeMapper::toTO)
                .map(modelTypeTOList -> ResponseEntity.ok().body(modelTypeTOList));
    }

    /**
     * {@code GET  /model-types} : get all the modelTypes as a stream.
     * @return the {@link Flux} of modelTypes.
     */
    @GetMapping(value = "/model-types/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModelTypeDTO> getAllModelTypesAsStream() {
        log.debug("REST request to get all ModelTypes as a stream");
        return modelTypeService.findAllStream();
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
        return modelTypeDTO.map(
            modelType -> ResponseEntity.ok().body(modelType)
        );
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
        modelTypeService.delete(id);
        return Mono.justOrEmpty(ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build());
    }
}
