package ai.turintech.modelcatalog.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.turintech.modelcatalog.dto.BooleanParameterDTO;
import ai.turintech.modelcatalog.service.BooleanParameterService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.BooleanParameter}.
 */
@Service
@Transactional
public class BooleanParameterFacade {

    private final Logger log = LoggerFactory.getLogger(BooleanParameterFacade.class);

    private final BooleanParameterService booleanParameterService;

    public BooleanParameterFacade(BooleanParameterService booleanParameterService) {
        this.booleanParameterService = booleanParameterService;
    }

    /**
     * Save a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> save(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to save BooleanParameter : {}", booleanParameterDTO);
        return booleanParameterService.save(booleanParameterDTO);
    }

    /**
     * Update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> update(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to update BooleanParameter : {}", booleanParameterDTO);
        return booleanParameterService.save(booleanParameterDTO);
    }

    /**
     * Partially update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BooleanParameterDTO> partialUpdate(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to partially update BooleanParameter : {}", booleanParameterDTO);
        return booleanParameterService.partialUpdate(booleanParameterDTO);
    }

    /**
     * Get all the booleanParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BooleanParameterDTO> findAll() {
        log.debug("Request to get all BooleanParameters");
        return booleanParameterService.findAll();
    }

    /**
     * Returns the number of booleanParameters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return booleanParameterService.countAll();
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
        return booleanParameterService.findOne(id);
    }

    /**
     * Delete the booleanParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete BooleanParameter : {}", id);
        return booleanParameterService.delete(id);
    }
    
    /**
     * Returns wether or not a BooleanParameter exists with provided id.
     * @param id
     * @return a Mono to signal the existence of the BooleanParameter
     */
    public Mono<Boolean> existsById(Long id) {
    	log.debug("Request to delete BooleanParameter : {}", id);
    	return this.booleanParameterService.existsById(id);
    }
}