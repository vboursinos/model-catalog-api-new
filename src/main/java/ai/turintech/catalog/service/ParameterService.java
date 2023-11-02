package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.Parameter}.
 */
@Service
@Transactional
public class ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterService.class);

    @Autowired
    private final ParameterRepository parameterRepository;

    private final ParameterMapper parameterMapper;

    public ParameterService(ParameterRepository parameterRepository, ParameterMapper parameterMapper) {
        this.parameterRepository = parameterRepository;
        this.parameterMapper = parameterMapper;
    }

    /**
     * Save a parameter.
     *
     * @param parameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterDTO> save(ParameterDTO parameterDTO) {
        log.debug("Request to save Parameter : {}", parameterDTO);
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return Mono.just(parameterMapper.toDto(parameter));
    }

    /**
     * Update a parameter.
     *
     * @param parameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterDTO> update(ParameterDTO parameterDTO) {
        log.debug("Request to update Parameter : {}", parameterDTO);
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return Mono.just(parameterMapper.toDto(parameter));
    }

    /**
     * Partially update a parameter.
     *
     * @param parameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParameterDTO> partialUpdate(ParameterDTO parameterDTO) {
        log.debug("Request to partially update Parameter : {}", parameterDTO);

        return Mono.justOrEmpty(parameterRepository
                .findById(parameterDTO.getId())
                .map(existingParameter -> {
                    parameterMapper.partialUpdate(existingParameter, parameterDTO);

                    return existingParameter;
                })
                .map(parameterRepository::save)
                .map(parameterMapper::toDto));
    }

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParameterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        Page<ParameterDTO> parameterDTOS = parameterRepository.findAll(pageable).map(parameterMapper::toDto);
        return Flux.fromIterable(parameterDTOS.getContent());
    }

    /**
     * Get one parameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParameterDTO> findOne(UUID id) {
        log.debug("Request to get Parameter : {}", id);
        return Mono.justOrEmpty(parameterRepository.findById(id).map(parameterMapper::toDto));
    }

    /**
     * Delete the parameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.deleteById(id);
    }
}
