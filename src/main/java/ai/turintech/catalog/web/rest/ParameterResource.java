package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.ParameterService;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.Parameter}.
 */
@RestController
@RequestMapping("/api")
public class ParameterResource {

    private final Logger log = LoggerFactory.getLogger(ParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogParameter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private ParameterRepository parameterRepository;

    /**
     * {@code POST  /parameters} : Create a new parameter.
     *
     * @param parameterDTO the parameterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parameterDTO, or with status {@code 400 (Bad Request)} if the parameter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parameters")
    public Mono<ResponseEntity<ParameterDTO>> createParameter(@Valid @RequestBody ParameterDTO parameterDTO) throws URISyntaxException {
        log.debug("REST request to save Parameter : {}", parameterDTO);
        if (parameterDTO.getId() != null) {
            throw new BadRequestAlertException("A new parameter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mono<ParameterDTO> result = parameterService.save(parameterDTO);
        return result
            .map(
                res -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/parameters/" + res.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res);
                    } catch (URISyntaxException e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                }
            );
    }

    /**
     * {@code PUT  /parameters/:id} : Updates an existing parameter.
     *
     * @param id the id of the parameterDTO to save.
     * @param parameterDTO the parameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterDTO,
     * or with status {@code 400 (Bad Request)} if the parameterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parameters/{id}")
    public Mono<ResponseEntity<ParameterDTO>> updateParameter(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody ParameterDTO parameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Parameter : {}, {}", id, parameterDTO);
        if (parameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<ParameterDTO> result = parameterService.update(parameterDTO);
        return result
            .map(
                res -> ResponseEntity
                    .ok()
                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                    .body(res)
            );
    }

    /**
     * {@code PATCH  /parameters/:id} : Partial updates given fields of an existing parameter, field will ignore if it is null
     *
     * @param id the id of the parameterDTO to save.
     * @param parameterDTO the parameterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parameterDTO,
     * or with status {@code 400 (Bad Request)} if the parameterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the parameterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the parameterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parameters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ParameterDTO>> partialUpdateParameter(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody ParameterDTO parameterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parameter partially : {}, {}", id, parameterDTO);
        if (parameterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parameterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parameterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mono<Optional<ParameterDTO>> result = parameterService.partialUpdate(parameterDTO);

        return result.flatMap(updatedFloatParameterOptional -> {
            if (updatedFloatParameterOptional.isPresent()) {
                ParameterDTO updatedParameterDTO = updatedFloatParameterOptional.get();
                String idString = Optional
                        .ofNullable(updatedParameterDTO.getId())
                        .map(UUID::toString)
                        .orElse(null);

                if (idString == null) throw new RuntimeException("ID cannot be null after update");

                return Mono.just(
                        ResponseEntity.ok()
                                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, idString))
                                .body(updatedParameterDTO)
                );
            } else {
                return Mono.just(ResponseEntity.notFound().build());
            }
        });
    }

    /**
     * {@code GET  /parameters} : get all the parameters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parameters in body.
     */
    @GetMapping(value = "/parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ParameterDTO>>> getAllParameters(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Parameters");
        return parameterService.findAll(pageable)
                .map(updatedListParameter -> ResponseEntity.ok().body(updatedListParameter))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error while fetching models: " + ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * {@code GET  /parameters/:id} : get the "id" parameter.
     *
     * @param id the id of the parameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parameters/{id}")
    public Mono<ResponseEntity<ParameterDTO>> getParameter(@PathVariable UUID id) {
        log.debug("REST request to get Parameter : {}", id);
        Mono<ParameterDTO> parameterDTO = parameterService.findOne(id);
        return parameterDTO
                .map((response) -> ResponseEntity.ok().body(response))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    /**
     * {@code DELETE  /parameters/:id} : delete the "id" parameter.
     *
     * @param id the id of the parameterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parameters/{id}")
    public Mono<ResponseEntity<Void>> deleteParameter(@PathVariable UUID id) {
        log.debug("REST request to delete Parameter : {}", id);
        return parameterService.delete(id)
                .map((response) -> ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build());
    }
}
