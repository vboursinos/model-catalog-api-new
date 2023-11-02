package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.IntegerParameter;
import ai.turintech.catalog.repository.IntegerParameterRepository;
import ai.turintech.catalog.service.dto.IntegerParameterDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterMapper;
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
 * Service Implementation for managing {@link IntegerParameter}.
 */
@Service
@Transactional
public class IntegerParameterService {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterService.class);

    private IntegerParameterRepository integerParameterRepository;

    private IntegerParameterMapper integerParameterMapper;

//    public IntegerParameterService(IntegerParameterRepository integerParameterRepository, IntegerParameterMapper integerParameterMapper) {
//        this.integerParameterRepository = integerParameterRepository;
//        this.integerParameterMapper = integerParameterMapper;
//    }

    /**
     * Save a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegerParameterDTO save(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to save IntegerParameter : {}", integerParameterDTO);
        IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
        integerParameter = integerParameterRepository.save(integerParameter);
        return integerParameterMapper.toDto(integerParameter);
    }

    /**
     * Update a integerParameter.
     *
     * @param integerParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegerParameterDTO update(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to update IntegerParameter : {}", integerParameterDTO);
        IntegerParameter integerParameter = integerParameterMapper.toEntity(integerParameterDTO);
        integerParameter = integerParameterRepository.save(integerParameter);
        return integerParameterMapper.toDto(integerParameter);
    }

    /**
     * Partially update a integerParameter.
     *
     * @param integerParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IntegerParameterDTO> partialUpdate(IntegerParameterDTO integerParameterDTO) {
        log.debug("Request to partially update IntegerParameter : {}", integerParameterDTO);

        return integerParameterRepository
            .findById(integerParameterDTO.getParameterTypeDefinitionId())
            .map(existingIntegerParameter -> {
                integerParameterMapper.partialUpdate(existingIntegerParameter, integerParameterDTO);

                return existingIntegerParameter;
            })
            .map(integerParameterRepository::save)
            .map(integerParameterMapper::toDto);
    }

    /**
     * Get all the integerParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IntegerParameterDTO> findAll() {
        log.debug("Request to get all IntegerParameters");
        return integerParameterRepository
            .findAll()
            .stream()
            .map(integerParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one integerParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IntegerParameterDTO> findOne(UUID id) {
        log.debug("Request to get IntegerParameter : {}", id);
        return integerParameterRepository.findById(id).map(integerParameterMapper::toDto);
    }

    /**
     * Delete the integerParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete IntegerParameter : {}", id);
        integerParameterRepository.deleteById(id);
    }
}
