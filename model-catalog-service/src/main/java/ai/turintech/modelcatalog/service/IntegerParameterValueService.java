package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.repository.IntegerParameterValueRepository;
import ai.turintech.modelcatalog.dto.IntegerParameterValueDTO;
import ai.turintech.modelcatalog.dtoentitymapper.IntegerParameterValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.IntegerParameterValue}.
 */
@Service
@Transactional
public class IntegerParameterValueService {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterValueService.class);

    private final IntegerParameterValueRepository integerParameterValueRepository;

    private final IntegerParameterValueMapper integerParameterValueMapper;

    public IntegerParameterValueService(
        IntegerParameterValueRepository integerParameterValueRepository,
        IntegerParameterValueMapper integerParameterValueMapper
    ) {
        this.integerParameterValueRepository = integerParameterValueRepository;
        this.integerParameterValueMapper = integerParameterValueMapper;
    }

    /**
     * Save a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterValueDTO> save(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to save IntegerParameterValue : {}", integerParameterValueDTO);
        return integerParameterValueRepository
            .save(integerParameterValueMapper.toEntity(integerParameterValueDTO))
            .map(integerParameterValueMapper::toDto);
    }

    /**
     * Update a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterValueDTO> update(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to update IntegerParameterValue : {}", integerParameterValueDTO);
        return integerParameterValueRepository
            .save(integerParameterValueMapper.toEntity(integerParameterValueDTO))
            .map(integerParameterValueMapper::toDto);
    }

    /**
     * Partially update a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IntegerParameterValueDTO> partialUpdate(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to partially update IntegerParameterValue : {}", integerParameterValueDTO);

        return integerParameterValueRepository
            .findById(integerParameterValueDTO.getId())
            .map(existingIntegerParameterValue -> {
                integerParameterValueMapper.partialUpdate(existingIntegerParameterValue, integerParameterValueDTO);

                return existingIntegerParameterValue;
            })
            .flatMap(integerParameterValueRepository::save)
            .map(integerParameterValueMapper::toDto);
    }

    /**
     * Get all the integerParameterValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IntegerParameterValueDTO> findAll() {
        log.debug("Request to get all IntegerParameterValues");
        return integerParameterValueRepository.findAll().map(integerParameterValueMapper::toDto);
    }

    /**
     * Returns the number of integerParameterValues available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return integerParameterValueRepository.count();
    }

    /**
     * Get one integerParameterValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IntegerParameterValueDTO> findOne(Long id) {
        log.debug("Request to get IntegerParameterValue : {}", id);
        return integerParameterValueRepository.findById(id).map(integerParameterValueMapper::toDto);
    }

    /**
     * Delete the integerParameterValue by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IntegerParameterValue : {}", id);
        return integerParameterValueRepository.deleteById(id);
    }
    
    /**
     * Returns whether or not a IntegerParameterValue exists with provided id.
     * @param id
     * @return a Mono to signal the existence of the IntegerParameterValue
     */
    public Mono<Boolean> existsById(Long id) {
    	log.debug("Request to delete IntegerParameterValue : {}", id);
    	return this.integerParameterValueRepository.existsById(id);
    }
}
