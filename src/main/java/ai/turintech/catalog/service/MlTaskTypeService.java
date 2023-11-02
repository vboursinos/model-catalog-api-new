package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.mapper.MlTaskTypeMapper;

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
 * Service Implementation for managing {@link ai.turintech.catalog.domain.MlTaskType}.
 */
@Service
@Transactional
public class MlTaskTypeService {

    private final Logger log = LoggerFactory.getLogger(MlTaskTypeService.class);

    @Autowired
    private final MlTaskTypeRepository mlTaskTypeRepository;

    private final MlTaskTypeMapper mlTaskTypeMapper;

    public MlTaskTypeService(MlTaskTypeRepository mlTaskTypeRepository, MlTaskTypeMapper mlTaskTypeMapper) {
        this.mlTaskTypeRepository = mlTaskTypeRepository;
        this.mlTaskTypeMapper = mlTaskTypeMapper;
    }

    /**
     * Save a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> save(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to save MlTaskType : {}", mlTaskTypeDTO);
        MlTaskType mlTaskType = mlTaskTypeMapper.toEntity(mlTaskTypeDTO);
        mlTaskType = mlTaskTypeRepository.save(mlTaskType);
        return Mono.just(mlTaskTypeMapper.toDto(mlTaskType));
    }

    /**
     * Update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> update(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to update MlTaskType : {}", mlTaskTypeDTO);
        MlTaskType mlTaskType = mlTaskTypeMapper.toEntity(mlTaskTypeDTO);
        mlTaskType = mlTaskTypeRepository.save(mlTaskType);
        return Mono.just(mlTaskTypeMapper.toDto(mlTaskType));
    }

    /**
     * Partially update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> partialUpdate(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to partially update MlTaskType : {}", mlTaskTypeDTO);

        return Mono.justOrEmpty(mlTaskTypeRepository
                .findById(mlTaskTypeDTO.getId())
                .map(existingMlTaskType -> {
                    mlTaskTypeMapper.partialUpdate(existingMlTaskType, mlTaskTypeDTO);

                    return existingMlTaskType;
                })
                .map(mlTaskTypeRepository::save)
                .map(mlTaskTypeMapper::toDto));
    }

    /**
     * Get all the mlTaskTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MlTaskTypeDTO> findAll() {
        log.debug("Request to get all MlTaskTypes");
        List<MlTaskTypeDTO> mlTaskTypeDTOS = mlTaskTypeRepository.findAll().stream().map(mlTaskTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(mlTaskTypeDTOS);
    }

    /**
     * Get one mlTaskType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<MlTaskTypeDTO> findOne(UUID id) {
        log.debug("Request to get MlTaskType : {}", id);
        return Mono.justOrEmpty(mlTaskTypeRepository.findById(id).map(mlTaskTypeMapper::toDto));
    }

    /**
     * Delete the mlTaskType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete MlTaskType : {}", id);
        mlTaskTypeRepository.deleteById(id);
    }
}
