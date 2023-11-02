package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.repository.ModelStructureTypeRepository;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import ai.turintech.catalog.service.mapper.ModelStructureTypeMapper;

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
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelStructureType}.
 */
@Service
@Transactional
public class ModelStructureTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelStructureTypeService.class);

    @Autowired
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
        ModelStructureType modelStructureType = modelStructureTypeMapper.toEntity(modelStructureTypeDTO);
        modelStructureType = modelStructureTypeRepository.save(modelStructureType);
        return Mono.just(modelStructureTypeMapper.toDto(modelStructureType));
    }

    /**
     * Update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> update(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to update ModelStructureType : {}", modelStructureTypeDTO);
        ModelStructureType modelStructureType = modelStructureTypeMapper.toEntity(modelStructureTypeDTO);
        modelStructureType = modelStructureTypeRepository.save(modelStructureType);
        return Mono.just(modelStructureTypeMapper.toDto(modelStructureType));
    }

    /**
     * Partially update a modelStructureType.
     *
     * @param modelStructureTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelStructureTypeDTO> partialUpdate(ModelStructureTypeDTO modelStructureTypeDTO) {
        log.debug("Request to partially update ModelStructureType : {}", modelStructureTypeDTO);

        return Mono.justOrEmpty(modelStructureTypeRepository
                .findById(modelStructureTypeDTO.getId())
                .map(existingModelStructureType -> {
                    modelStructureTypeMapper.partialUpdate(existingModelStructureType, modelStructureTypeDTO);

                    return existingModelStructureType;
                })
                .map(modelStructureTypeRepository::save)
                .map(modelStructureTypeMapper::toDto));
    }

    /**
     * Get all the modelStructureTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelStructureTypeDTO> findAll() {
        log.debug("Request to get all ModelStructureTypes");
        List<ModelStructureTypeDTO> modelStructureTypeDTOS = modelStructureTypeRepository
                .findAll()
                .stream()
                .map(modelStructureTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(modelStructureTypeDTOS);
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
        return Mono.justOrEmpty(modelStructureTypeRepository.findById(id).map(modelStructureTypeMapper::toDto));
    }

    /**
     * Delete the modelStructureType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelStructureType : {}", id);
        modelStructureTypeRepository.deleteById(id);
    }
}
