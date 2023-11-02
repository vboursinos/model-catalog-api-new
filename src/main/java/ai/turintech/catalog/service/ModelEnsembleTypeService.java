package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelEnsembleType;
import ai.turintech.catalog.repository.ModelEnsembleTypeRepository;
import ai.turintech.catalog.service.dto.ModelEnsembleTypeDTO;
import ai.turintech.catalog.service.mapper.ModelEnsembleTypeMapper;
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
 * Service Implementation for managing {@link ModelEnsembleType}.
 */
@Service
@Transactional
public class ModelEnsembleTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelEnsembleTypeService.class);

    private ModelEnsembleTypeRepository modelEnsembleTypeRepository;

    private ModelEnsembleTypeMapper modelEnsembleTypeMapper;

//    public ModelEnsembleTypeService(
//        ModelEnsembleTypeRepository modelEnsembleTypeRepository,
//        ModelEnsembleTypeMapper modelEnsembleTypeMapper
//    ) {
//        this.modelEnsembleTypeRepository = modelEnsembleTypeRepository;
//        this.modelEnsembleTypeMapper = modelEnsembleTypeMapper;
//    }

    /**
     * Save a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelEnsembleTypeDTO save(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to save ModelEnsembleType : {}", modelEnsembleTypeDTO);
        ModelEnsembleType modelEnsembleType = modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO);
        modelEnsembleType = modelEnsembleTypeRepository.save(modelEnsembleType);
        return modelEnsembleTypeMapper.toDto(modelEnsembleType);
    }

    /**
     * Update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelEnsembleTypeDTO update(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to update ModelEnsembleType : {}", modelEnsembleTypeDTO);
        ModelEnsembleType modelEnsembleType = modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO);
        modelEnsembleType = modelEnsembleTypeRepository.save(modelEnsembleType);
        return modelEnsembleTypeMapper.toDto(modelEnsembleType);
    }

    /**
     * Partially update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModelEnsembleTypeDTO> partialUpdate(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to partially update ModelEnsembleType : {}", modelEnsembleTypeDTO);

        return modelEnsembleTypeRepository
            .findById(modelEnsembleTypeDTO.getId())
            .map(existingModelEnsembleType -> {
                modelEnsembleTypeMapper.partialUpdate(existingModelEnsembleType, modelEnsembleTypeDTO);

                return existingModelEnsembleType;
            })
            .map(modelEnsembleTypeRepository::save)
            .map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Get all the modelEnsembleTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ModelEnsembleTypeDTO> findAll() {
        log.debug("Request to get all ModelEnsembleTypes");
        return modelEnsembleTypeRepository
            .findAll()
            .stream()
            .map(modelEnsembleTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one modelEnsembleType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModelEnsembleTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelEnsembleType : {}", id);
        return modelEnsembleTypeRepository.findById(id).map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Delete the modelEnsembleType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelEnsembleType : {}", id);
        modelEnsembleTypeRepository.deleteById(id);
    }
}
