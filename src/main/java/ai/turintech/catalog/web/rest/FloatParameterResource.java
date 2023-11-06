package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.FloatParameterService;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
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
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.FloatParameter}.
 */
@RestController
@RequestMapping("/api")
public class FloatParameterResource {

    private final Logger log = LoggerFactory.getLogger(FloatParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogFloatParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private FloatParameterService floatParameterService;

    @Autowired
    private FloatParameterRepository floatParameterRepository;


    /**
     * {@code POST  /float-parameters} : Create a new floatParameter.
     *
     * @param floatParameterDTO the floatParameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new floatParameterDTO, or with status {@code 400 (Bad Request)} if the floatParameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/float-parameters")
    public Mono<ResponseEntity<FloatParameterDTO>> createFloatParameter(@RequestBody FloatParameterDTO floatParameterDTO)
            throws URISyntaxException {
        log.debug("REST request to save FloatParameter : {}", floatParameterDTO);
        if (floatParameterDTO.getParameterTypeDefinitionId() != null) {
            throw new BadRequestAlertException("A new floatParameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mono<FloatParameterDTO> result = floatParameterService.save(floatParameterDTO);
        return result
                .map(
                        newentity -> {
                            try {
                                return ResponseEntity
                                        .created(new URI("/api/float-parameters/" + newentity.getParameterTypeDefinitionId()))
                                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, newentity.getParameterTypeDefinitionId().toString()))
                                        .body(newentity);
                            } catch (URISyntaxException e) {
                                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                            }
                        }
                );
    }

    /**
     * {@code PUT  /float-parameters/:id} : Updates an existing floatParameter.
     *
     * @param id                the id of the floatParameterDTO to save.
     * @param floatParameterDTO the floatParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floatParameterDTO,
     * or with status {@code 400 (Bad Request)} if the floatParameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the floatParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/float-parameters/{id}")
    public Mono<ResponseEntity<FloatParameterDTO>> updateFloatParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody FloatParameterDTO floatParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FloatParameter : {}, {}", id, floatParameterDTO);
        if (floatParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floatParameterDTO.getParameterTypeDefinitionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floatParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<FloatParameterDTO> result = floatParameterService.update(floatParameterDTO);
        return result.map((updatedBooleanParameterDTO) ->
                ResponseEntity
                        .ok()
                        .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updatedBooleanParameterDTO.getParameterTypeDefinitionId().toString()))
                        .body(updatedBooleanParameterDTO)
        );
    }

    /**
     * {@code PATCH  /float-parameters/:id} : Partial updates given fields of an existing floatParameter, field will ignore if it is null
     *
     * @param id                the id of the floatParameterDTO to save.
     * @param floatParameterDTO the floatParameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated floatParameterDTO,
     * or with status {@code 400 (Bad Request)} if the floatParameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the floatParameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the floatParameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/float-parameters/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public Mono<ResponseEntity<FloatParameterDTO>> partialUpdateFloatParameter(
            @PathVariable(value = "id", required = false) final UUID id,
            @RequestBody FloatParameterDTO floatParameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FloatParameter partially : {}, {}", id, floatParameterDTO);
        if (floatParameterDTO.getParameterTypeDefinitionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, floatParameterDTO.getParameterTypeDefinitionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!floatParameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<Optional<FloatParameterDTO>> result = floatParameterService.partialUpdate(floatParameterDTO);

        return result.flatMap(updatedFloatParameterOptional -> {
            if (updatedFloatParameterOptional.isPresent()) {
                FloatParameterDTO updatedFloatParameterDTO = updatedFloatParameterOptional.get();
                String idString = Optional
                        .ofNullable(updatedFloatParameterDTO.getParameterTypeDefinitionId())
                        .map(UUID::toString)
                        .orElse(null);

                if (idString == null) throw new RuntimeException("ID cannot be null after update");

                return Mono.just(
                        ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idString))
                                .body(updatedFloatParameterDTO)
                );
            } else {
                return Mono.just(ResponseEntity.notFound().build());
            }
        });
    }

    /**
     * {@code GET  /float-parameters} : get all the floatParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of floatParameters in body.
     */
    @GetMapping(value = "/float-parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<FloatParameterDTO>> getAllFloatParameters() {
        log.debug("REST request to get all FloatParameters");
        return floatParameterService.findAll();
    }

    /**
     * {@code GET  /float-parameters} : get all the floatParameters as a stream.
     *
     * @return the {@link Flux} of floatParameters.
     */
    @GetMapping(value = "/float-parameters", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FloatParameterDTO> getAllFloatParametersAsStream() {
        log.debug("REST request to get all FloatParameters as a stream");
        return floatParameterService.findAllStream();
    }

    /**
     * {@code GET  /float-parameters/:id} : get the "id" floatParameter.
     *
     * @param id the id of the floatParameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the floatParameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/float-parameters/{id}")
    public Mono<ResponseEntity<FloatParameterDTO>> getFloatParameter(@PathVariable UUID id) {
        log.debug("REST request to get FloatParameter : {}", id);
        Mono<FloatParameterDTO> floatParameterDTO = floatParameterService.findOne(id);
        return floatParameterDTO
                .map((response) -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * {@code DELETE  /float-parameters/:id} : delete the "id" floatParameter.
     *
     * @param id the id of the floatParameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/float-parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteFloatParameter(@PathVariable UUID id) {
        log.debug("REST request to delete FloatParameter : {}", id);
        floatParameterService.delete(id);
        return Mono.just(ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build());
    }
}