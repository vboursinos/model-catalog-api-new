package ai.turintech.modelcatalog.facade;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.turintech.modelcatalog.dto.ModelStructureTypeDTO;
import ai.turintech.modelcatalog.service.ModelStructureTypeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelStructureType}.
 */
@Service
@Transactional
public class ModelStructureTypeFacade {

    private final Logger log = LoggerFactory.getLogger(ModelStructureTypeFacade.class);

    private final ModelStructureTypeService modelStructureTypeService;

    public ModelStructureTypeFacade(
        ModelStructureTypeService modelStructureTypeService
    ) {
        this.modelStructureTypeService = modelStructureTypeService;
    }

    /**
     * Save a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> save(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to save ModelStructureType : {}", modelStructureTypeDTO);
        return modelStructureTypeService.save(modelStructureTypeDTO);
    }

    /**
     * Update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> update(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to update ModelStructureType : {}", modelStructureTypeDTO);
        return modelStructureTypeService.update(modelStructureTypeDTO);
    }

    /**
     * Partially update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> partialUpdate(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to partially update ModelStructureType : {}", modelStructureTypeDTO);

        return modelStructureTypeService.partialUpdate(modelStructureTypeDTO);
    }

    /**
     * Get all the modelStructureTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelStructureTypeDTO> findAll() {
        log.debug("Request to get all ModelStructureTypes");
        return modelStructureTypeService.findAll();
    }

    /**
     * Returns the number of modelStructureTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelStructureTypeService.countAll();
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
        return modelStructureTypeService.findOne(id);
    }

    /**
     * Delete the modelStructureType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ModelStructureType : {}", id);
        return modelStructureTypeService.delete(id);
    }
    
    /**
     * Returns whether or not a ModelStructureType exists with provided id.
     * @param id
     * @return a Mono to signal the existence of the ModelStructureType
     */
    public Mono<Boolean> existsById(UUID id) {
    	log.debug("Request to delete ModelGroupType : {}", id);
    	return this.modelStructureTypeService.existsById(id);
    }
}