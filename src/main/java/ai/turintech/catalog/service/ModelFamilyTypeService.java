package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.mapper.ModelFamilyTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelFamilyType}.
 */
@Service
@Transactional
public class ModelFamilyTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelFamilyTypeService.class);

    private final ModelFamilyTypeRepository modelFamilyTypeRepository;

    private final ModelFamilyTypeMapper modelFamilyTypeMapper;

    public ModelFamilyTypeService(ModelFamilyTypeRepository modelFamilyTypeRepository, ModelFamilyTypeMapper modelFamilyTypeMapper) {
        this.modelFamilyTypeRepository = modelFamilyTypeRepository;
        this.modelFamilyTypeMapper = modelFamilyTypeMapper;
    }

    /**
     * Save a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> save(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to save ModelFamilyType : {}", modelFamilyTypeDTO);
        return modelFamilyTypeRepository.save(modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO)).map(modelFamilyTypeMapper::toDto);
    }

    /**
     * Update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> update(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to update ModelFamilyType : {}", modelFamilyTypeDTO);
        return modelFamilyTypeRepository
            .save(modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO).setIsPersisted())
            .map(modelFamilyTypeMapper::toDto);
    }

    /**
     * Partially update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> partialUpdate(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to partially update ModelFamilyType : {}", modelFamilyTypeDTO);

        return modelFamilyTypeRepository
            .findById(modelFamilyTypeDTO.getId())
            .map(existingModelFamilyType -> {
                modelFamilyTypeMapper.partialUpdate(existingModelFamilyType, modelFamilyTypeDTO);

                return existingModelFamilyType;
            })
            .flatMap(modelFamilyTypeRepository::save)
            .map(modelFamilyTypeMapper::toDto);
    }

    /**
     * Get all the modelFamilyTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelFamilyTypeDTO> findAll() {
        log.debug("Request to get all ModelFamilyTypes");
        return modelFamilyTypeRepository.findAll().map(modelFamilyTypeMapper::toDto);
    }

    /**
     * Returns the number of modelFamilyTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelFamilyTypeRepository.count();
    }

    /**
     * Get one modelFamilyType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelFamilyTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelFamilyType : {}", id);
        return modelFamilyTypeRepository.findById(id).map(modelFamilyTypeMapper::toDto);
    }

    /**
     * Delete the modelFamilyType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelFamilyType : {}", id);
        return modelFamilyTypeRepository.deleteById(id);
    }
}
