package ai.turintech.modelcatalog.rest.resource;

import ai.turintech.modelcatalog.dto.FilterDTO;
import ai.turintech.modelcatalog.dto.ModelDTO;
import ai.turintech.modelcatalog.dto.ModelPaginatedListDTO;
import ai.turintech.modelcatalog.dto.SearchDTO;
import ai.turintech.modelcatalog.repository.ModelRepository;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.service.ModelService;
import ai.turintech.modelcatalog.service.PaginationConverter;
import ai.turintech.modelcatalog.entity.Model;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * REST controller for managing {@link Model}.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "modelCatalogModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private PaginationConverter paginationConverter;
    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

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

        return modelService.save(modelDTO)
                .flatMap(result -> {
                    try {
                        return Mono.just(ResponseEntity
                                .created(new URI("/api/models/" + result.getId()))
                                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                                .body(result));
                    } catch (URISyntaxException e) {
                        // Handle URI creation exception here
                        return Mono.error(e);
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
    ) throws Exception {
        log.debug("REST request to update Model : {}, {}", id, modelDTO);
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return modelService.findOne(id)
                .flatMap(existingModel -> {
                    if (existingModel == null) {
                        throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
                    }

                    if (!Objects.equals(id, modelDTO.getId())) {
                        throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
                    }

                    return modelService.update(modelDTO)
                            .flatMap(result -> {
                                return Mono.just(ResponseEntity
                                        .ok()
                                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelDTO.getId().toString()))
                                        .body(result));
                            });
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
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        return modelService.partialUpdate(modelDTO)
                .flatMap(result -> {
                    if (!Objects.equals(id, result.getId())) {
                        throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
                    }
                    return Mono.just(ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelDTO.getId().toString()))
                            .body(result));
                });
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ModelPaginatedListDTO>> getAllModels(
            @ParameterObject Pageable pageable,
            ServerHttpRequest request,
            @RequestParam(required = false, defaultValue = "false") boolean eagerload,
            FilterDTO filterDTO,
            @RequestParam(value = "search", required = false) String search
    ) {
        log.debug("REST request to get a page of Models");
        List<SearchDTO> searchParams = new ArrayList<SearchDTO>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+)(:)([^,]+),?");
            Matcher matcher = pattern.matcher(search);

            while (matcher.find()) {
                searchParams.add(new SearchDTO(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        return modelService.findAll(pageable)
                .map(modelPaginatedListDTO -> ResponseEntity.ok().body(modelPaginatedListDTO))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(Exception.class, ex -> {
                    // Handle exceptions here and return an appropriate response
                    log.error("Error while fetching models: " + ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * {@code GET  /models/:id} : get the "id" model.
     *
     * @param id the id of the modelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/models/{id}")
    @Transactional(readOnly = true, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Mono<ResponseEntity<ModelDTO>> getModel(@PathVariable UUID id) throws Exception {
        log.debug("REST request to get Model : {}", id);

        Mono<ModelDTO> modelMono = modelService.findOne(id);

        return modelMono.map(modelDTO -> ResponseEntity.ok().body(modelDTO))
                .defaultIfEmpty(ResponseEntity.notFound().build());
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
        modelService.delete(id);
        return Mono.justOrEmpty(ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build());
    }
}
