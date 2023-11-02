package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Parameter;
import ai.turintech.catalog.repository.ParameterRepository;
import ai.turintech.catalog.service.dto.ParameterDTO;
import ai.turintech.catalog.service.mapper.ParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Parameter}.
 */
@Service
@Transactional
public class ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterService.class);

    private ParameterRepository parameterRepository;

    private ParameterMapper parameterMapper;

//    public ParameterService(ParameterRepository parameterRepository, ParameterMapper parameterMapper) {
//        this.parameterRepository = parameterRepository;
//        this.parameterMapper = parameterMapper;
//    }

    /**
     * Save a parameter.
     *
     * @param parameterDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterDTO save(ParameterDTO parameterDTO) {
        log.debug("Request to save Parameter : {}", parameterDTO);
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return parameterMapper.toDto(parameter);
    }

    /**
     * Update a parameter.
     *
     * @param parameterDTO the entity to save.
     * @return the persisted entity.
     */
    public ParameterDTO update(ParameterDTO parameterDTO) {
        log.debug("Request to update Parameter : {}", parameterDTO);
        Parameter parameter = parameterMapper.toEntity(parameterDTO);
        parameter = parameterRepository.save(parameter);
        return parameterMapper.toDto(parameter);
    }

    /**
     * Partially update a parameter.
     *
     * @param parameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParameterDTO> partialUpdate(ParameterDTO parameterDTO) {
        log.debug("Request to partially update Parameter : {}", parameterDTO);

        return parameterRepository
            .findById(parameterDTO.getId())
            .map(existingParameter -> {
                parameterMapper.partialUpdate(existingParameter, parameterDTO);

                return existingParameter;
            })
            .map(parameterRepository::save)
            .map(parameterMapper::toDto);
    }

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParameterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        return parameterRepository.findAll(pageable).map(parameterMapper::toDto);
    }

    /**
     * Get one parameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParameterDTO> findOne(UUID id) {
        log.debug("Request to get Parameter : {}", id);
        return parameterRepository.findById(id).map(parameterMapper::toDto);
    }

    /**
     * Delete the parameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.deleteById(id);
    }
}
