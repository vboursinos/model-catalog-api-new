package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.ModelService;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelPaginatedListDTO;
import ai.turintech.catalog.service.dto.FilterDTO;
import ai.turintech.catalog.service.dto.SearchDTO;
import ai.turintech.catalog.utils.PaginationConverter;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private PaginationConverter paginationConverter;
    @Autowired
    private ModelService modelService;
//    @Autowired
//    private ModelRepository modelRepository;

//    public ModelResource(ModelService modelService, ModelRepository modelRepository) {
//        this.modelService = modelService;
//        this.modelRepository = modelRepository;
//    }

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
        ModelDTO result = modelService.save(modelDTO);
        return Mono.just(ResponseEntity
                .created(new URI("/api/models/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result));
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

//        if (!modelRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }

        ModelDTO result = modelService.update(modelDTO);
        return Mono.just(ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelDTO.getId().toString()))
                .body(result));
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

//        if (!modelRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }

        Optional<ModelDTO> result = modelService.partialUpdate(modelDTO);

        return Mono.justOrEmpty(tech.jhipster.web.util.ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, modelDTO.getId().toString()))
        );
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     */
//    @GetMapping(value = "/models", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<ResponseEntity<ModelPaginatedListDTO>> getAllModels(
//            @ParameterObject Pageable pageable,
//            ServerHttpRequest request,
//            @RequestParam(required = false, defaultValue = "false") boolean eagerload,
//            FilterDTO filterDTO,
//            @RequestParam(value = "search", required = false) String search
//    ) {
//        log.debug("REST request to get a page of Models");
//        List<SearchDTO> searchParams = new ArrayList<SearchDTO>();
//        if (search != null) {
//            Pattern pattern = Pattern.compile("(\\w+)(:)([^,]+),?");
//            Matcher matcher = pattern.matcher(search);
//
//            while (matcher.find()) {
//                searchParams.add(new SearchDTO(matcher.group(1), matcher.group(2), matcher.group(3)));
//            }
//        }
//        return modelService
//                .countAll(filterDTO, searchParams)
//                .zipWith(modelService.findAll(pageable, filterDTO, searchParams).collectList())
//                .map(countWithEntities -> {
//                    ModelPaginatedListDTO paginatedList = paginationConverter.getPaginatedList(
//                            countWithEntities.getT2(),
//                            pageable.getPageNumber(),
//                            pageable.getPageSize(),
//                            countWithEntities.getT1()
//                    );
//
//                    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
//                            UriComponentsBuilder.fromHttpRequest(request),
//                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1()));
//
//                    return ResponseEntity.ok().headers(headers).body(paginatedList);
//                });
//    }

    @GetMapping("")
    public ResponseEntity<List<ModelDTO>> getAllModels(
            @org.springdoc.core.annotations.ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Models");
        Page<ModelDTO> page;
        if (eagerload) {
            page = modelService.findAllWithEagerRelationships(pageable);
        } else {
            page = modelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
        Optional<ModelDTO> modelDTO = modelService.findOne(id);
        return Mono.justOrEmpty(ResponseUtil.wrapOrNotFound(modelDTO));
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
