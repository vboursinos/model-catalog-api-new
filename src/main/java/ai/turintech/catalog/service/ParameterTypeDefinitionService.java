package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.repository.ParameterTypeDefinitionRepository;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import ai.turintech.catalog.service.mapper.ParameterTypeDefinitionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link ParameterTypeDefinition}.
 */
@Service
@Transactional
public class ParameterTypeDefinitionService {

    private final Logger log = LoggerFactory.getLogger(ParameterTypeDefinitionService.class);

    private ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

    private ParameterTypeDefinitionMapper parameterTypeDefinitionMapper;

//    public ParameterTypeDefinitionService(
//        ParameterTypeDefinitionRepository parameterTypeDefinitionRepository,
//        ParameterTypeDefinitionMapper parameterTypeDefinitionMapper
//    ) {
//        this.parameterTypeDefinitionRepository = parameterTypeDefinitionRepository;
//        this.parameterTypeDefinitionMapper = parameterTypeDefinitionMapper;
//    }

    /**
     * Save a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterTypeDefinitionDTO save(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to save ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        ParameterTypeDefinition parameterTypeDefinition = parameterTypeDefinitionMapper.toEntity(parameterTypeDefinitionDTO);
        parameterTypeDefinition = parameterTypeDefinitionRepository.save(parameterTypeDefinition);
        return parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);
    }

    /**
     * Update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterTypeDefinitionDTO update(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);
        ParameterTypeDefinition parameterTypeDefinition = parameterTypeDefinitionMapper.toEntity(parameterTypeDefinitionDTO);
        parameterTypeDefinition = parameterTypeDefinitionRepository.save(parameterTypeDefinition);
        return parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);
    }

    /**
     * Partially update a parameterTypeDefinition.
     *
     * @param parameterTypeDefinitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParameterTypeDefinitionDTO> partialUpdate(ParameterTypeDefinitionDTO parameterTypeDefinitionDTO) {
        log.debug("Request to partially update ParameterTypeDefinition : {}", parameterTypeDefinitionDTO);

        return parameterTypeDefinitionRepository
            .findById(parameterTypeDefinitionDTO.getId())
            .map(existingParameterTypeDefinition -> {
                parameterTypeDefinitionMapper.partialUpdate(existingParameterTypeDefinition, parameterTypeDefinitionDTO);

                return existingParameterTypeDefinition;
            })
            .map(parameterTypeDefinitionRepository::save)
            .map(parameterTypeDefinitionMapper::toDto);
    }

    /**
     * Get all the parameterTypeDefinitions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterTypeDefinitionDTO> findAll() {
        log.debug("Request to get all ParameterTypeDefinitions");
        return parameterTypeDefinitionRepository
            .findAll()
            .stream()
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the parameterTypeDefinitions where IntegerParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterTypeDefinitionDTO> findAllWhereIntegerParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where IntegerParameter is null");
        return StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getIntegerParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the parameterTypeDefinitions where FloatParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterTypeDefinitionDTO> findAllWhereFloatParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where FloatParameter is null");
        return StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getFloatParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the parameterTypeDefinitions where CategoricalParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterTypeDefinitionDTO> findAllWhereCategoricalParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where CategoricalParameter is null");
        return StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getCategoricalParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the parameterTypeDefinitions where BooleanParameter is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterTypeDefinitionDTO> findAllWhereBooleanParameterIsNull() {
        log.debug("Request to get all parameterTypeDefinitions where BooleanParameter is null");
        return StreamSupport
            .stream(parameterTypeDefinitionRepository.findAll().spliterator(), false)
            .filter(parameterTypeDefinition -> parameterTypeDefinition.getBooleanParameter() == null)
            .map(parameterTypeDefinitionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParameterTypeDefinitionDTO> findOne(UUID id) {
        log.debug("Request to get ParameterTypeDefinition : {}", id);
        return parameterTypeDefinitionRepository.findById(id).map(parameterTypeDefinitionMapper::toDto);
    }

    /**
     * Delete the parameterTypeDefinition by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ParameterTypeDefinition : {}", id);
        parameterTypeDefinitionRepository.deleteById(id);
    }
}
