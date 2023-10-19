package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ModelGroupTypeRepository;
import ai.turintech.catalog.service.dto.ModelGroupTypeDTO;
import ai.turintech.catalog.service.mapper.ModelGroupTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelGroupType}.
 */
@Service
@Transactional
public class ModelGroupTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelGroupTypeService.class);

    private final ModelGroupTypeRepository modelGroupTypeRepository;

    private final ModelGroupTypeMapper modelGroupTypeMapper;

    public ModelGroupTypeService(ModelGroupTypeRepository modelGroupTypeRepository, ModelGroupTypeMapper modelGroupTypeMapper) {
        this.modelGroupTypeRepository = modelGroupTypeRepository;
        this.modelGroupTypeMapper = modelGroupTypeMapper;
    }

    /**
     * Save a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> save(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to save ModelGroupType : {}", modelGroupTypeDTO);
        return modelGroupTypeRepository.save(modelGroupTypeMapper.toEntity(modelGroupTypeDTO)).map(modelGroupTypeMapper::toDto);
    }

    /**
     * Update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> update(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to update ModelGroupType : {}", modelGroupTypeDTO);
        return modelGroupTypeRepository
            .save(modelGroupTypeMapper.toEntity(modelGroupTypeDTO).setIsPersisted())
            .map(modelGroupTypeMapper::toDto);
    }

    /**
     * Partially update a modelGroupType.
     *
     * @param modelGroupTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelGroupTypeDTO> partialUpdate(ModelGroupTypeDTO modelGroupTypeDTO) {
        log.debug("Request to partially update ModelGroupType : {}", modelGroupTypeDTO);

        return modelGroupTypeRepository
            .findById(modelGroupTypeDTO.getId())
            .map(existingModelGroupType -> {
                modelGroupTypeMapper.partialUpdate(existingModelGroupType, modelGroupTypeDTO);

                return existingModelGroupType;
            })
            .flatMap(modelGroupTypeRepository::save)
            .map(modelGroupTypeMapper::toDto);
    }

    /**
     * Get all the modelGroupTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelGroupTypeDTO> findAll() {
        log.debug("Request to get all ModelGroupTypes");
        return modelGroupTypeRepository.findAll().map(modelGroupTypeMapper::toDto);
    }

    /**
     * Returns the number of modelGroupTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelGroupTypeRepository.count();
    }

    /**
     * Get one modelGroupType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelGroupTypeDTO> findOne(UUID id) {
        log.debug("Request to get ModelGroupType : {}", id);
        return modelGroupTypeRepository.findById(id).map(modelGroupTypeMapper::toDto);
    }

    /**
     * Delete the modelGroupType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelGroupType : {}", id);
        return modelGroupTypeRepository.deleteById(id);
    }
}
