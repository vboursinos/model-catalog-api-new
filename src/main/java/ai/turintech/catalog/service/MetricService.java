package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Metric;
import ai.turintech.catalog.repository.MetricRepository;
import ai.turintech.catalog.service.dto.MetricDTO;
import ai.turintech.catalog.service.mapper.MetricMapper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        Metric metric = metricMapper.toEntity(metricDTO);
        metric = metricRepository.save(metric);
        return Mono.just(metricMapper.toDto(metric));
    }

    /**
     * Update a metric.
     *
     * @param metricDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MetricDTO> update(MetricDTO metricDTO) {
        log.debug("Request to save Metric : {}", metricDTO);
        Metric metric = metricMapper.toEntity(metricDTO);
        metric = metricRepository.save(metric);
        return Mono.just(metricMapper.toDto(metric));
    }

    /**
     * Partially update a metric.
     *
     * @param metricDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MetricDTO> partialUpdate(MetricDTO metricDTO) {
        log.debug("Request to partially update Metric : {}", metricDTO);

        return Mono.justOrEmpty(metricRepository
                .findById(metricDTO.getId())
                .map(existingMetric -> {
                    metricMapper.partialUpdate(existingMetric, metricDTO);

                    return existingMetric;
                })
                .map(metricRepository::save)
                .map(metricMapper::toDto));
    }

    /**
     * Get all the metrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MetricDTO> findAll() {
        log.debug("Request to get all Metrics");
        List<MetricDTO> metricDTOS = metricRepository.findAll().stream().map(metricMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(metricDTOS);
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
        return Mono.justOrEmpty(metricRepository.findById(id).map(metricMapper::toDto));
    }

    /**
     * Delete the metric by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete Metric : {}", id);
        metricRepository.deleteById(id);
    }
}
