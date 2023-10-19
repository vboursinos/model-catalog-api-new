package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ParameterDistributionTypeRepository;
import ai.turintech.catalog.service.dto.ParameterDistributionTypeDTO;
import ai.turintech.catalog.service.mapper.ParameterDistributionTypeMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ParameterDistributionType}.
 */
@Service
@Transactional
public class ParameterDistributionTypeService {

    private final Logger log = LoggerFactory.getLogger(ParameterDistributionTypeService.class);

    private final ParameterDistributionTypeRepository parameterDistributionTypeRepository;

    private final ParameterDistributionTypeMapper parameterDistributionTypeMapper;

    public ParameterDistributionTypeService(
        ParameterDistributionTypeRepository parameterDistributionTypeRepository,
        ParameterDistributionTypeMapper parameterDistributionTypeMapper
    ) {
        this.parameterDistributionTypeRepository = parameterDistributionTypeRepository;
        this.parameterDistributionTypeMapper = parameterDistributionTypeMapper;
    }

    /**
     * Save a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterDistributionTypeDTO> save(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to save ParameterDistributionType : {}", parameterDistributionTypeDTO);
        return parameterDistributionTypeRepository
            .save(parameterDistributionTypeMapper.toEntity(parameterDistributionTypeDTO))
            .map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Update a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterDistributionTypeDTO> update(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to update ParameterDistributionType : {}", parameterDistributionTypeDTO);
        return parameterDistributionTypeRepository
            .save(parameterDistributionTypeMapper.toEntity(parameterDistributionTypeDTO).setIsPersisted())
            .map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Partially update a parameterDistributionType.
     *
     * @param parameterDistributionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParameterDistributionTypeDTO> partialUpdate(ParameterDistributionTypeDTO parameterDistributionTypeDTO) {
        log.debug("Request to partially update ParameterDistributionType : {}", parameterDistributionTypeDTO);

        return parameterDistributionTypeRepository
            .findById(parameterDistributionTypeDTO.getId())
            .map(existingParameterDistributionType -> {
                parameterDistributionTypeMapper.partialUpdate(existingParameterDistributionType, parameterDistributionTypeDTO);

                return existingParameterDistributionType;
            })
            .flatMap(parameterDistributionTypeRepository::save)
            .map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Get all the parameterDistributionTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterDistributionTypeDTO> findAll() {
        log.debug("Request to get all ParameterDistributionTypes");
        return parameterDistributionTypeRepository.findAll().map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Returns the number of parameterDistributionTypes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return parameterDistributionTypeRepository.count();
    }

    /**
     * Get one parameterDistributionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParameterDistributionTypeDTO> findOne(UUID id) {
        log.debug("Request to get ParameterDistributionType : {}", id);
        return parameterDistributionTypeRepository.findById(id).map(parameterDistributionTypeMapper::toDto);
    }

    /**
     * Delete the parameterDistributionType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete ParameterDistributionType : {}", id);
        return parameterDistributionTypeRepository.deleteById(id);
    }
}
