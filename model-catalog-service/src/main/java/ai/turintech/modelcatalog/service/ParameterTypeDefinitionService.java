package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.callable.ParameterTypeDefinitionCallable;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import ai.turintech.modelcatalog.dtoentitymapper.ParameterTypeDefinitionMapper;
import ai.turintech.modelcatalog.repository.ParameterTypeDefinitionRepository;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link ParameterTypeDefinition}.
 */
@Service
@Transactional
public class ParameterTypeDefinitionService {

    private final Logger log = LoggerFactory.getLogger(ParameterTypeDefinitionService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;

    @Autowired
    private ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

    @Autowired
    private ParameterTypeDefinitionMapper parameterTypeDefinitionMapper;

    /**
     * Save a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ParameterTypeDefinitionDTO> save(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to save ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        Callable<ParameterTypeDefinitionDTO> parameterTypeDefinitionCallable = context.getBean(ParameterTypeDefinitionCallable.class, "create", parameterTypeDefinitionDTO);
        return Mono.fromCallable(parameterTypeDefinitionCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ParameterTypeDefinitionDTO> update(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        Callable<ParameterTypeDefinitionDTO> parameterTypeDefinitionCallable = context.getBean(ParameterTypeDefinitionCallable.class, "update", parameterTypeDefinitionDTO);
        return Mono.fromCallable(parameterTypeDefinitionCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Partially update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ParameterTypeDefinitionDTO> partialUpdate(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to partially update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        Callable<ParameterTypeDefinitionDTO> parameterTypeDefinitionCallable = context.getBean(ParameterTypeDefinitionCallable.class, "partialUpdate", parameterTypeDefinitionDTO);
        return Mono.fromCallable(parameterTypeDefinitionCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Get all the parameterTypeDefinitions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<ParameterTypeDefinitionDTO>> findAll() {
        log.debug("Request to get all ParameterTypeDefinitions");
        Callable<List<ParameterTypeDefinitionDTO>> parameterTypeDefinitionCallable = context.getBean(ParameterTypeDefinitionCallable.class, "findAll");
        return Mono.fromCallable(parameterTypeDefinitionCallable)
                .subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllStream() {
        log.debug("Request to get all ParameterTypeDefinitions");
        return Flux.fromIterable(parameterTypeDefinitionRepository
                .findAll()
                .stream()
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     *  Get all the parameterTypeDefinitions where IntegerParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereIntegerParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where IntegerParameter is null");
        return Flux.fromIterable(StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getIntegerParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     *  Get all the parameterTypeDefinitions where FloatParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereFloatParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where FloatParameter is null");
        return Flux.fromIterable(StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getFloatParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     *  Get all the parameterTypeDefinitions where CategoricalParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereCategoricalParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where CategoricalParameter is null");
        return Flux.fromIterable(StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getCategoricalParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     *  Get all the parameterTypeDefinitions where BooleanParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereBooleanParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where BooleanParameter is null");

        return Flux.defer(() -> Flux.fromStream(
                        parameterTypeDefinitionRepository.findAll().stream()
                                .filter(parameterTypeDefinition -> parameterTypeDefinition.getBooleanParameter() == null)
                                .map(parameterTypeDefinitionMapper::toDto)))
                .subscribeOn(Schedulers.boundedElastic());
    }
    /**
     * Get one parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParameterTypeDefinitionDTO> findOne(UUID id) {
        log.debug("Request to get ParameterTypeDefinition : {}", id);
        Callable<ParameterTypeDefinitionDTO> parameterTypeDefinitionCallable = context.getBean(ParameterTypeDefinitionCallable.class, "findById", id);
        return Mono.fromCallable(parameterTypeDefinitionCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Delete the parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ParameterTypeDefinition : {}", id);
        Callable<Void> callable = context.getBean(ParameterTypeDefinitionCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe();
        return delete;
    }


    @Transactional
    public Mono<Boolean> existsById(UUID id) {
        log.debug("Request to check if ModelGroupType exists : {}", id);
        return Mono.just(parameterTypeDefinitionRepository.existsById(id));
    }
}