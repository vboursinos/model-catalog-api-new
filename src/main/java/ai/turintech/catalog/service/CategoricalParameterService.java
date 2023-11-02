package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.CategoricalParameter}.
 */
@Service
@Transactional
public class CategoricalParameterService {

    private final Logger log = LoggerFactory.getLogger(CategoricalParameterService.class);

    @Autowired
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
        CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
        categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
        return Mono.just(categoricalParameterMapper.toDto(categoricalParameter));
    }

    /**
     * Update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> update(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to update CategoricalParameter : {}", categoricalParameterDTO);
        CategoricalParameter categoricalParameter = categoricalParameterMapper.toEntity(categoricalParameterDTO);
        categoricalParameter = categoricalParameterRepository.save(categoricalParameter);
        return Mono.just(categoricalParameterMapper.toDto(categoricalParameter));
    }

    /**
     * Partially update a categoricalParameter.
     *
     * @param categoricalParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CategoricalParameterDTO> partialUpdate(CategoricalParameterDTO categoricalParameterDTO) {
        log.debug("Request to partially update CategoricalParameter : {}", categoricalParameterDTO);

        Optional<CategoricalParameterDTO> categoricalParameterDTOOptional = categoricalParameterRepository
                .findById(categoricalParameterDTO.getParameterTypeDefinitionId())
                .map(existingCategoricalParameter -> {
                    categoricalParameterMapper.partialUpdate(existingCategoricalParameter, categoricalParameterDTO);

                    return existingCategoricalParameter;
                })
                .map(categoricalParameterRepository::save)
                .map(categoricalParameterMapper::toDto);
        return Mono.justOrEmpty(categoricalParameterDTOOptional);
    }

    /**
     * Get all the categoricalParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CategoricalParameterDTO> findAll() {
        log.debug("Request to get all CategoricalParameters");
        List<CategoricalParameterDTO> categoricalParameterDTOList = categoricalParameterRepository
                .findAll()
                .stream()
                .map(categoricalParameterMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        return Flux.fromIterable(categoricalParameterDTOList);
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
        Optional<CategoricalParameterDTO> categoricalParameterDTO = categoricalParameterRepository.findById(id).map(categoricalParameterMapper::toDto);
        return Mono.justOrEmpty(categoricalParameterDTO);
    }

    /**
     * Delete the categoricalParameter by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete CategoricalParameter : {}", id);
        categoricalParameterRepository.deleteById(id);
    }
}
