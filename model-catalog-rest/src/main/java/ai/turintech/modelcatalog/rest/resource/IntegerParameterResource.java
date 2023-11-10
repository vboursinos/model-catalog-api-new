package ai.turintech.modelcatalog.rest.resource;

import ai.turintech.modelcatalog.dto.IntegerParameterDTO;
import ai.turintech.modelcatalog.repository.IntegerParameterRepository;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.service.IntegerParameterService;
import ai.turintech.modelcatalog.entity.IntegerParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing {@link IntegerParameter}.
 */
@RestController
@RequestMapping("/api")
public class IntegerParameterResource {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogIntegerParameter";

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private IntegerParameterService integerParameterService;
    @Autowired
    private IntegerParameterRepository integerParameterRepository;

    /**
     * {@code POST  /integer-parameters} : Create a new integerParameter.
     *
     * @param integerParameterDTO the integerParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new integerParameterDTO, or with status {@code 400 (Bad Request)} if the integerParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/integer-parameters")
    public Mono<ResponseEntity<IntegerParameterDTO>> createIntegerParameter(@RequestBody IntegerParameterDTO integerParameterDTO)
            throws URISyntaxException {
        log.debug("REST request to save IntegerParameter : {}", integerParameterDTO);
        if (integerParameterDTO.getParameterTypeDefinitionId() != null) {
            throw new BadRequestAlertException("A new integerParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mono<IntegerParameterDTO> result = integerParameterService.save(integerParameterDTO);
        return result.flatMap(
                integerParameterDTO1 -> {
                    try {
                        return Mono.just(
                                ResponseEntity
                                        .created(new URI("/api/integer-parameters/" + integerParameterDTO1.getParameterTypeDefinitionId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, integerParameterDTO1.getParameterTypeDefinitionId().toString()))
                                        .body(integerParameterDTO1)
                        );
                    } catch (URISyntaxException e) {
                        return Mono.error(e);
                    }
                }
        );
    }

    /**
     * {@code PUT  /integer-parameters/:id} : Updates an existing integerParameter.
     *
     * @param id                  the id of the integerParameterDTO to save.
     * @param integerParameterDTO the integerParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integerParameterDTO,
     * or with status {@code 400 (Bad Request)} if the integerParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the integerParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<IntegerParameterDTO>> updateIntegerParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody IntegerParameterDTO integerParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IntegerParameter : {}, {}", id, integerParameterDTO);
        if (integerParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integerParameterDTO.getIntegerParameterValues())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!integerParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<IntegerParameterDTO> result = integerParameterService.update(integerParameterDTO);
        return result.flatMap(
                integerParameterDTO1 -> {
                    return Mono.just(
                            ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, integerParameterDTO1.getParameterTypeDefinitionId().toString()))
                                    .body(integerParameterDTO1)
                    );
                }
        );
    }

    /**
     * {@code PATCH  /integer-parameters/:id} : Partial updates given fields of an existing integerParameter, field will ignore if it is null
     *
     * @param id                  the id of the integerParameterDTO to save.
     * @param integerParameterDTO the integerParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integerParameterDTO,
     * or with status {@code 400 (Bad Request)} if the integerParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the integerParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the integerParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/integer-parameters/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<IntegerParameterDTO>> partialUpdateIntegerParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody IntegerParameterDTO integerParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IntegerParameter partially : {}, {}", id, integerParameterDTO);
        if (integerParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integerParameterDTO.getParameterTypeDefinitionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!integerParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<IntegerParameterDTO> result = integerParameterService.partialUpdate(integerParameterDTO);

        return result.flatMap(
                integerParameterDTO1 -> Mono.just(
                        ResponseEntity
                                .ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, integerParameterDTO1.getParameterTypeDefinitionId().toString()))
                                .body(integerParameterDTO1)
                )
        );
    }

    /**
     * {@code GET  /integer-parameters} : get all the integerParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of integerParameters in body.
     */
    @GetMapping(value = "/integer-parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<IntegerParameterDTO>>> getAllIntegerParameters() {
        log.debug("REST request to get all IntegerParameters");
        return integerParameterService.findAll().map(
                integerParameterDTOS -> new ResponseEntity<>(integerParameterDTOS, HttpStatus.OK)
        );
    }

    /**
     * {@code GET  /integer-parameters} : get all the integerParameters as a stream.
     *
     * @return the {@link Flux} of integerParameters.
     */
    @GetMapping(value = "/integer-parameters/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IntegerParameterDTO> getAllIntegerParametersAsStream() {
        log.debug("REST request to get all IntegerParameters as a stream");
        return integerParameterService.findAllStream();
    }

    /**
     * {@code GET  /integer-parameters/:id} : get the "id" integerParameter.
     *
     * @param id the id of the integerParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the integerParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<IntegerParameterDTO>> getIntegerParameter(@PathVariable UUID id) {
        log.debug("REST request to get IntegerParameter : {}", id);
        Mono<IntegerParameterDTO> integerParameterDTO = integerParameterService.findOne(id);
        return integerParameterDTO
                .map((response) -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * {@code DELETE  /integer-parameters/:id} : delete the "id" integerParameter.
     *
     * @param id the id of the integerParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteIntegerParameter(@PathVariable UUID id) {
        log.debug("REST request to delete IntegerParameter : {}", id);
        return integerParameterService.delete(id)
                .map((response) -> ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                );
    }
}
