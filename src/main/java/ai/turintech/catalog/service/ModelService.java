package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.ModelCallable;
import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.ModelPaginatedListDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import ai.turintech.catalog.utils.PaginationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Model}.
 */
@Service
@Transactional
public class ModelService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;

    private final Logger log = LoggerFactory.getLogger(ModelService.class);
    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaginationConverter paginationConverter;

    /**
     * Save a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ModelDTO> save(ModelDTO modelDTO) {
        log.debug("Request to save Model : {}", modelDTO);
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "create", modelDTO);
        return Mono.fromCallable(modelCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Update a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ModelDTO> update(ModelDTO modelDTO) {
        log.debug("Request to update Model : {}", modelDTO);
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "update", modelDTO);
        return Mono.fromCallable(modelCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Partially update a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Mono<ModelDTO> partialUpdate(ModelDTO modelDTO) {
        log.debug("Request to partially update Model : {}", modelDTO);
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "partialUpdate", modelDTO);
        return Mono.fromCallable(modelCallable)
                .subscribeOn(jdbcScheduler);
    }


    /**
     * Get all the models.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<ModelPaginatedListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Models");
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "findAll", pageable);
        return Mono.fromCallable(modelCallable)
                .subscribeOn(jdbcScheduler);
    }

    /**
     * Get all the models with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        List<Model> models = modelRepository.findAllWithEagerRelationships();
        return modelRepository.findAllWithEagerRelationships(pageable).map(modelMapper::toDto);
    }

    /**
     * Get one model by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelDTO> findOne(UUID id) throws Exception {
        log.debug("Request to get Model : {}", id);
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "findById", id);
        return Mono.fromCallable(modelCallable);
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Model : {}", id);
        ModelCallable modelCallable = context.getBean(ModelCallable.class, "delete", id);
        Mono delete = Mono.fromCallable(modelCallable);
        delete.subscribe(result -> {
            System.out.println(result);
        }, error -> {
            System.err.println("An error occurred: " + error.toString());
        });
        return delete.subscribeOn(jdbcScheduler);
    }
}
