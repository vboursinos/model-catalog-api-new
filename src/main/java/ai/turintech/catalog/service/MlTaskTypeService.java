package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.mapper.MlTaskTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return mlTaskTypeRepository.save(mlTaskTypeMapper.toEntity(mlTaskTypeDTO)).map(mlTaskTypeMapper::toDto);
    }

    /**
     * Update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> update(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to update MlTaskType : {}", mlTaskTypeDTO);
        return mlTaskTypeRepository.save(mlTaskTypeMapper.toEntity(mlTaskTypeDTO).setIsPersisted()).map(mlTaskTypeMapper::toDto);
    }

    /**
     * Partially update a mlTaskType.
     *
     * @param mlTaskTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<MlTaskTypeDTO> partialUpdate(MlTaskTypeDTO mlTaskTypeDTO) {
        log.debug("Request to partially update MlTaskType : {}", mlTaskTypeDTO);

        return mlTaskTypeRepository
            .findById(mlTaskTypeDTO.getId())
            .map(existingMlTaskType -> {
                mlTaskTypeMapper.partialUpdate(existingMlTaskType, mlTaskTypeDTO);

                return existingMlTaskType;
            })
            .flatMap(mlTaskTypeRepository::save)
            .map(mlTaskTypeMapper::toDto);
    }

    /**
     * Get all the mlTaskTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<MlTaskTypeDTO> findAll() {
        log.debug("Request to get all MlTaskTypes");
        return mlTaskTypeRepository.findAll().map(mlTaskTypeMapper::toDto);
    }

    /**
     * Returns the number of mlTaskTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return mlTaskTypeRepository.count();
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
        return mlTaskTypeRepository.findById(id).map(mlTaskTypeMapper::toDto);
    }

    /**
     * Delete the mlTaskType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete MlTaskType : {}", id);
        return mlTaskTypeRepository.deleteById(id);
    }
}
