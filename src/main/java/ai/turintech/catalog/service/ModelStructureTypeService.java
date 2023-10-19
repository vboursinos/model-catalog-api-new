package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ModelStructureTypeRepository;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import ai.turintech.catalog.service.mapper.ModelStructureTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelStructureType}.
 */
@Service
@Transactional
public class ModelStructureTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelStructureTypeService.class);

    private final ModelStructureTypeRepository modelStructureTypeRepository;

    private final ModelStructureTypeMapper modelStructureTypeMapper;

    public ModelStructureTypeService(
        ModelStructureTypeRepository modelStructureTypeRepository,
        ModelStructureTypeMapper modelStructureTypeMapper
    ) {
        this.modelStructureTypeRepository = modelStructureTypeRepository;
        this.modelStructureTypeMapper = modelStructureTypeMapper;
    }

    /**
     * Save a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> save(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to save ModelStructureType : {}", modelStructureTypeDTO);
        return modelStructureTypeRepository
            .save(modelStructureTypeMapper.toEntity(modelStructureTypeDTO))
            .map(modelStructureTypeMapper::toDto);
    }

    /**
     * Update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> update(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to update ModelStructureType : {}", modelStructureTypeDTO);
        return modelStructureTypeRepository
            .save(modelStructureTypeMapper.toEntity(modelStructureTypeDTO).setIsPersisted())
            .map(modelStructureTypeMapper::toDto);
    }

    /**
     * Partially update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> partialUpdate(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to partially update ModelStructureType : {}", modelStructureTypeDTO);

        return modelStructureTypeRepository
            .findById(modelStructureTypeDTO.getId())
            .map(existingModelStructureType -> {
                modelStructureTypeMapper.partialUpdate(existingModelStructureType, modelStructureTypeDTO);

                return existingModelStructureType;
            })
            .flatMap(modelStructureTypeRepository::save)
            .map(modelStructureTypeMapper::toDto);
    }

    /**
     * Get all the modelStructureTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelStructureTypeDTO> findAll() {
        log.debug("Request to get all ModelStructureTypes");
        return modelStructureTypeRepository.findAll().map(modelStructureTypeMapper::toDto);
    }

    /**
     * Returns the number of modelStructureTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelStructureTypeRepository.count();
    }

    /**
     * Get one modelStructureType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelStructureTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelStructureType : {}", id);
        return modelStructureTypeRepository.findById(id).map(modelStructureTypeMapper::toDto);
    }

    /**
     * Delete the modelStructureType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelStructureType : {}", id);
        return modelStructureTypeRepository.deleteById(id);
    }
}
