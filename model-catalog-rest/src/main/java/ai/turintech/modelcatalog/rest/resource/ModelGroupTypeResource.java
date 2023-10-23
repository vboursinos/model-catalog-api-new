package ai.turintech.modelcatalog.rest.resource;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ai.turintech.modelcatalog.dto.ModelGroupTypeDTO;
import ai.turintech.modelcatalog.facade.ModelGroupTypeFacade;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.rest.support.reactive.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.ModelGroupType}.
 */
@RestController
@RequestMapping("/api")
public class ModelGroupTypeResource {

    private final Logger log = LoggerFactory.getLogger(ModelGroupTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogModelGroupType";

    @Value("${jhipster.clientApp.name:'modelCatalogApp'}")
    private String applicationName;

    private final ModelGroupTypeFacade modelGroupTypeFacade;


    public ModelGroupTypeResource(ModelGroupTypeFacade modelGroupTypeFacade) {
        this.modelGroupTypeFacade = modelGroupTypeFacade;
    }

    /**
     * {@code POST  /model-group-types} : Create a new modelGroupType.
     *
     * @param modelGroupTypeDTO the modelGroupTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelGroupTypeDTO, or with status {@code 400 (Bad Request)} if the modelGroupType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/model-group-types")
    public Mono<ResponseEntity<ModelGroupTypeDTO>> createModelGroupType(@Valid @RequestBody ModelGroupTypeDTO modelGroupTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ModelGroupType : {}", modelGroupTypeDTO);
        if (modelGroupTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new modelGroupType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelGroupTypeDTO.setId(UUID.randomUUID());
        return modelGroupTypeFacade
            .save(modelGroupTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/model-group-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /model-group-types/:id} : Updates an existing modelGroupType.
     *
     * @param id the id of the modelGroupTypeDTO to save.
     * @param modelGroupTypeDTO the modelGroupTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelGroupTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelGroupTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelGroupTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/model-group-types/{id}")
    public Mono<ResponseEntity<ModelGroupTypeDTO>> updateModelGroupType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ModelGroupTypeDTO modelGroupTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ModelGroupType : {}, {}", id, modelGroupTypeDTO);
        if (modelGroupTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelGroupTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelGroupTypeFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return modelGroupTypeFacade
                    .update(modelGroupTypeDTO)
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
     * {@code PATCH  /model-group-types/:id} : Partial updates given fields of an existing modelGroupType, field will ignore if it is null
     *
     * @param id the id of the modelGroupTypeDTO to save.
     * @param modelGroupTypeDTO the modelGroupTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelGroupTypeDTO,
     * or with status {@code 400 (Bad Request)} if the modelGroupTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modelGroupTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelGroupTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/model-group-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModelGroupTypeDTO>> partialUpdateModelGroupType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ModelGroupTypeDTO modelGroupTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ModelGroupType partially : {}, {}", id, modelGroupTypeDTO);
        if (modelGroupTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelGroupTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelGroupTypeFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModelGroupTypeDTO> result = modelGroupTypeFacade.partialUpdate(modelGroupTypeDTO);

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
     * {@code GET  /model-group-types} : get all the modelGroupTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of modelGroupTypes in body.
     */
    @GetMapping(value = "/model-group-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<ModelGroupTypeDTO>> getAllModelGroupTypes() {
        log.debug("REST request to get all ModelGroupTypes");
        return modelGroupTypeFacade.findAll().collectList();
    }

    /**
     * {@code GET  /model-group-types} : get all the modelGroupTypes as a stream.
     * @return the {@link Flux} of modelGroupTypes.
     */
    @GetMapping(value = "/model-group-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ModelGroupTypeDTO> getAllModelGroupTypesAsStream() {
        log.debug("REST request to get all ModelGroupTypes as a stream");
        return modelGroupTypeFacade.findAll();
    }

    /**
     * {@code GET  /model-group-types/:id} : get the "id" modelGroupType.
     *
     * @param id the id of the modelGroupTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelGroupTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/model-group-types/{id}")
    public Mono<ResponseEntity<ModelGroupTypeDTO>> getModelGroupType(@PathVariable UUID id) {
        log.debug("REST request to get ModelGroupType : {}", id);
        Mono<ModelGroupTypeDTO> modelGroupTypeDTO = modelGroupTypeFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelGroupTypeDTO);
    }

    /**
     * {@code DELETE  /model-group-types/:id} : delete the "id" modelGroupType.
     *
     * @param id the id of the modelGroupTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/model-group-types/{id}")
    public Mono<ResponseEntity<Void>> deleteModelGroupType(@PathVariable UUID id) {
        log.debug("REST request to delete ModelGroupType : {}", id);
        return modelGroupTypeFacade
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
