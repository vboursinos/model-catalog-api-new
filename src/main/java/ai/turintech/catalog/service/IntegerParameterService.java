package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.IntegerParameter}.
 */
@Service
@Transactional
public class IntegerParameterService {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterService.class);

    @Autowired
    private final IntegerParameterRepository integerParameterRepository;

    private final IntegerParameterMapper integerParameterMapper;

    public IntegerParameterService(IntegerParameterRepository integerParameterRepository, IntegerParameterMapper integerParameterMapper) {
        this.integerParameterRepository = integerParameterRepository;
        this.integerParameterMapper = integerParameterMapper;
    }

    /**
     * Save a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterDTO> save(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to save IntegerParameter : {}", integerParameterDTO);
        IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
        integerParameter = integerParameterRepository.save(integerParameter);
        return Mono.just(integerParameterMapper.toDto(integerParameter));
    }

    /**
     * Update a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterDTO> update(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to update IntegerParameter : {}", integerParameterDTO);
        IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
        integerParameter = integerParameterRepository.save(integerParameter);
        return Mono.just(integerParameterMapper.toDto(integerParameter));
    }

    /**
     * Partially update a integerParameter.
     *
     * @param integerParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterDTO> partialUpdate(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to partially update IntegerParameter : {}", integerParameterDTO);

        return Mono.justOrEmpty(integerParameterRepository
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
    public Flux<IntegerParameterDTO> findAll() {
        log.debug("Request to get all IntegerParameters");
        List<IntegerParameterDTO> integerParameterDTOS =  integerParameterRepository
                .findAll()
                .stream()
                .map(integerParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(integerParameterDTOS);
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
        return Mono.justOrEmpty(integerParameterRepository.findById(id).map(integerParameterMapper::toDto));
    }

    /**
     * Delete the integerParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete IntegerParameter : {}", id);
        integerParameterRepository.deleteById(id);
    }
}
