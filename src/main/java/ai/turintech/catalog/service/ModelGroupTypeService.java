package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelGroupType;
import ai.turintech.catalog.repository.ModelGroupTypeRepository;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.mapper.ModelGroupTypeMapper;
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
 * Service Implementation for managing {@link ModelGroupType}.
 */
@Service
@Transactional
public class ModelGroupTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelGroupTypeService.class);

    private ModelGroupTypeRepository modelGroupTypeRepository;

    private ModelGroupTypeMapper modelGroupTypeMapper;

//    public ModelGroupTypeService(ModelGroupTypeRepository modelGroupTypeRepository, ModelGroupTypeMapper modelGroupTypeMapper) {
//        this.modelGroupTypeRepository = modelGroupTypeRepository;
//        this.modelGroupTypeMapper = modelGroupTypeMapper;
//    }

    /**
     * Save a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelGroupTypeDTO save(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to save ModelGroupType : {}", modelGroupTypeDTO);
        ModelGroupType modelGroupType = modelGroupTypeMapper.toEntity(modelGroupTypeDTO);
        modelGroupType = modelGroupTypeRepository.save(modelGroupType);
        return modelGroupTypeMapper.toDto(modelGroupType);
    }

    /**
     * Update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelGroupTypeDTO update(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to update ModelGroupType : {}", modelGroupTypeDTO);
        ModelGroupType modelGroupType = modelGroupTypeMapper.toEntity(modelGroupTypeDTO);
        modelGroupType = modelGroupTypeRepository.save(modelGroupType);
        return modelGroupTypeMapper.toDto(modelGroupType);
    }

    /**
     * Partially update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModelGroupTypeDTO> partialUpdate(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to partially update ModelGroupType : {}", modelGroupTypeDTO);

        return modelGroupTypeRepository
            .findById(modelGroupTypeDTO.getId())
            .map(existingModelGroupType -> {
                modelGroupTypeMapper.partialUpdate(existingModelGroupType, modelGroupTypeDTO);

                return existingModelGroupType;
            })
            .map(modelGroupTypeRepository::save)
            .map(modelGroupTypeMapper::toDto);
    }

    /**
     * Get all the modelGroupTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ModelGroupTypeDTO> findAll() {
        log.debug("Request to get all ModelGroupTypes");
        return modelGroupTypeRepository
            .findAll()
            .stream()
            .map(modelGroupTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one modelGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModelGroupTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelGroupType : {}", id);
        return modelGroupTypeRepository.findById(id).map(modelGroupTypeMapper::toDto);
    }

    /**
     * Delete the modelGroupType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelGroupType : {}", id);
        modelGroupTypeRepository.deleteById(id);
    }
}
