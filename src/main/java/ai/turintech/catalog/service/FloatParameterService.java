package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.float_parameter.FindAllFloatParametersCallable;
import ai.turintech.catalog.callable.float_parameter.FindFloatParameterCallable;
import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.mapper.FloatParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link FloatParameter}.
 */
@Service
@Transactional
public class FloatParameterService {

    private final Logger log = LoggerFactory.getLogger(FloatParameterService.class);

    @Autowired
    private ApplicationContext context;
    @Autowired
    private Scheduler jdbcScheduler;

    @Autowired
    private FloatParameterRepository floatParameterRepository;

    @Autowired
    private FloatParameterMapper floatParameterMapper;

    /**
     * Save a floatParameter.
     *
     * @param floatParameterDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<FloatParameterDTO> save(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to save FloatParameter : {}", floatParameterDTO);
        return Mono.fromCallable(() ->  {
            FloatParameter floatParameter = floatParameterMapper.toEntity(floatParameterDTO);
            floatParameter = floatParameterRepository.save(floatParameter);
            return floatParameterMapper.toDto(floatParameter);
        });
    }

    /**
     * Update a floatParameter.
     *
     * @param floatParameterDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<FloatParameterDTO> update(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to update FloatParameter : {}", floatParameterDTO);
        return Mono.fromCallable(() ->  {
            FloatParameter floatParameter = floatParameterMapper.toEntity(floatParameterDTO);
            floatParameter = floatParameterRepository.save(floatParameter);
            return floatParameterMapper.toDto(floatParameter);
        });
    }

    /**
     * Partially update a floatParameter.
     *
     * @param floatParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<Optional<FloatParameterDTO>> partialUpdate(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to partially update FloatParameter : {}", floatParameterDTO);
        return Mono.fromCallable(() -> floatParameterRepository
            .findById(floatParameterDTO.getParameterTypeDefinitionId())
            .map(existingFloatParameter -> {
                floatParameterMapper.partialUpdate(existingFloatParameter, floatParameterDTO);

                return existingFloatParameter;
            })
            .map(floatParameterRepository::save)
            .map(floatParameterMapper::toDto));
    }

    /**
     * Get all the floatParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<FloatParameterDTO>> findAll() {
        log.debug("Request to get all FloatParameters");
        FindAllFloatParametersCallable findAllFloatParametersCallable = context.getBean(FindAllFloatParametersCallable.class);

        return Mono.fromCallable(findAllFloatParametersCallable).subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<FloatParameterDTO> findAllStream() {
        log.debug("Request to get all FloatParameters");
        return Flux.fromStream(floatParameterRepository
            .findAll()
            .stream()
            .map(floatParameterMapper::toDto));
    }

    /**
     * Get one floatParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FloatParameterDTO> findOne(UUID id) {
        log.debug("Request to get FloatParameter : {}", id);
        FindFloatParameterCallable findFloatParameterCallable = context.getBean(FindFloatParameterCallable.class, id);
        return Mono.fromCallable(findFloatParameterCallable).subscribeOn(jdbcScheduler);
    }

    /**
     * Delete the floatParameter by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete FloatParameter : {}", id);
        return Mono.fromRunnable(() -> floatParameterRepository.deleteById(id));
    }
}
