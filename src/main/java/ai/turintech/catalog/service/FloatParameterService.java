package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.FloatParameter;
import ai.turintech.catalog.repository.FloatParameterRepository;
import ai.turintech.catalog.service.dto.FloatParameterDTO;
import ai.turintech.catalog.service.mapper.FloatParameterMapper;
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
 * Service Implementation for managing {@link FloatParameter}.
 */
@Service
@Transactional
public class FloatParameterService {

    private final Logger log = LoggerFactory.getLogger(FloatParameterService.class);

    private FloatParameterRepository floatParameterRepository;

    private FloatParameterMapper floatParameterMapper;

//    public FloatParameterService(FloatParameterRepository floatParameterRepository, FloatParameterMapper floatParameterMapper) {
//        this.floatParameterRepository = floatParameterRepository;
//        this.floatParameterMapper = floatParameterMapper;
//    }

    /**
     * Save a floatParameter.
     *
     * @param floatParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public FloatParameterDTO save(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to save FloatParameter : {}", floatParameterDTO);
        FloatParameter floatParameter = floatParameterMapper.toEntity(floatParameterDTO);
        floatParameter = floatParameterRepository.save(floatParameter);
        return floatParameterMapper.toDto(floatParameter);
    }

    /**
     * Update a floatParameter.
     *
     * @param floatParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public FloatParameterDTO update(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to update FloatParameter : {}", floatParameterDTO);
        FloatParameter floatParameter = floatParameterMapper.toEntity(floatParameterDTO);
        floatParameter = floatParameterRepository.save(floatParameter);
        return floatParameterMapper.toDto(floatParameter);
    }

    /**
     * Partially update a floatParameter.
     *
     * @param floatParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FloatParameterDTO> partialUpdate(FloatParameterDTO floatParameterDTO) {
        log.debug("Request to partially update FloatParameter : {}", floatParameterDTO);

        return floatParameterRepository
            .findById(floatParameterDTO.getParameterTypeDefinitionId())
            .map(existingFloatParameter -> {
                floatParameterMapper.partialUpdate(existingFloatParameter, floatParameterDTO);

                return existingFloatParameter;
            })
            .map(floatParameterRepository::save)
            .map(floatParameterMapper::toDto);
    }

    /**
     * Get all the floatParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FloatParameterDTO> findAll() {
        log.debug("Request to get all FloatParameters");
        return floatParameterRepository
            .findAll()
            .stream()
            .map(floatParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one floatParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FloatParameterDTO> findOne(UUID id) {
        log.debug("Request to get FloatParameter : {}", id);
        return floatParameterRepository.findById(id).map(floatParameterMapper::toDto);
    }

    /**
     * Delete the floatParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete FloatParameter : {}", id);
        floatParameterRepository.deleteById(id);
    }
}
