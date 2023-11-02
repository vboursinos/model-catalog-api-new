package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.CategoricalParameterValue;
import ai.turintech.catalog.repository.CategoricalParameterValueRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterValueDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CategoricalParameterValue}.
 */
@Service
@Transactional
public class CategoricalParameterValueService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterValueService.class);

    private CategoricalParameterValueRepository categoricalParameterValueRepository;

    private CategoricalParameterValueMapper categoricalParameterValueMapper;

//    public CategoricalParameterValueService(
//        CategoricalParameterValueRepository categoricalParameterValueRepository,
//        CategoricalParameterValueMapper categoricalParameterValueMapper
//    ) {
//        this.categoricalParameterValueRepository = categoricalParameterValueRepository;
//        this.categoricalParameterValueMapper = categoricalParameterValueMapper;
//    }

    /**
     * Save a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoricalParameterValueDTO save(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to save CategoricalParameterValue : {}", categoricalParameterValueDTO);
        CategoricalParameterValue categoricalParameterValue = categoricalParameterValueMapper.toEntity(categoricalParameterValueDTO);
        categoricalParameterValue = categoricalParameterValueRepository.save(categoricalParameterValue);
        return categoricalParameterValueMapper.toDto(categoricalParameterValue);
    }

    /**
     * Update a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoricalParameterValueDTO update(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to update CategoricalParameterValue : {}", categoricalParameterValueDTO);
        CategoricalParameterValue categoricalParameterValue = categoricalParameterValueMapper.toEntity(categoricalParameterValueDTO);
        categoricalParameterValue = categoricalParameterValueRepository.save(categoricalParameterValue);
        return categoricalParameterValueMapper.toDto(categoricalParameterValue);
    }

    /**
     * Partially update a categoricalParameterValue.
     *
     * @param categoricalParameterValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoricalParameterValueDTO> partialUpdate(CategoricalParameterValueDTO categoricalParameterValueDTO) {
        log.debug("Request to partially update CategoricalParameterValue : {}", categoricalParameterValueDTO);

        return categoricalParameterValueRepository
            .findById(categoricalParameterValueDTO.getId())
            .map(existingCategoricalParameterValue -> {
                categoricalParameterValueMapper.partialUpdate(existingCategoricalParameterValue, categoricalParameterValueDTO);

                return existingCategoricalParameterValue;
            })
            .map(categoricalParameterValueRepository::save)
            .map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Get all the categoricalParameterValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CategoricalParameterValueDTO> findAll() {
        log.debug("Request to get all CategoricalParameterValues");
        return categoricalParameterValueRepository
            .findAll()
            .stream()
            .map(categoricalParameterValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one categoricalParameterValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoricalParameterValueDTO> findOne(UUID id) {
        log.debug("Request to get CategoricalParameterValue : {}", id);
        return categoricalParameterValueRepository.findById(id).map(categoricalParameterValueMapper::toDto);
    }

    /**
     * Delete the categoricalParameterValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete CategoricalParameterValue : {}", id);
        categoricalParameterValueRepository.deleteById(id);
    }
}
