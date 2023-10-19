package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ModelEnsembleTypeRepository;
import ai.turintech.catalog.service.dto.ModelEnsembleTypeDTO;
import ai.turintech.catalog.service.mapper.ModelEnsembleTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelEnsembleType}.
 */
@Service
@Transactional
public class ModelEnsembleTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelEnsembleTypeService.class);

    private final ModelEnsembleTypeRepository modelEnsembleTypeRepository;

    private final ModelEnsembleTypeMapper modelEnsembleTypeMapper;

    public ModelEnsembleTypeService(
        ModelEnsembleTypeRepository modelEnsembleTypeRepository,
        ModelEnsembleTypeMapper modelEnsembleTypeMapper
    ) {
        this.modelEnsembleTypeRepository = modelEnsembleTypeRepository;
        this.modelEnsembleTypeMapper = modelEnsembleTypeMapper;
    }

    /**
     * Save a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelEnsembleTypeDTO> save(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to save ModelEnsembleType : {}", modelEnsembleTypeDTO);
        return modelEnsembleTypeRepository.save(modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO)).map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelEnsembleTypeDTO> update(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to update ModelEnsembleType : {}", modelEnsembleTypeDTO);
        return modelEnsembleTypeRepository
            .save(modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO).setIsPersisted())
            .map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Partially update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelEnsembleTypeDTO> partialUpdate(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to partially update ModelEnsembleType : {}", modelEnsembleTypeDTO);

        return modelEnsembleTypeRepository
            .findById(modelEnsembleTypeDTO.getId())
            .map(existingModelEnsembleType -> {
                modelEnsembleTypeMapper.partialUpdate(existingModelEnsembleType, modelEnsembleTypeDTO);

                return existingModelEnsembleType;
            })
            .flatMap(modelEnsembleTypeRepository::save)
            .map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Get all the modelEnsembleTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelEnsembleTypeDTO> findAll() {
        log.debug("Request to get all ModelEnsembleTypes");
        return modelEnsembleTypeRepository.findAll().map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Returns the number of modelEnsembleTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelEnsembleTypeRepository.count();
    }

    /**
     * Get one modelEnsembleType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelEnsembleTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelEnsembleType : {}", id);
        return modelEnsembleTypeRepository.findById(id).map(modelEnsembleTypeMapper::toDto);
    }

    /**
     * Delete the modelEnsembleType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelEnsembleType : {}", id);
        return modelEnsembleTypeRepository.deleteById(id);
    }
}
