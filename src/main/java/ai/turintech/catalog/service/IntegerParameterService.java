package ai.turintech.catalog.service;

import ai.turintech.catalog.callable.integer_parameter.FindAllIntegerParametersCallable;
import ai.turintech.catalog.callable.integer_parameter.FindIntegerParameterCallable;
import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
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
 * Service Implementation for managing {@link IntegerParameter}.
 */
@Service
@Transactional
public class IntegerParameterService {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterService.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler jdbcScheduler;

    @Autowired
    private IntegerParameterRepository integerParameterRepository;

    @Autowired
    private IntegerParameterMapper integerParameterMapper;


    /**
     * Save a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterDTO> save(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to save IntegerParameter : {}", integerParameterDTO);
        return Mono.fromCallable(() ->  {
            IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
            integerParameter = integerParameterRepository.save(integerParameter);
            return integerParameterMapper.toDto(integerParameter);
        });
    }

    /**
     * Update a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterDTO> update(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to update IntegerParameter : {}", integerParameterDTO);
        return Mono.fromCallable(() ->  {
            IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
            integerParameter = integerParameterRepository.save(integerParameter);
            return integerParameterMapper.toDto(integerParameter);
        });
    }

    /**
     * Partially update a integerParameter.
     *
     * @param integerParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Optional<IntegerParameterDTO>> partialUpdate(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to partially update IntegerParameter : {}", integerParameterDTO);
        return Mono.fromCallable(() -> integerParameterRepository
            .findById(integerParameterDTO.getParameterTypeDefinitionId())
            .map(existingIntegerParameter -> {
                integerParameterMapper.partialUpdate(existingIntegerParameter, integerParameterDTO);

                return existingIntegerParameter;
            })
            .map(integerParameterRepository::save)
            .map(integerParameterMapper::toDto));
    }

    /**
     * Get all the integerParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Mono<List<IntegerParameterDTO>> findAll() {
        log.debug("Request to get all IntegerParameters");
        FindAllIntegerParametersCallable findAllIntegerParametersCallable = context.getBean(FindAllIntegerParametersCallable.class);

        return Mono.fromCallable(findAllIntegerParametersCallable).subscribeOn(jdbcScheduler);
    }

    @Transactional(readOnly = true)
    public Flux<IntegerParameterDTO> findAllStream() {
        log.debug("Request to get all IntegerParameters");
        return Flux.fromIterable(integerParameterRepository
            .findAll()
            .stream()
            .map(integerParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new)));
    }


    /**
     * Get one integerParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IntegerParameterDTO> findOne(UUID id) {
        log.debug("Request to get IntegerParameter : {}", id);
        FindIntegerParameterCallable findAllIntegerParametersCallable = context.getBean(FindIntegerParameterCallable.class, id);

        return Mono.fromCallable(findAllIntegerParametersCallable).subscribeOn(jdbcScheduler);
    }

    /**
     * Delete the integerParameter by id.
     *
     * @param id the id of the entity.
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete IntegerParameter : {}", id);
        Mono.justOrEmpty(id).subscribe(integerParameterRepository::deleteById);
        return Mono.empty();
    }
}
