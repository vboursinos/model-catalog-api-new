package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.repository.ModelStructureTypeRepository;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import ai.turintech.catalog.service.mapper.ModelStructureTypeMapper;
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
 * Service Implementation for managing {@link ModelStructureType}.
 */
@Service
@Transactional
public class ModelStructureTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelStructureTypeService.class);

    private ModelStructureTypeRepository modelStructureTypeRepository;

    private ModelStructureTypeMapper modelStructureTypeMapper;

//    public ModelStructureTypeService(
//        ModelStructureTypeRepository modelStructureTypeRepository,
//        ModelStructureTypeMapper modelStructureTypeMapper
//    ) {
//        this.modelStructureTypeRepository = modelStructureTypeRepository;
//        this.modelStructureTypeMapper = modelStructureTypeMapper;
//    }

    /**
     * Save a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelStructureTypeDTO save(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to save ModelStructureType : {}", modelStructureTypeDTO);
        ModelStructureType modelStructureType = modelStructureTypeMapper.toEntity(modelStructureTypeDTO);
        modelStructureType = modelStructureTypeRepository.save(modelStructureType);
        return modelStructureTypeMapper.toDto(modelStructureType);
    }

    /**
     * Update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelStructureTypeDTO update(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to update ModelStructureType : {}", modelStructureTypeDTO);
        ModelStructureType modelStructureType = modelStructureTypeMapper.toEntity(modelStructureTypeDTO);
        modelStructureType = modelStructureTypeRepository.save(modelStructureType);
        return modelStructureTypeMapper.toDto(modelStructureType);
    }

    /**
     * Partially update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModelStructureTypeDTO> partialUpdate(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to partially update ModelStructureType : {}", modelStructureTypeDTO);

        return modelStructureTypeRepository
            .findById(modelStructureTypeDTO.getId())
            .map(existingModelStructureType -> {
                modelStructureTypeMapper.partialUpdate(existingModelStructureType, modelStructureTypeDTO);

                return existingModelStructureType;
            })
            .map(modelStructureTypeRepository::save)
            .map(modelStructureTypeMapper::toDto);
    }

    /**
     * Get all the modelStructureTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ModelStructureTypeDTO> findAll() {
        log.debug("Request to get all ModelStructureTypes");
        return modelStructureTypeRepository
            .findAll()
            .stream()
            .map(modelStructureTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one modelStructureType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModelStructureTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelStructureType : {}", id);
        return modelStructureTypeRepository.findById(id).map(modelStructureTypeMapper::toDto);
    }

    /**
     * Delete the modelStructureType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelStructureType : {}", id);
        modelStructureTypeRepository.deleteById(id);
    }
}
