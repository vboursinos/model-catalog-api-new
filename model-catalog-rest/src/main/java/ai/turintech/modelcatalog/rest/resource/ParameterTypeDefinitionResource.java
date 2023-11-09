package ai.turintech.modelcatalog.rest.resource;

import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.repository.ParameterTypeDefinitionRepository;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.service.ParameterTypeDefinitionService;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * REST controller for managing {@link ParameterTypeDefinition}.
 */
@RestController
@RequestMapping("/api")
public class ParameterTypeDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(ParameterTypeDefinitionResource.class);

    private static final String ENTITY_NAME = "modelCatalogParameterTypeDefinition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ParameterTypeDefinitionService parameterTypeDefinitionService;

    @Autowired
    private ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

    /**
     * {@code POST  /parameter-type-definitions} : Create a new parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the parameterTypeDefinitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parameterTypeDefinitionDTO, or with status {@code 400 (Bad Request)} if the parameterTypeDefinition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parameter-type-definitions")
    public Mono<ResponseEntity<ParameterTypeDefinitionDTO>> createParameterTypeDefinition(@Valid @RequestBody ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to save ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        if (parameterTypeDefinitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new parameterTypeDefinition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mono<ParameterTypeDefinitionDTO> result = parameterTypeDefinitionService.save(parameterTypeDefinitionDTO);
        return result.map((ParameterTypeDefinitionDTO savedParameterTypeDefinitionDTO) -> ResponseEntity.created(URI.create("/api/parameter-type-definitions/" + savedParameterTypeDefinitionDTO.getId())).headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, savedParameterTypeDefinitionDTO.getId().toString())).body(savedParameterTypeDefinitionDTO));
    }

    /**
     * {@code PUT  /parameter-type-definitions/:id} : Updates an existing parameterTypeDefinition.
     *
     * @param id                         the id of the parameterTypeDefinitionDTO to save.
     * @param parameterTypeDefinitionDTO the parameterTypeDefinitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterTypeDefinitionDTO,
     * or with status {@code 400 (Bad Request)} if the parameterTypeDefinitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parameterTypeDefinitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parameter-type-definitions/{id}")
    public Mono<ResponseEntity<ParameterTypeDefinitionDTO>> updateParameterTypeDefinition(@PathVariable(value = "id", required = false) final UUID id, @Valid @RequestBody ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to update ParameterTypeDefinition : {}, {}", id, parameterTypeDefinitionDTO);
        if (parameterTypeDefinitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterTypeDefinitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterTypeDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ParameterTypeDefinitionDTO> result = parameterTypeDefinitionService.update(parameterTypeDefinitionDTO);
        return result.map((ParameterTypeDefinitionDTO savedParameterTypeDefinitionDTO) -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, savedParameterTypeDefinitionDTO.getId().toString())).body(savedParameterTypeDefinitionDTO));
    }

    /**
     * {@code PATCH  /parameter-type-definitions/:id} : Partial updates given fields of an existing parameterTypeDefinition, field will ignore if it is null
     *
     * @param id                         the id of the parameterTypeDefinitionDTO to save.
     * @param parameterTypeDefinitionDTO the parameterTypeDefinitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterTypeDefinitionDTO,
     * or with status {@code 400 (Bad Request)} if the parameterTypeDefinitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parameterTypeDefinitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parameterTypeDefinitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parameter-type-definitions/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<ParameterTypeDefinitionDTO>> partialUpdateParameterTypeDefinition(@PathVariable(value = "id", required = false) final UUID id, @NotNull @RequestBody ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) throws URISyntaxException {
        log.debug("REST request to partial update ParameterTypeDefinition partially : {}, {}", id, parameterTypeDefinitionDTO);
        if (parameterTypeDefinitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterTypeDefinitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterTypeDefinitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ParameterTypeDefinitionDTO> result = parameterTypeDefinitionService.partialUpdate(parameterTypeDefinitionDTO);

        return result.map((ParameterTypeDefinitionDTO savedParameterTypeDefinitionDTO) -> ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, savedParameterTypeDefinitionDTO.getId().toString())).body(savedParameterTypeDefinitionDTO));
    }


    /**
     * {@code GET  /parameter-type-definitions} : get all the parameterTypeDefinitions as a stream.
     *
     * @return the {@link Flux} of parameterTypeDefinitions.
     */
    @GetMapping(value = "/parameter-type-definitions/stream", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ParameterTypeDefinitionDTO> getAllParameterTypeDefinitions(@RequestParam(required = false) String filter) {
        if ("integerparameter-is-null".equals(filter)) {
            log.debug("REST request to get all ParameterTypeDefinitions where integerParameter is null");
            return parameterTypeDefinitionService.findAllWhereIntegerParameterIsNull();
        }

        if ("floatparameter-is-null".equals(filter)) {
            log.debug("REST request to get all ParameterTypeDefinitions where floatParameter is null");
            return parameterTypeDefinitionService.findAllWhereFloatParameterIsNull();
        }

        if ("categoricalparameter-is-null".equals(filter)) {
            log.debug("REST request to get all ParameterTypeDefinitions where categoricalParameter is null");
            return parameterTypeDefinitionService.findAllWhereCategoricalParameterIsNull();
        }

        if ("booleanparameter-is-null".equals(filter)) {
            log.debug("REST request to get all ParameterTypeDefinitions where booleanParameter is null");
            return parameterTypeDefinitionService.findAllWhereBooleanParameterIsNull();
        }
        log.debug("REST request to get all ParameterTypeDefinitions");
        return parameterTypeDefinitionService.findAllStream();
    }



    /**
     * {@code GET  /parameter-type-definitions} : get all the parameterTypeDefinitions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parameterTypeDefinitions in body.
     */
    @GetMapping(value = "/parameter-type-definitions", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ResponseEntity<List<ParameterTypeDefinitionDTO>>> getAllParameterTypeDefinitions() {
        log.debug("REST request to get all ParameterTypeDefinitions as a stream");
        return parameterTypeDefinitionService.findAll().map(parameterTypeDefinitions -> ResponseEntity.ok().body(parameterTypeDefinitions));
    }

    /**
     * {@code GET  /parameter-type-definitions/:id} : get the "id" parameterTypeDefinition.
     *
     * @param id the id of the parameterTypeDefinitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parameterTypeDefinitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parameter-type-definitions/{id}")
    public Mono<ResponseEntity<ParameterTypeDefinitionDTO>> getParameterTypeDefinition(@PathVariable UUID id) {
        log.debug("REST request to get ParameterTypeDefinition : {}", id);
        Mono<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTO = parameterTypeDefinitionService.findOne(id);
        return parameterTypeDefinitionDTO.map(parameterTypeDefinition -> ResponseEntity.ok().body(parameterTypeDefinition));
    }

    /**
     * {@code DELETE  /parameter-type-definitions/:id} : delete the "id" parameterTypeDefinition.
     *
     * @param id the id of the parameterTypeDefinitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parameter-type-definitions/{id}")
    public Mono<ResponseEntity<Void>> deleteParameterTypeDefinition(@PathVariable UUID id) {
        log.debug("REST request to delete ParameterTypeDefinition : {}", id);
        parameterTypeDefinitionService.delete(id);
        return Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build());
    }
}
