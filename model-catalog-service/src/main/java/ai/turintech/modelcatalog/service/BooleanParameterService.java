package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.callable.BooleanParameterCallable;
import ai.turintech.modelcatalog.dto.BooleanParameterDTO;
import ai.turintech.modelcatalog.dtoentitymapper.BooleanParameterMapper;
import ai.turintech.modelcatalog.repository.BooleanParameterRepository;
import ai.turintech.modelcatalog.entity.BooleanParameter;
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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Service Implementation for managing {@link BooleanParameter}.
 */
@Service
@Transactional
public class BooleanParameterService {

    private final Logger log = LoggerFactory.getLogger(BooleanParameterService.class);

    @Autowired
    private ApplicationContext context;
    @Autowired
    private Scheduler jdbcScheduler;
    @Autowired
    private BooleanParameterRepository booleanParameterRepository;

    @Autowired
    private BooleanParameterMapper booleanParameterMapper;

    /**
     * Save a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<BooleanParameterDTO> save(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to save BooleanParameter : {}", booleanParameterDTO);
        Callable<BooleanParameterDTO> callable = context.getBean(BooleanParameterCallable.class, "create", booleanParameterDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<BooleanParameterDTO> update(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to update BooleanParameter : {}", booleanParameterDTO);
        Callable<BooleanParameterDTO> callable = context.getBean(BooleanParameterCallable.class, "update", booleanParameterDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Partially update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<BooleanParameterDTO> partialUpdate(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to partially update BooleanParameter : {}", booleanParameterDTO);
        Callable<BooleanParameterDTO> callable = context.getBean(BooleanParameterCallable.class, "partialUpdate", booleanParameterDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Get all the booleanParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<BooleanParameterDTO>> findAll() {
        log.debug("Request to get all BooleanParameters");
        Callable<List<BooleanParameterDTO>> callable = context.getBean(BooleanParameterCallable.class);
        return Mono.fromCallable(callable)
                .subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<BooleanParameterDTO> findAllStream() {
        log.debug("Request to get all BooleanParameters");

        return Flux.defer(() -> Flux.fromStream(
                        booleanParameterRepository.findAll().stream()
                                .map(booleanParameterMapper::toDto)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Get one booleanParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BooleanParameterDTO> findOne(UUID id) {
        log.debug("Request to get BooleanParameter : {}", id);
        Callable<BooleanParameterDTO> callable = context.getBean(BooleanParameterCallable.class, "findById", id);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Delete the booleanParameter by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete BooleanParameter : {}", id);
        Callable<BooleanParameterDTO> callable = context.getBean(BooleanParameterCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe();
        return delete;
    }


    @Transactional
    public Mono<Boolean> existsById(UUID id) {
        log.debug("Request to check if ModelGroupType exists : {}", id);
        return Mono.just(booleanParameterRepository.existsById(id));
    }
}