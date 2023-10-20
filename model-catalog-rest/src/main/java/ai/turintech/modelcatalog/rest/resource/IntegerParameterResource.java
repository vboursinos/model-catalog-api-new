package ai.turintech.modelcatalog.rest.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

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

import ai.turintech.modelcatalog.dto.IntegerParameterDTO;
import ai.turintech.modelcatalog.facade.IntegerParameterFacade;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.rest.support.reactive.ResponseUtil;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.IntegerParameter}.
 */
@RestController
@RequestMapping("/api")
public class IntegerParameterResource {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogIntegerParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IntegerParameterFacade integerParameterFacade;

    public IntegerParameterResource(IntegerParameterFacade integerParameterFacade) {
        this.integerParameterFacade = integerParameterFacade;
    }

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
        if (integerParameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new integerParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return integerParameterFacade
            .save(integerParameterDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/integer-parameters/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /integer-parameters/:id} : Updates an existing integerParameter.
     *
     * @param id the id of the integerParameterDTO to save.
     * @param integerParameterDTO the integerParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integerParameterDTO,
     * or with status {@code 400 (Bad Request)} if the integerParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the integerParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<IntegerParameterDTO>> updateIntegerParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IntegerParameterDTO integerParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IntegerParameter : {}, {}", id, integerParameterDTO);
        if (integerParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integerParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return integerParameterFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return integerParameterFacade
                    .update(integerParameterDTO)
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
     * {@code PATCH  /integer-parameters/:id} : Partial updates given fields of an existing integerParameter, field will ignore if it is null
     *
     * @param id the id of the integerParameterDTO to save.
     * @param integerParameterDTO the integerParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated integerParameterDTO,
     * or with status {@code 400 (Bad Request)} if the integerParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the integerParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the integerParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/integer-parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IntegerParameterDTO>> partialUpdateIntegerParameter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IntegerParameterDTO integerParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IntegerParameter partially : {}, {}", id, integerParameterDTO);
        if (integerParameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, integerParameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return integerParameterFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IntegerParameterDTO> result = integerParameterFacade.partialUpdate(integerParameterDTO);

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
     * {@code GET  /integer-parameters} : get all the integerParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of integerParameters in body.
     */
    @GetMapping(value = "/integer-parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<IntegerParameterDTO>> getAllIntegerParameters() {
        log.debug("REST request to get all IntegerParameters");
        return integerParameterFacade.findAll().collectList();
    }

    /**
     * {@code GET  /integer-parameters} : get all the integerParameters as a stream.
     * @return the {@link Flux} of integerParameters.
     */
    @GetMapping(value = "/integer-parameters", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<IntegerParameterDTO> getAllIntegerParametersAsStream() {
        log.debug("REST request to get all IntegerParameters as a stream");
        return integerParameterFacade.findAll();
    }

    /**
     * {@code GET  /integer-parameters/:id} : get the "id" integerParameter.
     *
     * @param id the id of the integerParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the integerParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<IntegerParameterDTO>> getIntegerParameter(@PathVariable Long id) {
        log.debug("REST request to get IntegerParameter : {}", id);
        Mono<IntegerParameterDTO> integerParameterDTO = integerParameterFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(integerParameterDTO);
    }

    /**
     * {@code DELETE  /integer-parameters/:id} : delete the "id" integerParameter.
     *
     * @param id the id of the integerParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/integer-parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteIntegerParameter(@PathVariable Long id) {
        log.debug("REST request to delete IntegerParameter : {}", id);
        return integerParameterFacade
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
