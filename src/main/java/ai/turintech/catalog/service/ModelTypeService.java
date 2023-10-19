package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ModelTypeRepository;
import ai.turintech.catalog.service.dto.ModelTypeDTO;
import ai.turintech.catalog.service.mapper.ModelTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelType}.
 */
@Service
@Transactional
public class ModelTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelTypeService.class);

    private final ModelTypeRepository modelTypeRepository;

    private final ModelTypeMapper modelTypeMapper;

    public ModelTypeService(ModelTypeRepository modelTypeRepository, ModelTypeMapper modelTypeMapper) {
        this.modelTypeRepository = modelTypeRepository;
        this.modelTypeMapper = modelTypeMapper;
    }

    /**
     * Save a modelType.
     *
     * @param modelTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> save(ModelTypeDTO modelTypeDTO) {
        log.debug("Request to save ModelType : {}", modelTypeDTO);
        return modelTypeRepository.save(modelTypeMapper.toEntity(modelTypeDTO)).map(modelTypeMapper::toDto);
    }

    /**
     * Update a modelType.
     *
     * @param modelTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> update(ModelTypeDTO modelTypeDTO) {
        log.debug("Request to update ModelType : {}", modelTypeDTO);
        return modelTypeRepository.save(modelTypeMapper.toEntity(modelTypeDTO).setIsPersisted()).map(modelTypeMapper::toDto);
    }

    /**
     * Partially update a modelType.
     *
     * @param modelTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelTypeDTO> partialUpdate(ModelTypeDTO modelTypeDTO) {
        log.debug("Request to partially update ModelType : {}", modelTypeDTO);

        return modelTypeRepository
            .findById(modelTypeDTO.getId())
            .map(existingModelType -> {
                modelTypeMapper.partialUpdate(existingModelType, modelTypeDTO);

                return existingModelType;
            })
            .flatMap(modelTypeRepository::save)
            .map(modelTypeMapper::toDto);
    }

    /**
     * Get all the modelTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelTypeDTO> findAll() {
        log.debug("Request to get all ModelTypes");
        return modelTypeRepository.findAll().map(modelTypeMapper::toDto);
    }

    /**
     * Returns the number of modelTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelTypeRepository.count();
    }

    /**
     * Get one modelType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelType : {}", id);
        return modelTypeRepository.findById(id).map(modelTypeMapper::toDto);
    }

    /**
     * Delete the modelType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelType : {}", id);
        return modelTypeRepository.deleteById(id);
    }
}
