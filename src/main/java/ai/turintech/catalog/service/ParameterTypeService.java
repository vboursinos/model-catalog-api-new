package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.ParameterType;
import ai.turintech.catalog.repository.ParameterTypeRepository;
import ai.turintech.catalog.service.dto.ParameterTypeDTO;
import ai.turintech.catalog.service.mapper.ParameterTypeMapper;

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
 * Service Implementation for managing {@link ai.turintech.catalog.domain.ParameterType}.
 */
@Service
@Transactional
public class ParameterTypeService {

    private final Logger log = LoggerFactory.getLogger(ParameterTypeService.class);

    @Autowired
    private final ParameterTypeRepository parameterTypeRepository;

    private final ParameterTypeMapper parameterTypeMapper;

    public ParameterTypeService(ParameterTypeRepository parameterTypeRepository, ParameterTypeMapper parameterTypeMapper) {
        this.parameterTypeRepository = parameterTypeRepository;
        this.parameterTypeMapper = parameterTypeMapper;
    }

    /**
     * Save a parameterType.
     *
     * @param parameterTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDTO> save(ParameterTypeDTO parameterTypeDTO) {
        log.debug("Request to save ParameterType : {}", parameterTypeDTO);
        ParameterType parameterType = parameterTypeMapper.toEntity(parameterTypeDTO);
        parameterType = parameterTypeRepository.save(parameterType);
        return Mono.just(parameterTypeMapper.toDto(parameterType));
    }

    /**
     * Update a parameterType.
     *
     * @param parameterTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDTO> update(ParameterTypeDTO parameterTypeDTO) {
        log.debug("Request to update ParameterType : {}", parameterTypeDTO);
        ParameterType parameterType = parameterTypeMapper.toEntity(parameterTypeDTO);
        parameterType = parameterTypeRepository.save(parameterType);
        return Mono.just(parameterTypeMapper.toDto(parameterType));
    }

    /**
     * Partially update a parameterType.
     *
     * @param parameterTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParameterTypeDTO> partialUpdate(ParameterTypeDTO parameterTypeDTO) {
        log.debug("Request to partially update ParameterType : {}", parameterTypeDTO);

        return Mono.justOrEmpty(parameterTypeRepository
                .findById(parameterTypeDTO.getId())
                .map(existingParameterType -> {
                    parameterTypeMapper.partialUpdate(existingParameterType, parameterTypeDTO);

                    return existingParameterType;
                })
                .map(parameterTypeRepository::save)
                .map(parameterTypeMapper::toDto));
    }

    /**
     * Get all the parameterTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterTypeDTO> findAll() {
        log.debug("Request to get all ParameterTypes");
        List<ParameterTypeDTO> parameterTypeDTOS = parameterTypeRepository.findAll().stream().map(parameterTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(parameterTypeDTOS);
    }

    /**
     * Get one parameterType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParameterTypeDTO> findOne(UUID id) {
        log.debug("Request to get ParameterType : {}", id);
        return Mono.justOrEmpty(parameterTypeRepository.findById(id).map(parameterTypeMapper::toDto));
    }

    /**
     * Delete the parameterType by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete ParameterType : {}", id);
        parameterTypeRepository.deleteById(id);
    }
}
