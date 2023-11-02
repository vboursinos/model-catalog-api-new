package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.FloatParameterRange;
import ai.turintech.catalog.repository.FloatParameterRangeRepository;
import ai.turintech.catalog.service.dto.FloatParameterRangeDTO;
import ai.turintech.catalog.service.mapper.FloatParameterRangeMapper;
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
 * Service Implementation for managing {@link FloatParameterRange}.
 */
@Service
@Transactional
public class FloatParameterRangeService {

    private final Logger log = LoggerFactory.getLogger(FloatParameterRangeService.class);

    private FloatParameterRangeRepository floatParameterRangeRepository;

    private FloatParameterRangeMapper floatParameterRangeMapper;

//    public FloatParameterRangeService(
//        FloatParameterRangeRepository floatParameterRangeRepository,
//        FloatParameterRangeMapper floatParameterRangeMapper
//    ) {
//        this.floatParameterRangeRepository = floatParameterRangeRepository;
//        this.floatParameterRangeMapper = floatParameterRangeMapper;
//    }

    /**
     * Save a floatParameterRange.
     *
     * @param floatParameterRangeDTO the entity to save.
     * @return the persisted entity.
     */
    public FloatParameterRangeDTO save(FloatParameterRangeDTO floatParameterRangeDTO) {
        log.debug("Request to save FloatParameterRange : {}", floatParameterRangeDTO);
        FloatParameterRange floatParameterRange = floatParameterRangeMapper.toEntity(floatParameterRangeDTO);
        floatParameterRange = floatParameterRangeRepository.save(floatParameterRange);
        return floatParameterRangeMapper.toDto(floatParameterRange);
    }

    /**
     * Update a floatParameterRange.
     *
     * @param floatParameterRangeDTO the entity to save.
     * @return the persisted entity.
     */
    public FloatParameterRangeDTO update(FloatParameterRangeDTO floatParameterRangeDTO) {
        log.debug("Request to update FloatParameterRange : {}", floatParameterRangeDTO);
        FloatParameterRange floatParameterRange = floatParameterRangeMapper.toEntity(floatParameterRangeDTO);
        floatParameterRange = floatParameterRangeRepository.save(floatParameterRange);
        return floatParameterRangeMapper.toDto(floatParameterRange);
    }

    /**
     * Partially update a floatParameterRange.
     *
     * @param floatParameterRangeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FloatParameterRangeDTO> partialUpdate(FloatParameterRangeDTO floatParameterRangeDTO) {
        log.debug("Request to partially update FloatParameterRange : {}", floatParameterRangeDTO);

        return floatParameterRangeRepository
            .findById(floatParameterRangeDTO.getId())
            .map(existingFloatParameterRange -> {
                floatParameterRangeMapper.partialUpdate(existingFloatParameterRange, floatParameterRangeDTO);

                return existingFloatParameterRange;
            })
            .map(floatParameterRangeRepository::save)
            .map(floatParameterRangeMapper::toDto);
    }

    /**
     * Get all the floatParameterRanges.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FloatParameterRangeDTO> findAll() {
        log.debug("Request to get all FloatParameterRanges");
        return floatParameterRangeRepository
            .findAll()
            .stream()
            .map(floatParameterRangeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one floatParameterRange by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FloatParameterRangeDTO> findOne(UUID id) {
        log.debug("Request to get FloatParameterRange : {}", id);
        return floatParameterRangeRepository.findById(id).map(floatParameterRangeMapper::toDto);
    }

    /**
     * Delete the floatParameterRange by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete FloatParameterRange : {}", id);
        floatParameterRangeRepository.deleteById(id);
    }
}
