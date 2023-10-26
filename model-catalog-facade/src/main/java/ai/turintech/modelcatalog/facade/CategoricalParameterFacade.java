package ai.turintech.modelcatalog.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.turintech.modelcatalog.dto.CategoricalParameterDTO;
import ai.turintech.modelcatalog.service.CategoricalParameterService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.CategoricalParameter}.
 */
@Service
@Transactional
public class CategoricalParameterFacade {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterFacade.class);

    private final CategoricalParameterService categoricalParameterService;

    public CategoricalParameterFacade(
        CategoricalParameterService categoricalParameterService) {
        this.categoricalParameterService = categoricalParameterService;
    }

    /**
     * Save a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> save(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to save CategoricalParameter : {}", categoricalParameterDTO);
        return categoricalParameterService.save(categoricalParameterDTO);
    }

    /**
     * Update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> update(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to update CategoricalParameter : {}", categoricalParameterDTO);
        return categoricalParameterService.save(categoricalParameterDTO);
    }

    /**
     * Partially update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> partialUpdate(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to partially update CategoricalParameter : {}", categoricalParameterDTO);
        return categoricalParameterService.partialUpdate(categoricalParameterDTO);
    }

    /**
     * Get all the categoricalParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CategoricalParameterDTO> findAll() {
        log.debug("Request to get all CategoricalParameters");
        return categoricalParameterService.findAll();
    }

    /**
     * Returns the number of categoricalParameters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return categoricalParameterService.countAll();
    }

    /**
     * Get one categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CategoricalParameterDTO> findOne(Long id) {
        log.debug("Request to get CategoricalParameter : {}", id);
        return categoricalParameterService.findOne(id);
    }

    /**
     * Delete the categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete CategoricalParameter : {}", id);
        return categoricalParameterService.delete(id);
    }
    
    /**
     * Returns wether or not a BooleanParameter exists with provided id.
     * @param id
     * @return a Mono to signal the existence of the CategoricalParameter
     */
    public Mono<Boolean> existsById(Long id) {
    	log.debug("Request to determine existence of CategoricalParameter : {}", id);
    	return this.categoricalParameterService.existsById(id);
    }
}