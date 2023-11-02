package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelEnsembleType;
import ai.turintech.catalog.repository.ModelEnsembleTypeRepository;
import ai.turintech.catalog.service.dto.ModelEnsembleTypeDTO;
import ai.turintech.catalog.service.mapper.ModelEnsembleTypeMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
        ModelEnsembleType modelEnsembleType = modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO);
        modelEnsembleType = modelEnsembleTypeRepository.save(modelEnsembleType);
        return Mono.just(modelEnsembleTypeMapper.toDto(modelEnsembleType));
    }

    /**
     * Update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelEnsembleTypeDTO> update(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to update ModelEnsembleType : {}", modelEnsembleTypeDTO);
        ModelEnsembleType modelEnsembleType = modelEnsembleTypeMapper.toEntity(modelEnsembleTypeDTO);
        modelEnsembleType = modelEnsembleTypeRepository.save(modelEnsembleType);
        return Mono.just(modelEnsembleTypeMapper.toDto(modelEnsembleType));
    }

    /**
     * Partially update a modelEnsembleType.
     *
     * @param modelEnsembleTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelEnsembleTypeDTO> partialUpdate(ModelEnsembleTypeDTO modelEnsembleTypeDTO) {
        log.debug("Request to partially update ModelEnsembleType : {}", modelEnsembleTypeDTO);

        return Mono.justOrEmpty(modelEnsembleTypeRepository
                .findById(modelEnsembleTypeDTO.getId())
                .map(existingModelEnsembleType -> {
                    modelEnsembleTypeMapper.partialUpdate(existingModelEnsembleType, modelEnsembleTypeDTO);

                    return existingModelEnsembleType;
                })
                .map(modelEnsembleTypeRepository::save)
                .map(modelEnsembleTypeMapper::toDto));
    }

    /**
     * Get all the modelEnsembleTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelEnsembleTypeDTO> findAll() {
        log.debug("Request to get all ModelEnsembleTypes");
        List<ModelEnsembleTypeDTO> modelEnsembleTypeDTOS = modelEnsembleTypeRepository
                .findAll()
                .stream()
                .map(modelEnsembleTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(modelEnsembleTypeDTOS);
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
        return Mono.justOrEmpty(modelEnsembleTypeRepository.findById(id).map(modelEnsembleTypeMapper::toDto));
    }

    /**
     * Delete the modelEnsembleType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelEnsembleType : {}", id);
        modelEnsembleTypeRepository.deleteById(id);
    }
}
