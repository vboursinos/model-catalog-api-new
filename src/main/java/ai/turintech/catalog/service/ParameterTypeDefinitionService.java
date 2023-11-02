package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.repository.ParameterTypeDefinitionRepository;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import ai.turintech.catalog.service.mapper.ParameterTypeDefinitionMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ParameterTypeDefinition}.
 */
@Service
@Transactional
public class ParameterTypeDefinitionService {

    private final Logger log = LoggerFactory.getLogger(ParameterTypeDefinitionService.class);

    @Autowired
    private final ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

    private final ParameterTypeDefinitionMapper parameterTypeDefinitionMapper;

    public ParameterTypeDefinitionService(
        ParameterTypeDefinitionRepository parameterTypeDefinitionRepository,
        ParameterTypeDefinitionMapper parameterTypeDefinitionMapper
    ) {
        this.parameterTypeDefinitionRepository = parameterTypeDefinitionRepository;
        this.parameterTypeDefinitionMapper = parameterTypeDefinitionMapper;
    }

    /**
     * Save a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDefinitionDTO> save(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to save ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        ParameterTypeDefinition parameterTypeDefinition = parameterTypeDefinitionMapper.toEntity(parameterTypeDefinitionDTO);
        parameterTypeDefinition = parameterTypeDefinitionRepository.save(parameterTypeDefinition);
        return Mono.just(parameterTypeDefinitionMapper.toDto(parameterTypeDefinition));
    }

    /**
     * Update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDefinitionDTO> update(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        ParameterTypeDefinition parameterTypeDefinition = parameterTypeDefinitionMapper.toEntity(parameterTypeDefinitionDTO);
        parameterTypeDefinition = parameterTypeDefinitionRepository.save(parameterTypeDefinition);
        return Mono.just(parameterTypeDefinitionMapper.toDto(parameterTypeDefinition));
    }

    /**
     * Partially update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDefinitionDTO> partialUpdate(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to partially update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);

        return Mono.justOrEmpty(parameterTypeDefinitionRepository
                .findById(parameterTypeDefinitionDTO.getId())
                .map(existingParameterTypeDefinition -> {
                    parameterTypeDefinitionMapper.partialUpdate(existingParameterTypeDefinition, parameterTypeDefinitionDTO);

                    return existingParameterTypeDefinition;
                })
                .map(parameterTypeDefinitionRepository::save)
                .map(parameterTypeDefinitionMapper::toDto));
    }

    /**
     * Get all the parameterTypeDefinitions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAll() {
        log.debug("Request to get all ParameterTypeDefinitions");
        List<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOS = parameterTypeDefinitionRepository
                .findAll()
                .stream()
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDefinitionDTOS);
    }

    /**
     *  Get all the parameterTypeDefinitions where IntegerParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereIntegerParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where IntegerParameter is null");
        List<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOS = StreamSupport
                .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
                .filter(parameterTypeDefinition -> parameterTypeDefinition.getIntegerParameter() == null)
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDefinitionDTOS);
    }

    /**
     *  Get all the parameterTypeDefinitions where FloatParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereFloatParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where FloatParameter is null");
        List<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOS = StreamSupport
                .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
                .filter(parameterTypeDefinition -> parameterTypeDefinition.getFloatParameter() == null)
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDefinitionDTOS);
    }

    /**
     *  Get all the parameterTypeDefinitions where CategoricalParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereCategoricalParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where CategoricalParameter is null");
        List<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOS = StreamSupport
                .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
                .filter(parameterTypeDefinition -> parameterTypeDefinition.getCategoricalParameter() == null)
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDefinitionDTOS);
    }

    /**
     *  Get all the parameterTypeDefinitions where BooleanParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDefinitionDTO> findAllWhereBooleanParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where BooleanParameter is null");
        List<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOS = StreamSupport
                .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
                .filter(parameterTypeDefinition -> parameterTypeDefinition.getBooleanParameter() == null)
                .map(parameterTypeDefinitionMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDefinitionDTOS);
    }

    /**
     * Get one parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParameterTypeDefinitionDTO> findOne(UUID id) {
        log.debug("Request to get ParameterTypeDefinition : {}", id);
        return Mono.justOrEmpty(parameterTypeDefinitionRepository.findById(id).map(parameterTypeDefinitionMapper::toDto));
    }

    /**
     * Delete the parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete ParameterTypeDefinition : {}", id);
        parameterTypeDefinitionRepository.deleteById(id);
    }
}
