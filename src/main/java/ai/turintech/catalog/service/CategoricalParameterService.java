package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.CategoricalParameter}.
 */
@Service
@Transactional
public class CategoricalParameterService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterService.class);

    private final CategoricalParameterRepository categoricalParameterRepository;

    private final CategoricalParameterMapper categoricalParameterMapper;

    public CategoricalParameterService(
        CategoricalParameterRepository categoricalParameterRepository,
        CategoricalParameterMapper categoricalParameterMapper
    ) {
        this.categoricalParameterRepository = categoricalParameterRepository;
        this.categoricalParameterMapper = categoricalParameterMapper;
    }

    /**
     * Save a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> save(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to save CategoricalParameter : {}", categoricalParameterDTO);
        return categoricalParameterRepository
            .save(categoricalParameterMapper.toEntity(categoricalParameterDTO))
            .map(categoricalParameterMapper::toDto);
    }

    /**
     * Update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> update(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to update CategoricalParameter : {}", categoricalParameterDTO);
        return categoricalParameterRepository
            .save(categoricalParameterMapper.toEntity(categoricalParameterDTO))
            .map(categoricalParameterMapper::toDto);
    }

    /**
     * Partially update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> partialUpdate(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to partially update CategoricalParameter : {}", categoricalParameterDTO);

        return categoricalParameterRepository
            .findById(categoricalParameterDTO.getParameterTypeDefinitionId())
            .map(existingCategoricalParameter -> {
                categoricalParameterMapper.partialUpdate(existingCategoricalParameter, categoricalParameterDTO);

                return existingCategoricalParameter;
            })
            .flatMap(categoricalParameterRepository::save)
            .map(categoricalParameterMapper::toDto);
    }

    /**
     * Get all the categoricalParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CategoricalParameterDTO> findAll() {
        log.debug("Request to get all CategoricalParameters");
        return categoricalParameterRepository.findAll().map(categoricalParameterMapper::toDto);
    }

    /**
     * Returns the number of categoricalParameters available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return categoricalParameterRepository.count();
    }

    /**
     * Get one categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CategoricalParameterDTO> findOne(UUID id) {
        log.debug("Request to get CategoricalParameter : {}", id);
        return categoricalParameterRepository.findById(id).map(categoricalParameterMapper::toDto);
    }

    /**
     * Delete the categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete CategoricalParameter : {}", id);
        return categoricalParameterRepository.deleteById(id);
    }
}
