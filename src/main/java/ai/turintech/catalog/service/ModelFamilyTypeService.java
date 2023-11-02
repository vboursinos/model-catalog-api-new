package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ModelFamilyType;
import ai.turintech.catalog.repository.ModelFamilyTypeRepository;
import ai.turintech.catalog.service.dto.ModelFamilyTypeDTO;
import ai.turintech.catalog.service.mapper.ModelFamilyTypeMapper;

import java.util.LinkedHashMap;
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
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ModelFamilyType}.
 */
@Service
@Transactional
public class ModelFamilyTypeService {

    private final Logger log = LoggerFactory.getLogger(ModelFamilyTypeService.class);

    @Autowired
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
        ModelFamilyType modelFamilyType = modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO);
        modelFamilyType = modelFamilyTypeRepository.save(modelFamilyType);
        return Mono.just(modelFamilyTypeMapper.toDto(modelFamilyType));
    }

    /**
     * Update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> update(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to update ModelFamilyType : {}", modelFamilyTypeDTO);
        ModelFamilyType modelFamilyType = modelFamilyTypeMapper.toEntity(modelFamilyTypeDTO);
        modelFamilyType = modelFamilyTypeRepository.save(modelFamilyType);
        return Mono.just(modelFamilyTypeMapper.toDto(modelFamilyType));
    }

    /**
     * Partially update a modelFamilyType.
     *
     * @param modelFamilyTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelFamilyTypeDTO> partialUpdate(ModelFamilyTypeDTO modelFamilyTypeDTO) {
        log.debug("Request to partially update ModelFamilyType : {}", modelFamilyTypeDTO);

        return Mono.justOrEmpty(modelFamilyTypeRepository
                .findById(modelFamilyTypeDTO.getId())
                .map(existingModelFamilyType -> {
                    modelFamilyTypeMapper.partialUpdate(existingModelFamilyType, modelFamilyTypeDTO);

                    return existingModelFamilyType;
                })
                .map(modelFamilyTypeRepository::save)
                .map(modelFamilyTypeMapper::toDto));
    }

    /**
     * Get all the modelFamilyTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelFamilyTypeDTO> findAll() {
        log.debug("Request to get all ModelFamilyTypes");
        List<ModelFamilyTypeDTO> modelFamilyTypeDTOS = modelFamilyTypeRepository
                .findAll()
                .stream()
                .map(modelFamilyTypeMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(modelFamilyTypeDTOS);
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
        return Mono.justOrEmpty(modelFamilyTypeRepository.findById(id).map(modelFamilyTypeMapper::toDto));
    }

    /**
     * Delete the modelFamilyType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete ModelFamilyType : {}", id);
        modelFamilyTypeRepository.deleteById(id);
    }
}
