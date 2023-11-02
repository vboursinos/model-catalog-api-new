package ai.turintech.catalog.service;

import ai.turintech.catalog.domain.Model;
import ai.turintech.catalog.repository.ModelRepository;
import ai.turintech.catalog.service.dto.ModelDTO;
import ai.turintech.catalog.service.mapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

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
        Model model = modelRepository.save(modelMapper.toEntity(modelDTO));
        return Mono.just(modelMapper.toDto(model));
    }

    /**
     * Update a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> update(ModelDTO modelDTO) {
        log.debug("Request to update Model : {}", modelDTO);
        Model model = modelRepository.save(modelMapper.toEntity(modelDTO));
        return Mono.just(modelMapper.toDto(model));
    }

    /**
     * Partially update a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ModelDTO> partialUpdate(ModelDTO modelDTO) {
        log.debug("Request to partially update Model : {}", modelDTO);

        return Mono.justOrEmpty(modelRepository
                .findById(modelDTO.getId())
                .map(existingModel -> {
                    modelMapper.partialUpdate(existingModel, modelDTO);

                    return existingModel;
                })
                .map(modelRepository::save)
                .map(modelMapper::toDto));
    }

    /**
     * Get all the models.
     *
     * @return the list of entities.
     */
/*    @Transactional(readOnly = true)
    public Flux<ModelDTO> findAll(Pageable pageable, FilterDTO filterDTO, List<SearchDTO> searchParams) {
        log.debug("Request to get all Models");
        Flux<ModelDTO> modelDTOs = modelRepository.findAllBy(pageable, filterDTO, searchParams)
                .doOnNext(model -> System.out.println("Original model: " + model))
                .map(modelMapper::toDto);
        modelDTOs.subscribe(modelDTO ->
                System.out.println("ModelDTO: " + modelDTO)
        );
        return modelDTOs;
    }*/
    @Transactional(readOnly = true)
    public Flux<ModelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Models");
        Page<ModelDTO> modelDTOS = modelRepository.findAll(pageable).map(modelMapper::toDto);
        return Flux.fromIterable(modelDTOS);
    }

    @Transactional(readOnly = true)
    public Flux<ModelDTO> findAll() {
        log.debug("Request to get all Models");
        List<Model> model = modelRepository.findAll();
        return Flux.fromIterable(model).map(modelMapper::toDto);
    }


    @Transactional(readOnly = true)
    public Mono<ModelDTO> findOne(UUID id) {
        log.debug("Request to get Model : {}", id);
        return Mono.justOrEmpty(modelRepository.findOneWithEagerRelationships(id).map(modelMapper::toDto));

    }

    /**
     * Delete the model by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public void delete(UUID id) {
        log.debug("Request to delete Model : {}", id);
        modelRepository.deleteById(id);
    }
}
