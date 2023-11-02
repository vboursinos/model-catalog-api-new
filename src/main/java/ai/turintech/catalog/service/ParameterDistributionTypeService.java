package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ParameterDistributionType;
import ai.turintech.catalog.repository.ParameterDistributionTypeRepository;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import ai.turintech.catalog.service.mapper.ParameterDistributionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ParameterDistributionType}.
 */
@Service
@Transactional
public class ParameterDistributionTypeService {

    private final Logger log = LoggerFactory.getLogger(ParameterDistributionTypeService.class);

    private ParameterDistributionTypeRepository parameterDistributionTypeRepository;

    private ParameterDistributionTypeMapper parameterDistributionTypeMapper;

//    public ParameterDistributionTypeService(
//        ParameterDistributionTypeRepository parameterDistributionTypeRepository,
//        ParameterDistributionTypeMapper parameterDistributionTypeMapper
//    ) {
//        this.parameterDistributionTypeRepository = parameterDistributionTypeRepository;
//        this.parameterDistributionTypeMapper = parameterDistributionTypeMapper;
//    }

    /**
     * Save a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterDistributionTypeDTO save(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to save ParameterDistributionType : {}", parameterDistributionTypeDTO);
        ParameterDistributionType parameterDistributionType = parameterDistributionTypeMapper.toEntity(parameterDistributionTypeDTO);
        parameterDistributionType = parameterDistributionTypeRepository.save(parameterDistributionType);
        return parameterDistributionTypeMapper.toDto(parameterDistributionType);
    }

    /**
     * Update a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterDistributionTypeDTO update(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to update ParameterDistributionType : {}", parameterDistributionTypeDTO);
        ParameterDistributionType parameterDistributionType = parameterDistributionTypeMapper.toEntity(parameterDistributionTypeDTO);
        parameterDistributionType = parameterDistributionTypeRepository.save(parameterDistributionType);
        return parameterDistributionTypeMapper.toDto(parameterDistributionType);
    }

    /**
     * Partially update a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParameterDistributionTypeDTO> partialUpdate(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to partially update ParameterDistributionType : {}", parameterDistributionTypeDTO);

        return parameterDistributionTypeRepository
            .findById(parameterDistributionTypeDTO.getId())
            .map(existingParameterDistributionType -> {
                parameterDistributionTypeMapper.partialUpdate(existingParameterDistributionType, parameterDistributionTypeDTO);

                return existingParameterDistributionType;
            })
            .map(parameterDistributionTypeRepository::save)
            .map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Get all the parameterDistributionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ParameterDistributionTypeDTO> findAll() {
        log.debug("Request to get all ParameterDistributionTypes");
        return parameterDistributionTypeRepository
            .findAll()
            .stream()
            .map(parameterDistributionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one parameterDistributionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParameterDistributionTypeDTO> findOne(UUID id) {
        log.debug("Request to get ParameterDistributionType : {}", id);
        return parameterDistributionTypeRepository.findById(id).map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Delete the parameterDistributionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ParameterDistributionType : {}", id);
        parameterDistributionTypeRepository.deleteById(id);
    }
}
