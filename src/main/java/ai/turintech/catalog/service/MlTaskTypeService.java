package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.MlTaskTypeCallable;
import ai.turintech.catalog.callable.ModelCallable;
import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.mapper.MlTaskTypeMapper;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MlTaskType}.
 */
@Service
@Transactional
public class MlTaskTypeService {

    private final Logger log = LoggerFactory.getLogger(MlTaskTypeService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;
    @Autowired
    private MlTaskTypeRepository mlTaskTypeRepository;

    @Autowired
    private MlTaskTypeMapper mlTaskTypeMapper;

    /**
     * Save a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> save(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to save MlTaskType : {}", mlTaskTypeDTO);
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "create", mlTaskTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> update(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to update MlTaskType : {}", mlTaskTypeDTO);
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "update", mlTaskTypeDTO);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Partially update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> partialUpdate(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to partially update MlTaskType : {}", mlTaskTypeDTO);
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "partialUpdate", mlTaskTypeDTO);

        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Get all the mlTaskTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<MlTaskTypeDTO>> findAll() {
        log.debug("Request to get all MlTaskTypes");
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "findAll");
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<MlTaskTypeDTO> findAllStream() {
        log.debug("Request to get all MlTaskTypes");
        return Flux.fromIterable(mlTaskTypeRepository
                .findAll()
                .stream()
                .map(mlTaskTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    /**
     * Get one mlTaskType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MlTaskTypeDTO> findOne(UUID id) {
        log.debug("Request to get MlTaskType : {}", id);
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "findById", id);
        return Mono.fromCallable(callable).publishOn(jdbcScheduler);
    }

    /**
     * Delete the mlTaskType by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete MlTaskType : {}", id);
        MlTaskTypeCallable callable = context.getBean(MlTaskTypeCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(callable);
        delete.subscribe(result -> {
            System.out.println(result);
        }, error -> {
            System.err.println("An error occurred: " + error.toString());
        });
        return delete.subscribeOn(jdbcScheduler);
    }
}
