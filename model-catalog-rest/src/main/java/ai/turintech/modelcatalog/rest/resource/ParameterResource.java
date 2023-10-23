package ai.turintech.modelcatalog.rest.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
import org.springframework.web.util.UriComponentsBuilder;

import ai.turintech.modelcatalog.dto.ParameterDTO;
import ai.turintech.modelcatalog.facade.ParameterFacade;
import ai.turintech.modelcatalog.rest.errors.BadRequestAlertException;
import ai.turintech.modelcatalog.rest.support.HeaderUtil;
import ai.turintech.modelcatalog.rest.support.PaginationUtil;
import ai.turintech.modelcatalog.rest.support.reactive.ResponseUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.Parameter}.
 */
@RestController
@RequestMapping("/api")
public class ParameterResource {

    private final Logger log = LoggerFactory.getLogger(ParameterResource.class);

    private static final String ENTITY_NAME = "modelCatalogParameter";

    @Value("${jhipster.clientApp.name:'modelCatalogApp'}")
    private String applicationName;

    private final ParameterFacade parameterFacade;

    public ParameterResource(ParameterFacade parameterFacade) {
        this.parameterFacade = parameterFacade;
    }

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
        parameterDTO.setId(UUID.randomUUID());
        return parameterFacade
            .save(parameterDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/parameters/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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

        return parameterFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return parameterFacade
                    .update(parameterDTO)
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

        return parameterFacade
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ParameterDTO> result = parameterFacade.partialUpdate(parameterDTO);

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
     * {@code GET  /parameters} : get all the parameters.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parameters in body.
     */
    @GetMapping(value = "/parameters", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<ParameterDTO>>> getAllParameters(
        @ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Parameters");
        return parameterFacade
            .countAll()
            .zipWith(parameterFacade.findAll(pageable).collectList())
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
     * {@code GET  /parameters/:id} : get the "id" parameter.
     *
     * @param id the id of the parameterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parameterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parameters/{id}")
    public Mono<ResponseEntity<ParameterDTO>> getParameter(@PathVariable UUID id) {
        log.debug("REST request to get Parameter : {}", id);
        Mono<ParameterDTO> parameterDTO = parameterFacade.findOne(id);
        return ResponseUtil.wrapOrNotFound(parameterDTO);
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
        return parameterFacade
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
