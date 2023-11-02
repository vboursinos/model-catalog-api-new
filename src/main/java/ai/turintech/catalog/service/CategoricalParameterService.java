package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
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
 * Service Implementation for managing {@link CategoricalParameter}.
 */
@Service
@Transactional
public class CategoricalParameterService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterService.class);

    private CategoricalParameterRepository categoricalParameterRepository;

    private CategoricalParameterMapper categoricalParameterMapper;

//    public CategoricalParameterService(
//        CategoricalParameterRepository categoricalParameterRepository,
//        CategoricalParameterMapper categoricalParameterMapper
//    ) {
//        this.categoricalParameterRepository = categoricalParameterRepository;
//        this.categoricalParameterMapper = categoricalParameterMapper;
//    }

    /**
     * Save a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoricalParameterDTO save(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to save CategoricalParameter : {}", categoricalParameterDTO);
        CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
        categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
        return categoricalParameterMapper.toDto(categoricalParameter);
    }

    /**
     * Update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoricalParameterDTO update(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to update CategoricalParameter : {}", categoricalParameterDTO);
        CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
        categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
        return categoricalParameterMapper.toDto(categoricalParameter);
    }

    /**
     * Partially update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoricalParameterDTO> partialUpdate(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to partially update CategoricalParameter : {}", categoricalParameterDTO);

        return categoricalParameterRepository
            .findById(categoricalParameterDTO.getParameterTypeDefinitionId())
            .map(existingCategoricalParameter -> {
                categoricalParameterMapper.partialUpdate(existingCategoricalParameter, categoricalParameterDTO);

                return existingCategoricalParameter;
            })
            .map(categoricalParameterRepository::save)
            .map(categoricalParameterMapper::toDto);
    }

    /**
     * Get all the categoricalParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CategoricalParameterDTO> findAll() {
        log.debug("Request to get all CategoricalParameters");
        return categoricalParameterRepository
            .findAll()
            .stream()
            .map(categoricalParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoricalParameterDTO> findOne(UUID id) {
        log.debug("Request to get CategoricalParameter : {}", id);
        return categoricalParameterRepository.findById(id).map(categoricalParameterMapper::toDto);
    }

    /**
     * Delete the categoricalParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete CategoricalParameter : {}", id);
        categoricalParameterRepository.deleteById(id);
    }
}
