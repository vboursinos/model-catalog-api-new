package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.dto.FloatParameterDTO;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FloatParameterService {
  /**
   * Save a floatParameter.
   *
   * @param floatParameterDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<FloatParameterDTO> save(FloatParameterDTO floatParameterDTO);

  /**
   * Update a floatParameter.
   *
   * @param floatParameterDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<FloatParameterDTO> update(FloatParameterDTO floatParameterDTO);

  /**
   * Partially update a floatParameter.
   *
   * @param floatParameterDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Mono<FloatParameterDTO> partialUpdate(FloatParameterDTO floatParameterDTO);

  /**
   * Get all the floatParameters.
   *
   * @return the list of entities.
   */
  public Mono<List<FloatParameterDTO>> findAll();

  public Flux<FloatParameterDTO> findAllStream();

  /**
   * Get one floatParameter by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  public Mono<FloatParameterDTO> findOne(UUID id);

  /**
   * Delete the floatParameter by id.
   *
   * @param id the id of the entity.
   */
  public Mono<Void> delete(UUID id);

  /**
   * Search for the floatParameter corresponding to the query.
   *
   * @param id the id of the floatParameter.
   * @return boolean true if exists
   */
  public Mono<Boolean> existsById(UUID id);
}
