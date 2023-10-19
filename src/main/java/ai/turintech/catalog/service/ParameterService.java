package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return parameterRepository.save(parameterMapper.toEntity(parameterDTO)).map(parameterMapper::toDto);
    }

    /**
     * Update a parameter.
     *
     * @param parameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParameterDTO> update(ParameterDTO parameterDTO) {
        log.debug("Request to update Parameter : {}", parameterDTO);
        return parameterRepository.save(parameterMapper.toEntity(parameterDTO).setIsPersisted()).map(parameterMapper::toDto);
    }

    /**
     * Partially update a parameter.
     *
     * @param parameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParameterDTO> partialUpdate(ParameterDTO parameterDTO) {
        log.debug("Request to partially update Parameter : {}", parameterDTO);

        return parameterRepository
            .findById(parameterDTO.getId())
            .map(existingParameter -> {
                parameterMapper.partialUpdate(existingParameter, parameterDTO);

                return existingParameter;
            })
            .flatMap(parameterRepository::save)
            .map(parameterMapper::toDto);
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
        return parameterRepository.findAllBy(pageable).map(parameterMapper::toDto);
    }

    /**
     * Returns the number of parameters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return parameterRepository.count();
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
        return parameterRepository.findById(id).map(parameterMapper::toDto);
    }

    /**
     * Delete the parameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Parameter : {}", id);
        return parameterRepository.deleteById(id);
    }
}
