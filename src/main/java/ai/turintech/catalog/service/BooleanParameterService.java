package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.BooleanParameter;
import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.mapper.BooleanParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BooleanParameter}.
 */
@Service
@Transactional
public class BooleanParameterService {

    private final Logger log = LoggerFactory.getLogger(BooleanParameterService.class);

    private BooleanParameterRepository booleanParameterRepository;

    private BooleanParameterMapper booleanParameterMapper;

//    @Autowired
//    public BooleanParameterService(BooleanParameterRepository booleanParameterRepository, BooleanParameterMapper booleanParameterMapper) {
//        this.booleanParameterRepository = booleanParameterRepository;
//        this.booleanParameterMapper = booleanParameterMapper;
//    }

    /**
     * Save a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public BooleanParameterDTO save(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to save BooleanParameter : {}", booleanParameterDTO);
        BooleanParameter booleanParameter = booleanParameterMapper.toEntity(booleanParameterDTO);
        booleanParameter = booleanParameterRepository.save(booleanParameter);
        return booleanParameterMapper.toDto(booleanParameter);
    }

    /**
     * Update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to save.
     * @return the persisted entity.
     */
    public BooleanParameterDTO update(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to update BooleanParameter : {}", booleanParameterDTO);
        BooleanParameter booleanParameter = booleanParameterMapper.toEntity(booleanParameterDTO);
        booleanParameter = booleanParameterRepository.save(booleanParameter);
        return booleanParameterMapper.toDto(booleanParameter);
    }

    /**
     * Partially update a booleanParameter.
     *
     * @param booleanParameterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BooleanParameterDTO> partialUpdate(BooleanParameterDTO booleanParameterDTO) {
        log.debug("Request to partially update BooleanParameter : {}", booleanParameterDTO);

        return booleanParameterRepository
            .findById(booleanParameterDTO.getParameterTypeDefinitionId())
            .map(existingBooleanParameter -> {
                booleanParameterMapper.partialUpdate(existingBooleanParameter, booleanParameterDTO);

                return existingBooleanParameter;
            })
            .map(booleanParameterRepository::save)
            .map(booleanParameterMapper::toDto);
    }

    /**
     * Get all the booleanParameters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BooleanParameterDTO> findAll() {
        log.debug("Request to get all BooleanParameters");
        return booleanParameterRepository
            .findAll()
            .stream()
            .map(booleanParameterMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one booleanParameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BooleanParameterDTO> findOne(UUID id) {
        log.debug("Request to get BooleanParameter : {}", id);
        return booleanParameterRepository.findById(id).map(booleanParameterMapper::toDto);
    }

    /**
     * Delete the booleanParameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete BooleanParameter : {}", id);
        booleanParameterRepository.deleteById(id);
    }
}
