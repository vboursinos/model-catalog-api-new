package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.mapper.BooleanParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.BooleanParameter}.
 */
@Service
@Transactional
public class BooleanParameterService {

    private final Logger log = LoggerFactory.getLogger(BooleanParameterService.class);

    private final BooleanParameterRepository booleanParameterRepository;

    private final BooleanParameterMapper booleanParameterMapper;

    public BooleanParameterService(BooleanParameterRepository booleanParameterRepository, BooleanParameterMapper booleanParameterMapper) {
        this.booleanParameterRepository = booleanParameterRepository;
        this.booleanParameterMapper = booleanParameterMapper;
    }

    /**
     * Save a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> save(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to save BooleanParameter : {}", booleanParameterDTO);
        return booleanParameterRepository.save(booleanParameterMapper.toEntity(booleanParameterDTO)).map(booleanParameterMapper::toDto);
    }

    /**
     * Update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> update(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to update BooleanParameter : {}", booleanParameterDTO);
        return booleanParameterRepository.save(booleanParameterMapper.toEntity(booleanParameterDTO)).map(booleanParameterMapper::toDto);
    }

    /**
     * Partially update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> partialUpdate(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to partially update BooleanParameter : {}", booleanParameterDTO);

        return booleanParameterRepository
            .findById(booleanParameterDTO.getId())
            .map(existingBooleanParameter -> {
                booleanParameterMapper.partialUpdate(existingBooleanParameter, booleanParameterDTO);

                return existingBooleanParameter;
            })
            .flatMap(booleanParameterRepository::save)
            .map(booleanParameterMapper::toDto);
    }

    /**
     * Get all the booleanParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BooleanParameterDTO> findAll() {
        log.debug("Request to get all BooleanParameters");
        return booleanParameterRepository.findAll().map(booleanParameterMapper::toDto);
    }

    /**
     * Returns the number of booleanParameters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return booleanParameterRepository.count();
    }

    /**
     * Get one booleanParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BooleanParameterDTO> findOne(Long id) {
        log.debug("Request to get BooleanParameter : {}", id);
        return booleanParameterRepository.findById(id).map(booleanParameterMapper::toDto);
    }

    /**
     * Delete the booleanParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete BooleanParameter : {}", id);
        return booleanParameterRepository.deleteById(id);
    }
}
