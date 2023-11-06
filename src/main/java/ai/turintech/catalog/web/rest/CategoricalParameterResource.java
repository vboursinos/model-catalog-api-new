package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.CategoricalParameterService;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
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
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.CategoricalParameter}.
 */
@RestController
@RequestMapping("/api")
public class CategoricalParameterResource {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogCategoricalParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CategoricalParameterService categoricalParameterService;

    @Autowired
    private CategoricalParameterRepository categoricalParameterRepository;


    /**
     * {@code POST  /categorical-parameters} : Create a new categoricalParameter.
     *
     * @param categoricalParameterDTO the categoricalParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoricalParameterDTO, or with status {@code 400 (Bad Request)} if the categoricalParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorical-parameters")
    public Mono<ResponseEntity<CategoricalParameterDTO>> createCategoricalParameter(
            @RequestBody CategoricalParameterDTO categoricalParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CategoricalParameter : {}", categoricalParameterDTO);
        if (categoricalParameterDTO.getParameterTypeDefinitionId() != null) {
            throw new BadRequestAlertException("A new categoricalParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mono<CategoricalParameterDTO> result = categoricalParameterService.save(categoricalParameterDTO);
        return result
                .map(
                        newentity -> {
                            try {
                                return ResponseEntity
                                        .created(new URI("/api/categorical-parameters/" + newentity.getParameterTypeDefinitionId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, newentity.getParameterTypeDefinitionId().toString()))
                                        .body(newentity);
                            } catch (URISyntaxException e) {
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                            }
                        }
                );
    }

    /**
     * {@code PUT  /categorical-parameters/:id} : Updates an existing categoricalParameter.
     *
     * @param id                      the id of the categoricalParameterDTO to save.
     * @param categoricalParameterDTO the categoricalParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoricalParameterDTO,
     * or with status {@code 400 (Bad Request)} if the categoricalParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoricalParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorical-parameters/{id}")
    public Mono<ResponseEntity<CategoricalParameterDTO>> updateCategoricalParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody CategoricalParameterDTO categoricalParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoricalParameter : {}, {}", id, categoricalParameterDTO);
        if (categoricalParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoricalParameterDTO.getParameterTypeDefinitionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoricalParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<CategoricalParameterDTO> result = categoricalParameterService.update(categoricalParameterDTO);
        return result.map((updatedBooleanParameterDTO) ->
                ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updatedBooleanParameterDTO.getParameterTypeDefinitionId().toString()))
                        .body(updatedBooleanParameterDTO)
        );
    }

    /**
     * {@code PATCH  /categorical-parameters/:id} : Partial updates given fields of an existing categoricalParameter, field will ignore if it is null
     *
     * @param id                      the id of the categoricalParameterDTO to save.
     * @param categoricalParameterDTO the categoricalParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoricalParameterDTO,
     * or with status {@code 400 (Bad Request)} if the categoricalParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoricalParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoricalParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorical-parameters/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<CategoricalParameterDTO>> partialUpdateCategoricalParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody CategoricalParameterDTO categoricalParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoricalParameter partially : {}, {}", id, categoricalParameterDTO);
        if (categoricalParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoricalParameterDTO.getParameterTypeDefinitionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoricalParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<CategoricalParameterDTO> result = categoricalParameterService.partialUpdate(categoricalParameterDTO);

        return result.map((updatedCategoricalParameterDTO) ->
                ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updatedCategoricalParameterDTO.getParameterTypeDefinitionId().toString()))
                        .body(updatedCategoricalParameterDTO)
        );
    }

    /**
     * {@code GET  /categorical-parameters} : get all the categoricalParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoricalParameters in body.
     */
    @GetMapping(value = "/categorical-parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<CategoricalParameterDTO>> getAllCategoricalParameters() {
        log.debug("REST request to get all CategoricalParameters");
        return categoricalParameterService.findAll();
    }

    /**
     * {@code GET  /categorical-parameters} : get all the categoricalParameters as a stream.
     * @return the {@link Flux} of categoricalParameters.
     */
    @GetMapping(value = "/categorical-parameters", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CategoricalParameterDTO> getAllCategoricalParametersAsStream() {
        log.debug("REST request to get all CategoricalParameters as a stream");
        return categoricalParameterService.findAllStream();
    }

    /**
     * {@code GET  /categorical-parameters/:id} : get the "id" categoricalParameter.
     *
     * @param id the id of the categoricalParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoricalParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorical-parameters/{id}")
    public Mono<ResponseEntity<CategoricalParameterDTO>> getCategoricalParameter(@PathVariable UUID id) {
        log.debug("REST request to get CategoricalParameter : {}", id);
        Mono<CategoricalParameterDTO> categoricalParameterDTO = categoricalParameterService.findOne(id);
        return categoricalParameterDTO
                .map((response) -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * {@code DELETE  /categorical-parameters/:id} : delete the "id" categoricalParameter.
     *
     * @param id the id of the categoricalParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorical-parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteCategoricalParameter(@PathVariable UUID id) {
        log.debug("REST request to delete CategoricalParameter : {}", id);
        categoricalParameterService.delete(id);
        return Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build());
    }
}
