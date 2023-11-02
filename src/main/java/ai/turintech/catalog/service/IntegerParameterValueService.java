package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.IntegerParameterValue;
import ai.turintech.catalog.repository.IntegerParameterValueRepository;
import ai.turintech.catalog.service.dto.IntegerParameterValueDTO;
import ai.turintech.catalog.service.mapper.IntegerParameterValueMapper;
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
 * Service Implementation for managing {@link IntegerParameterValue}.
 */
@Service
@Transactional
public class IntegerParameterValueService {

    private final Logger log = LoggerFactory.getLogger(IntegerParameterValueService.class);

    private IntegerParameterValueRepository integerParameterValueRepository;

    private IntegerParameterValueMapper integerParameterValueMapper;

//    public IntegerParameterValueService(
//        IntegerParameterValueRepository integerParameterValueRepository,
//        IntegerParameterValueMapper integerParameterValueMapper
//    ) {
//        this.integerParameterValueRepository = integerParameterValueRepository;
//        this.integerParameterValueMapper = integerParameterValueMapper;
//    }

    /**
     * Save a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegerParameterValueDTO save(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to save IntegerParameterValue : {}", integerParameterValueDTO);
        IntegerParameterValue integerParameterValue = integerParameterValueMapper.toEntity(integerParameterValueDTO);
        integerParameterValue = integerParameterValueRepository.save(integerParameterValue);
        return integerParameterValueMapper.toDto(integerParameterValue);
    }

    /**
     * Update a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to save.
     * @return the persisted entity.
     */
    public IntegerParameterValueDTO update(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to update IntegerParameterValue : {}", integerParameterValueDTO);
        IntegerParameterValue integerParameterValue = integerParameterValueMapper.toEntity(integerParameterValueDTO);
        integerParameterValue = integerParameterValueRepository.save(integerParameterValue);
        return integerParameterValueMapper.toDto(integerParameterValue);
    }

    /**
     * Partially update a integerParameterValue.
     *
     * @param integerParameterValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<IntegerParameterValueDTO> partialUpdate(IntegerParameterValueDTO integerParameterValueDTO) {
        log.debug("Request to partially update IntegerParameterValue : {}", integerParameterValueDTO);

        return integerParameterValueRepository
            .findById(integerParameterValueDTO.getId())
            .map(existingIntegerParameterValue -> {
                integerParameterValueMapper.partialUpdate(existingIntegerParameterValue, integerParameterValueDTO);

                return existingIntegerParameterValue;
            })
            .map(integerParameterValueRepository::save)
            .map(integerParameterValueMapper::toDto);
    }

    /**
     * Get all the integerParameterValues.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<IntegerParameterValueDTO> findAll() {
        log.debug("Request to get all IntegerParameterValues");
        return integerParameterValueRepository
            .findAll()
            .stream()
            .map(integerParameterValueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one integerParameterValue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<IntegerParameterValueDTO> findOne(UUID id) {
        log.debug("Request to get IntegerParameterValue : {}", id);
        return integerParameterValueRepository.findById(id).map(integerParameterValueMapper::toDto);
    }

    /**
     * Delete the integerParameterValue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete IntegerParameterValue : {}", id);
        integerParameterValueRepository.deleteById(id);
    }
}
