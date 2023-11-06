package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.categorical_parameter.FindAllCategoricalParametersCallable;
import ai.turintech.catalog.callable.categorical_parameter.FindCategoricalParameterCallable;
import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
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
 * Service Implementation for managing {@link CategoricalParameter}.
 */
@Service
@Transactional
public class CategoricalParameterService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;
    @Autowired
    private CategoricalParameterRepository categoricalParameterRepository;

    @Autowired
    private CategoricalParameterMapper categoricalParameterMapper;

    /**
     * Save a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> save(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to save CategoricalParameter : {}", categoricalParameterDTO);

        return Mono.fromCallable(() ->  {
            CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
            categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
            return categoricalParameterMapper.toDto(categoricalParameter);
        });
    }

    /**
     * Update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> update(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to update CategoricalParameter : {}", categoricalParameterDTO);
        return Mono.fromCallable(() ->  {
            CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
            categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
            return categoricalParameterMapper.toDto(categoricalParameter);
        });
    }

    /**
     * Partially update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> partialUpdate(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to partially update CategoricalParameter : {}", categoricalParameterDTO);
        return Mono.fromCallable(() -> {
            Optional<CategoricalParameterDTO> result = categoricalParameterRepository
                    .findById(categoricalParameterDTO.getParameterTypeDefinitionId())
                    .map(existingCategoricalParameter -> {
                        categoricalParameterMapper.partialUpdate(existingCategoricalParameter, categoricalParameterDTO);

                        return existingCategoricalParameter;
                    })
                    .map(categoricalParameterRepository::save)
                    .map(categoricalParameterMapper::toDto);
            return result.orElse(null);
        });
    }

    /**
     * Get all the categoricalParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<CategoricalParameterDTO>> findAll() {
        log.debug("Request to get all CategoricalParameters");
        FindAllCategoricalParametersCallable findAllCategoricalParametersCallable = context.getBean(FindAllCategoricalParametersCallable.class);

        return Mono.fromCallable(findAllCategoricalParametersCallable).subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<CategoricalParameterDTO> findAllStream() {
        log.debug("Request to get all BooleanParameters");
        return Flux.fromIterable(categoricalParameterRepository
                .findAll()
                .stream()
                .map(categoricalParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
    }
    /**
     * Get one categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CategoricalParameterDTO> findOne(UUID id) {
        log.debug("Request to get CategoricalParameter : {}", id);
        FindCategoricalParameterCallable findCategoricalParameterCallable = context.getBean(FindCategoricalParameterCallable.class, id);

        return Mono.fromCallable(findCategoricalParameterCallable).subscribeOn(jdbcScheduler);
    }

    /**
     * Delete the categoricalParameter by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete CategoricalParameter : {}", id);
        Mono.fromCallable(() -> {
            categoricalParameterRepository.deleteById(id);
            return null;
        });
        return null;
    }
}
