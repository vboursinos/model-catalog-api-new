package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.ModelFamilyCallable;
import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.mapper.ModelFamilyTypeMapper;
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
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ModelFamilyType}.
 */
@Service
@Transactional
public class ModelFamilyTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelFamilyTypeService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;
    @Autowired
    private ModelFamilyTypeRepository modelFamilyTypeRepository;

    @Autowired
    private ModelFamilyTypeMapper modelFamilyTypeMapper;

    /**
     * Save a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> save(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to save ModelFamilyType : {}", modelFamilyTypeDTO);
        ModelFamilyCallable callable = context.getBean(ModelFamilyCallable.class, "create", modelFamilyTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> update(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to update ModelFamilyType : {}", modelFamilyTypeDTO);
        ModelFamilyCallable callable = context.getBean(ModelFamilyCallable.class, "update", modelFamilyTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Partially update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> partialUpdate(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to partially update ModelFamilyType : {}", modelFamilyTypeDTO);
        ModelFamilyCallable callable = context.getBean(ModelFamilyCallable.class, "partialUpdate", modelFamilyTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Get all the modelFamilyTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<ModelFamilyTypeDTO>> findAll() {
        log.debug("Request to get all ModelFamilyTypes");
        Callable<List<ModelFamilyTypeDTO>> callable = context.getBean(ModelFamilyCallable.class, "findAll");
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<ModelFamilyTypeDTO> findAllStream() {
        log.debug("Request to get all ModelFamilyTypes");
        return Flux.fromIterable(modelFamilyTypeRepository
                .findAll()
                .stream()
                .map(modelFamilyTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Get one modelFamilyType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelFamilyTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelFamilyType : {}", id);
        Callable<ModelFamilyTypeDTO> callable = context.getBean(ModelFamilyCallable.class, "findById", id);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Delete the modelFamilyType by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelFamilyType : {}", id);
        Callable<ModelFamilyTypeDTO> callable = context.getBean(ModelFamilyCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe(result -> {
            System.out.println(result);
        }, error -> {
            System.err.println("An error occurred: " + error.toString());
        });
        return delete.subscribeOn(jdbcScheduler);
    }
}
