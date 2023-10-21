package ai.turintech.catalog.service;

import ai.turintech.catalog.repository.CategoricalParameterValueRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterValueDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.CategoricalParameterValue}.
 */
@Service
@Transactional
public class CategoricalParameterValueService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterValueService.class);

    private final CategoricalParameterValueRepository categoricalParameterValueRepository;

    private final CategoricalParameterValueMapper categoricalParameterValueMapper;

    public CategoricalParameterValueService(
        CategoricalParameterValueRepository categoricalParameterValueRepository,
        CategoricalParameterValueMapper categoricalParameterValueMapper
    ) {
        this.categoricalParameterValueRepository = categoricalParameterValueRepository;
        this.categoricalParameterValueMapper = categoricalParameterValueMapper;
    }

    /**
     * Save a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterValueDTO> save(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to save CategoricalParameterValue : {}", categoricalParameterValueDTO);
        return categoricalParameterValueRepository
            .save(categoricalParameterValueMapper.toEntity(categoricalParameterValueDTO))
            .map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Update a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterValueDTO> update(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to update CategoricalParameterValue : {}", categoricalParameterValueDTO);
        return categoricalParameterValueRepository
            .save(categoricalParameterValueMapper.toEntity(categoricalParameterValueDTO))
            .map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Partially update a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterValueDTO> partialUpdate(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to partially update CategoricalParameterValue : {}", categoricalParameterValueDTO);

        return categoricalParameterValueRepository
            .findById(categoricalParameterValueDTO.getId())
            .map(existingCategoricalParameterValue -> {
                categoricalParameterValueMapper.partialUpdate(existingCategoricalParameterValue, categoricalParameterValueDTO);

                return existingCategoricalParameterValue;
            })
            .flatMap(categoricalParameterValueRepository::save)
            .map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Get all the categoricalParameterValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CategoricalParameterValueDTO> findAll() {
        log.debug("Request to get all CategoricalParameterValues");
        return categoricalParameterValueRepository.findAll().map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Returns the number of categoricalParameterValues available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return categoricalParameterValueRepository.count();
    }

    /**
     * Get one categoricalParameterValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CategoricalParameterValueDTO> findOne(UUID id) {
        log.debug("Request to get CategoricalParameterValue : {}", id);
        return categoricalParameterValueRepository.findById(id).map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Delete the categoricalParameterValue by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete CategoricalParameterValue : {}", id);
        return categoricalParameterValueRepository.deleteById(id);
    }
}
