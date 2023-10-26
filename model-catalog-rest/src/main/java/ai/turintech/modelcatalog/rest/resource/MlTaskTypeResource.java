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

import ai.turintech.modelcatalog.facade.MlTaskTypeFacade;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.rest.support.reactive.ResponseUtil;
import ai.turintech.modelcatalog.to.MlTaskTypeTO;
import ai.turintech.modelcatalog.todtomapper.MlTaskTypeMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.MlTaskType}.
 */
@RestController
@RequestMapping("/api")
public class MlTaskTypeResource {

    private final Logger log = LoggerFactory.getLogger(MlTaskTypeResource.class);

    private static final String ENTITY_NAME = "modelCatalogMlTaskType";

    @Value("${jhipster.clientApp.name:'modelCatalogApp'}")
    private String applicationName;

    private final MlTaskTypeFacade mlTaskTypeFacade;
    
    private final MlTaskTypeMapper mlTaskTypeMapper;


    public MlTaskTypeResource(MlTaskTypeFacade mlTaskTypeFacade, MlTaskTypeMapper mlTaskTypeMapper) {
        this.mlTaskTypeFacade = mlTaskTypeFacade;
        this.mlTaskTypeMapper = mlTaskTypeMapper;
    }

    /**
     * {@code POST  /ml-task-types} : Create a new mlTaskType.
     *
     * @param mlTaskTypeDTO the mlTaskTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mlTaskTypeDTO, or with status {@code 400 (Bad Request)} if the mlTaskType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ml-task-types")
    public Mono<ResponseEntity<MlTaskTypeTO>> createMlTaskType(@Valid @RequestBody MlTaskTypeTO mlTaskTypeTO) throws URISyntaxException {
        log.debug("REST request to save MlTaskType : {}", mlTaskTypeTO);
        if (mlTaskTypeTO.getId() != null) {
            throw new BadRequestAlertException("A new mlTaskType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        mlTaskTypeTO.setId(UUID.randomUUID());
        return mlTaskTypeFacade
            .save(mlTaskTypeMapper.toDto(mlTaskTypeTO)).map(mlTaskTypeMapper::toTo)
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
    public Mono<ResponseEntity<MlTaskTypeTO>> updateMlTaskType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody MlTaskTypeTO mlTaskTypeTO
    ) throws URISyntaxException {
        log.debug("REST request to update MlTaskType : {}, {}", id, mlTaskTypeTO);
        if (mlTaskTypeTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlTaskTypeTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mlTaskTypeFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return mlTaskTypeFacade
                    .update(mlTaskTypeMapper.toDto(mlTaskTypeTO)).map(mlTaskTypeMapper::toTo)
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
    public Mono<ResponseEntity<MlTaskTypeTO>> partialUpdateMlTaskType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody MlTaskTypeTO mlTaskTypeTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MlTaskType partially : {}, {}", id, mlTaskTypeTO);
        if (mlTaskTypeTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mlTaskTypeTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return mlTaskTypeFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MlTaskTypeTO> result = mlTaskTypeFacade.partialUpdate(mlTaskTypeMapper.toDto(mlTaskTypeTO)).map(mlTaskTypeMapper::toTo);

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
    public Mono<List<MlTaskTypeTO>> getAllMlTaskTypes() {
        log.debug("REST request to get all MlTaskTypes");
        return mlTaskTypeFacade.findAll().collectList().map(mlTaskTypeMapper::toTo);
    }

    /**
     * {@code GET  /ml-task-types} : get all the mlTaskTypes as a stream.
     * @return the {@link Flux} of mlTaskTypes.
     */
    @GetMapping(value = "/ml-task-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MlTaskTypeTO> getAllMlTaskTypesAsStream() {
        log.debug("REST request to get all MlTaskTypes as a stream");
        return mlTaskTypeFacade.findAll().map(mlTaskTypeMapper::toTo);
    }

    /**
     * {@code GET  /ml-task-types/:id} : get the "id" mlTaskType.
     *
     * @param id the id of the mlTaskTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mlTaskTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ml-task-types/{id}")
    public Mono<ResponseEntity<MlTaskTypeTO>> getMlTaskType(@PathVariable UUID id) {
        log.debug("REST request to get MlTaskType : {}", id);
        Mono<MlTaskTypeTO> mlTaskTypeTO = mlTaskTypeFacade.findOne(id).map(mlTaskTypeMapper::toTo);
        return ResponseUtil.wrapOrNotFound(mlTaskTypeTO);
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
        return mlTaskTypeFacade
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