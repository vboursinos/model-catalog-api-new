package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.repository.ModelRepository;
import ai.turintech.modelcatalog.dto.ModelDTO;
import ai.turintech.modelcatalog.dtoentitymapper.ModelMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.Model}.
 */
@Service
@Transactional
public class ModelService {

    private final Logger log = LoggerFactory.getLogger(ModelService.class);

    private final ModelRepository modelRepository;

    private final ModelMapper modelMapper;

    public ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Save a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> save(ModelDTO modelDTO) {
        log.debug("Request to save Model : {}", modelDTO);
        return modelRepository.save(modelMapper.toEntity(modelDTO)).map(modelMapper::toDto);
    }

    /**
     * Update a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> update(ModelDTO modelDTO) {
        log.debug("Request to update Model : {}", modelDTO);
        return modelRepository.save(modelMapper.toEntity(modelDTO).setIsPersisted()).map(modelMapper::toDto);
    }

    /**
     * Partially update a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> partialUpdate(ModelDTO modelDTO) {
        log.debug("Request to partially update Model : {}", modelDTO);

        return modelRepository
            .findById(modelDTO.getId())
            .map(existingModel -> {
                modelMapper.partialUpdate(existingModel, modelDTO);

                return existingModel;
            })
            .flatMap(modelRepository::save)
            .map(modelMapper::toDto);
    }

    /**
     * Get all the models.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Models");
        return modelRepository.findAllBy(pageable).map(modelMapper::toDto);
    }

    /**
     * Get all the models with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return modelRepository.findAllWithEagerRelationships(pageable).map(modelMapper::toDto);
    }

    /**
     * Returns the number of models available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelRepository.count();
    }

    /**
     * Get one model by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ModelDTO> findOne(UUID id) {
        log.debug("Request to get Model : {}", id);
        return modelRepository.findOneWithEagerRelationships(id).map(modelMapper::toDto);
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Model : {}", id);
        return modelRepository.deleteById(id);
    }
    
    /**
     * Returns whether or not a Model exists with provided id.
     * @param id
     * @return a Mono to signal the existence of the Model
     */
    public Mono<Boolean> existsById(UUID id) {
    	log.debug("Request to delete ModelGroupType : {}", id);
    	return this.modelRepository.existsById(id);
    }
}
