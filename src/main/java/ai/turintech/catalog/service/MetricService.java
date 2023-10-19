package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.MetricRepository;
import ai.turintech.catalog.service.dto.MetricDTO;
import ai.turintech.catalog.service.mapper.MetricMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.Metric}.
 */
@Service
@Transactional
public class MetricService {

    private final Logger log = LoggerFactory.getLogger(MetricService.class);

    private final MetricRepository metricRepository;

    private final MetricMapper metricMapper;

    public MetricService(MetricRepository metricRepository, MetricMapper metricMapper) {
        this.metricRepository = metricRepository;
        this.metricMapper = metricMapper;
    }

    /**
     * Save a metric.
     *
     * @param metricDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MetricDTO> save(MetricDTO metricDTO) {
        log.debug("Request to save Metric : {}", metricDTO);
        return metricRepository.save(metricMapper.toEntity(metricDTO)).map(metricMapper::toDto);
    }

    /**
     * Update a metric.
     *
     * @param metricDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MetricDTO> update(MetricDTO metricDTO) {
        log.debug("Request to update Metric : {}", metricDTO);
        return metricRepository.save(metricMapper.toEntity(metricDTO).setIsPersisted()).map(metricMapper::toDto);
    }

    /**
     * Partially update a metric.
     *
     * @param metricDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MetricDTO> partialUpdate(MetricDTO metricDTO) {
        log.debug("Request to partially update Metric : {}", metricDTO);

        return metricRepository
            .findById(metricDTO.getId())
            .map(existingMetric -> {
                metricMapper.partialUpdate(existingMetric, metricDTO);

                return existingMetric;
            })
            .flatMap(metricRepository::save)
            .map(metricMapper::toDto);
    }

    /**
     * Get all the metrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MetricDTO> findAll() {
        log.debug("Request to get all Metrics");
        return metricRepository.findAll().map(metricMapper::toDto);
    }

    /**
     * Returns the number of metrics available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return metricRepository.count();
    }

    /**
     * Get one metric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MetricDTO> findOne(UUID id) {
        log.debug("Request to get Metric : {}", id);
        return metricRepository.findById(id).map(metricMapper::toDto);
    }

    /**
     * Delete the metric by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Metric : {}", id);
        return metricRepository.deleteById(id);
    }
}
