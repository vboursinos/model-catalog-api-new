package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.ModelTypeCallable;
import ai.turintech.catalog.domain.ModelType;
import ai.turintech.catalog.repository.ModelTypeRepository;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
import ai.turintech.catalog.service.mapper.ModelTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ModelType}.
 */
@Service
@Transactional
public class ModelTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelTypeService.class);
    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;
    @Autowired
    private ModelTypeRepository modelTypeRepository;

    @Autowired
    private ModelTypeMapper modelTypeMapper;

    /**
     * Save a modelType.
     *
     * @param modelTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> save(ModelTypeDTO modelTypeDTO) {
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "create", modelTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Update a modelType.
     *
     * @param modelTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> update(ModelTypeDTO modelTypeDTO) {
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "update", modelTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Partially update a modelType.
     *
     * @param modelTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> partialUpdate(ModelTypeDTO modelTypeDTO) {
        log.debug("Request to partially update ModelType : {}", modelTypeDTO);
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "partialUpdate", modelTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Get all the modelTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<ModelTypeDTO>> findAll() {
        log.debug("Request to get all ModelTypes");
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "findAll");
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<ModelTypeDTO> findAllStream() {
        log.debug("Request to get all MlTaskTypes");
        return Flux.fromIterable(modelTypeRepository
                .findAll()
                .stream()
                .map(modelTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Get one modelType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelType : {}", id);
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "findById", id);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Delete the modelType by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelType : {}", id);
        ModelTypeCallable callable = context.getBean(ModelTypeCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe(result -> {
            System.out.println(result);
        }, error -> {
            System.err.println("An error occurred: " + error.toString());
        });
        return delete.subscribeOn(jdbcScheduler);
    }
}
