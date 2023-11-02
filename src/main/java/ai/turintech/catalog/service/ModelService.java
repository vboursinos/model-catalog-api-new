package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Model}.
 */
@Service
@Transactional
public class ModelService {

    private final Logger log = LoggerFactory.getLogger(ModelService.class);
    @Autowired
    private ModelRepository modelRepository;

    private ModelMapper modelMapper;

//    public ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
//        this.modelRepository = modelRepository;
//        this.modelMapper = modelMapper;
//    }

    /**
     * Save a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelDTO save(ModelDTO modelDTO) {
        log.debug("Request to save Model : {}", modelDTO);
        Model model = modelMapper.toEntity(modelDTO);
        model = modelRepository.save(model);
        return modelMapper.toDto(model);
    }

    /**
     * Update a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public ModelDTO update(ModelDTO modelDTO) {
        log.debug("Request to update Model : {}", modelDTO);
        Model model = modelMapper.toEntity(modelDTO);
        model = modelRepository.save(model);
        return modelMapper.toDto(model);
    }

    /**
     * Partially update a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ModelDTO> partialUpdate(ModelDTO modelDTO) {
        log.debug("Request to partially update Model : {}", modelDTO);

        return modelRepository
            .findById(modelDTO.getId())
            .map(existingModel -> {
                modelMapper.partialUpdate(existingModel, modelDTO);

                return existingModel;
            })
            .map(modelRepository::save)
            .map(modelMapper::toDto);
    }

    /**
     * Get all the models.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Models");
        return modelRepository.findAll(pageable).map(modelMapper::toDto);
    }

    /**
     * Get all the models with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return modelRepository.findAllWithEagerRelationships(pageable).map(modelMapper::toDto);
    }

    /**
     * Get one model by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ModelDTO> findOne(UUID id) {
        log.debug("Request to get Model : {}", id);
        return modelRepository.findOneWithEagerRelationships(id).map(modelMapper::toDto);
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        log.debug("Request to delete Model : {}", id);
        modelRepository.deleteById(id);
    }
}
