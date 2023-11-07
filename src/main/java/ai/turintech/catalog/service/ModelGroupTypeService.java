package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.ModelGroupTypeCallable;
import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.repository.ModelGroupTypeRepository;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.mapper.ModelGroupTypeMapper;
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
 * Service Implementation for managing {@link ModelGroupType}.
 */
@Service
@Transactional
public class ModelGroupTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelGroupTypeService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;

    @Autowired
    private ModelGroupTypeRepository modelGroupTypeRepository;

    @Autowired
    private ModelGroupTypeMapper modelGroupTypeMapper;

    /**
     * Save a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> save(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to save ModelGroupType : {}", modelGroupTypeDTO);
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "create", modelGroupTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> update(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to update ModelGroupType : {}", modelGroupTypeDTO);
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "update", modelGroupTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Partially update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> partialUpdate(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to partially update ModelGroupType : {}", modelGroupTypeDTO);
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "partialUpdate", modelGroupTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Get all the modelGroupTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<ModelGroupTypeDTO>> findAll() {
        log.debug("Request to get all ModelGroupTypes");
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "findAll");
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<ModelGroupTypeDTO> findAllStream() {
        log.debug("Request to get all ModelGroupTypes");
        return Flux.fromIterable(modelGroupTypeRepository
                .findAll()
                .stream()
                .map(modelGroupTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Get one modelGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelGroupTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelGroupType : {}", id);
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "findById", id);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Delete the modelGroupType by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelGroupType : {}", id);
        ModelGroupTypeCallable callable = context.getBean(ModelGroupTypeCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe(result -> {
            System.out.println(result);
        }, error -> {
            System.err.println("An error occurred: " + error.toString());
        });
        return delete.subscribeOn(jdbcScheduler);
    }
}
