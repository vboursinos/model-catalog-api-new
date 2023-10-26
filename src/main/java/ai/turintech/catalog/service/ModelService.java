package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.dto.FilterDTO;
import ai.turintech.catalog.service.dto.SearchDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ai.turintech.catalog.domain.Model}.
 */
@Service
@Transactional
public class ModelService {
    private final Logger log = LoggerFactory.getLogger(ModelService.class);

    private final ModelRepository modelRepository;

    private final ModelMapper modelMapper;

    public ModelService(ModelRepository modelRepository, ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Save a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> save(ModelDTO modelDTO) {
        log.debug("Request to save Model : {}", modelDTO);
        return modelRepository.save(modelMapper.toEntity(modelDTO)).map(modelMapper::toDto);
    }

    /**
     * Update a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> update(ModelDTO modelDTO) {
        log.debug("Request to update Model : {}", modelDTO);
        return modelRepository.save(modelMapper.toEntity(modelDTO).setIsPersisted()).map(modelMapper::toDto);
    }

    /**
     * Partially update a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> partialUpdate(ModelDTO modelDTO) {
        log.debug("Request to partially update Model : {}", modelDTO);

        return modelRepository
            .findById(modelDTO.getId())
            .map(existingModel -> {
                modelMapper.partialUpdate(existingModel, modelDTO);

                return existingModel;
            })
            .flatMap(modelRepository::save)
            .map(modelMapper::toDto);
    }

    /**
     * Get all the models.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ModelDTO> findAll(Pageable pageable, FilterDTO filterDTO, List<SearchDTO> searchParams) {
        log.debug("Request to get all Models");
        Flux<ModelDTO> modelDTOs = modelRepository.findAllBy(pageable, filterDTO, searchParams)
                .doOnNext(model -> System.out.println("Original model: " + model))
                .map(modelMapper::toDto);
        modelDTOs.subscribe(modelDTO ->
                System.out.println("ModelDTO: " + modelDTO)
        );
        return modelDTOs;
    }

    /**
     * Get all the models with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<ModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        Flux<ModelDTO> modelDTOs = modelRepository.findAllWithEagerRelationships(pageable).map(modelMapper::toDto);
        modelDTOs.subscribe(modelDTO -> {
            System.out.println("Model found: ");
            System.out.println(modelDTO); // Print the ModelDTO when it's available
        });
        return modelDTOs;
    }

    /**
     * Returns the number of models available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return modelRepository.count();
    }

    public Mono<Long> countAll(FilterDTO filterDTO, List<SearchDTO> searchParams) {
        return modelRepository.count(filterDTO, searchParams);
    }
    /**
     * Get one model by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
//    @Transactional(readOnly = true)
//    public Mono<ModelDTO> findOne(UUID id) {
//        log.debug("Request to get Model : {}", id);
//        return modelRepository.findOneWithEagerRelationships(id).map(modelMapper::toDto);
//    }

    @Transactional(readOnly = true)
    public Mono<ModelDTO> findOne(UUID id) {
        log.debug("Request to get Model : {}", id);
        // Retrieve the model entity from the repository
        Mono<Model> modelMono = modelRepository.findOneWithEagerRelationships(id);
        modelMono.subscribe(model -> {
            System.out.println("Model with parameters found: ");
            System.out.println(model); // Print the ModelDTO when it's available
        });
        // Map the model entity to a ModelDTO
        Mono<ModelDTO> modelDTOMono = modelMono.map(model -> {
            // Perform the mapping from the entity to DTO
            ModelDTO modelDTO = modelMapper.toDto(model);
            return modelDTO;
        });

        // Return the Mono containing the ModelDTO
        return modelDTOMono;
    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(UUID id) {
        log.debug("Request to delete Model : {}", id);
        return modelRepository.deleteById(id);
    }
}
