package ai.turintech.catalog.web.rest;

import ai.turintech.catalog.repository.MetricRepository;
import ai.turintech.catalog.service.MetricService;
import ai.turintech.catalog.service.dto.MetricDTO;
import ai.turintech.catalog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link ai.turintech.catalog.domain.Metric}.
 */
@RestController
@RequestMapping("/api")
public class MetricResource {

    private final Logger log = LoggerFactory.getLogger(MetricResource.class);

    private static final String ENTITY_NAME = "modelCatalogMetric";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetricService metricService;

    private final MetricRepository metricRepository;

    public MetricResource(MetricService metricService, MetricRepository metricRepository) {
        this.metricService = metricService;
        this.metricRepository = metricRepository;
    }

    /**
     * {@code POST  /metrics} : Create a new metric.
     *
     * @param metricDTO the metricDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metricDTO, or with status {@code 400 (Bad Request)} if the metric has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/metrics")
    public Mono<ResponseEntity<MetricDTO>> createMetric(@RequestBody MetricDTO metricDTO) throws URISyntaxException {
        log.debug("REST request to save Metric : {}", metricDTO);
        if (metricDTO.getId() != null) {
            throw new BadRequestAlertException("A new metric cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metricDTO.setId(UUID.randomUUID());
        return metricService
            .save(metricDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/metrics/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /metrics/:id} : Updates an existing metric.
     *
     * @param id the id of the metricDTO to save.
     * @param metricDTO the metricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metricDTO,
     * or with status {@code 400 (Bad Request)} if the metricDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/metrics/{id}")
    public Mono<ResponseEntity<MetricDTO>> updateMetric(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody MetricDTO metricDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Metric : {}, {}", id, metricDTO);
        if (metricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return metricRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return metricService
                    .update(metricDTO)
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
     * {@code PATCH  /metrics/:id} : Partial updates given fields of an existing metric, field will ignore if it is null
     *
     * @param id the id of the metricDTO to save.
     * @param metricDTO the metricDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metricDTO,
     * or with status {@code 400 (Bad Request)} if the metricDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metricDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metricDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/metrics/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<MetricDTO>> partialUpdateMetric(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody MetricDTO metricDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Metric partially : {}, {}", id, metricDTO);
        if (metricDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metricDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return metricRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<MetricDTO> result = metricService.partialUpdate(metricDTO);

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
     * {@code GET  /metrics} : get all the metrics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metrics in body.
     */
    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<MetricDTO>> getAllMetrics() {
        log.debug("REST request to get all Metrics");
        return metricService.findAll().collectList();
    }

    /**
     * {@code GET  /metrics} : get all the metrics as a stream.
     * @return the {@link Flux} of metrics.
     */
    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<MetricDTO> getAllMetricsAsStream() {
        log.debug("REST request to get all Metrics as a stream");
        return metricService.findAll();
    }

    /**
     * {@code GET  /metrics/:id} : get the "id" metric.
     *
     * @param id the id of the metricDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metricDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/metrics/{id}")
    public Mono<ResponseEntity<MetricDTO>> getMetric(@PathVariable UUID id) {
        log.debug("REST request to get Metric : {}", id);
        Mono<MetricDTO> metricDTO = metricService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metricDTO);
    }

    /**
     * {@code DELETE  /metrics/:id} : delete the "id" metric.
     *
     * @param id the id of the metricDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/metrics/{id}")
    public Mono<ResponseEntity<Void>> deleteMetric(@PathVariable UUID id) {
        log.debug("REST request to delete Metric : {}", id);
        return metricService
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
