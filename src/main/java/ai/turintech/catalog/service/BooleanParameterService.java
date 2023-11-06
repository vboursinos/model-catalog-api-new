package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.boolean_parameter.FindAllBooleanParametersCallable;
import ai.turintech.catalog.domain.BooleanParameter;
import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.mapper.BooleanParameterMapper;
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
    public Mono<BooleanParameterDTO> save(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to save BooleanParameter : {}", booleanParameterDTO);
        return Mono.fromCallable(() -> {
            BooleanParameter booleanParameter = booleanParameterMapper.toEntity(booleanParameterDTO);
            booleanParameter = booleanParameterRepository.save(booleanParameter);
            return booleanParameterMapper.toDto(booleanParameter);
        });
    }

    /**
     * Update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> update(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to update BooleanParameter : {}", booleanParameterDTO);
        return Mono.fromCallable(() -> {
            BooleanParameter booleanParameter = booleanParameterMapper.toEntity(booleanParameterDTO);
            booleanParameter = booleanParameterRepository.save(booleanParameter);
            return booleanParameterMapper.toDto(booleanParameter);
        });
    }

    /**
     * Partially update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> partialUpdate(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to partially update BooleanParameter : {}", booleanParameterDTO);
        return Mono.fromCallable(() -> {
            Optional<BooleanParameterDTO> result = booleanParameterRepository
                .findById(booleanParameterDTO.getParameterTypeDefinitionId())
                .map(existingBooleanParameter -> {
                    booleanParameterMapper.partialUpdate(existingBooleanParameter, booleanParameterDTO);

                    return existingBooleanParameter;
                })
                .map(booleanParameterRepository::save)
                .map(booleanParameterMapper::toDto);
            return result.orElse(null);
        });
    }

    /**
     * Get all the booleanParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<BooleanParameterDTO>> findAll() {
        log.debug("Request to get all BooleanParameters");
        FindAllBooleanParametersCallable findAllBooleanParametersCallable = context.getBean(FindAllBooleanParametersCallable.class);
        return Mono.fromCallable(findAllBooleanParametersCallable)
                .subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<BooleanParameterDTO> findAllStream() {
        log.debug("Request to get all BooleanParameters");
        return Flux.fromIterable(booleanParameterRepository
                .findAll()
                .stream()
                .map(booleanParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new)));
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
        return Mono.fromCallable(() -> {
            Optional<BooleanParameterDTO> booleanParameterDTO = booleanParameterRepository.findById(id).map(booleanParameterMapper::toDto);
            return booleanParameterDTO.orElse(null);
        });
    }

    /**
     * Delete the booleanParameter by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete BooleanParameter : {}", id);
        Mono.fromCallable(() -> {
            booleanParameterRepository.deleteById(id);
            return null;
        });
        return null;
    }
}
