package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.ModelService;
import ai.turintech.catalog.service.dto.ModelDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.Model}.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "modelCatalogModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelService modelService;

    private final ModelRepository modelRepository;

    public ModelResource(ModelService modelService, ModelRepository modelRepository) {
        this.modelService = modelService;
        this.modelRepository = modelRepository;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param modelDTO the modelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelDTO, or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/models")
    public Mono<ResponseEntity<ModelDTO>> createModel(@Valid @RequestBody ModelDTO modelDTO) throws URISyntaxException {
        log.debug("REST request to save Model : {}", modelDTO);
        if (modelDTO.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelDTO.setId(UUID.randomUUID());
        return modelService
            .save(modelDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/models/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /models/:id} : Updates an existing model.
     *
     * @param id the id of the modelDTO to save.
     * @param modelDTO the modelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelDTO,
     * or with status {@code 400 (Bad Request)} if the modelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/models/{id}")
    public Mono<ResponseEntity<ModelDTO>> updateModel(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ModelDTO modelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Model : {}, {}", id, modelDTO);
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return modelService
                    .update(modelDTO)
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
     * {@code PATCH  /models/:id} : Partial updates given fields of an existing model, field will ignore if it is null
     *
     * @param id the id of the modelDTO to save.
     * @param modelDTO the modelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelDTO,
     * or with status {@code 400 (Bad Request)} if the modelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ModelDTO>> partialUpdateModel(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ModelDTO modelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Model partially : {}, {}", id, modelDTO);
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return modelRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ModelDTO> result = modelService.partialUpdate(modelDTO);

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
     * {@code GET  /models} : get all the models.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ModelDTO>>> getAllModels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Models");
        return modelService
            .countAll()
            .zipWith(modelService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /models/:id} : get the "id" model.
     *
     * @param id the id of the modelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/models/{id}")
    public Mono<ResponseEntity<ModelDTO>> getModel(@PathVariable UUID id) {
        log.debug("REST request to get Model : {}", id);
        Mono<ModelDTO> modelDTO = modelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelDTO);
    }

    /**
     * {@code DELETE  /models/:id} : delete the "id" model.
     *
     * @param id the id of the modelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/models/{id}")
    public Mono<ResponseEntity<Void>> deleteModel(@PathVariable UUID id) {
        log.debug("REST request to delete Model : {}", id);
        return modelService
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
