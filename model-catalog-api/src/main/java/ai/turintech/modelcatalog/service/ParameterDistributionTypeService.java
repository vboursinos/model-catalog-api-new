package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.dto.ParameterDistributionTypeDTO;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParameterDistributionTypeService {

  /**
   * Save a parameterDistributionType.
   *
   * @param parameterDistributionTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<ParameterDistributionTypeDTO> save(
      ParameterDistributionTypeDTO parameterDistributionTypeDTO);

  /**
   * Update a parameterDistributionType.
   *
   * @param parameterDistributionTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<ParameterDistributionTypeDTO> update(
      ParameterDistributionTypeDTO parameterDistributionTypeDTO);

  /**
   * Partially update a parameterDistributionType.
   *
   * @param parameterDistributionTypeDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Mono<ParameterDistributionTypeDTO> partialUpdate(
      ParameterDistributionTypeDTO parameterDistributionTypeDTO);

  /**
   * Get all the parameterDistributionTypes.
   *
   * @return the list of entities.
   */
  public Mono<List<ParameterDistributionTypeDTO>> findAll();

  public Flux<ParameterDistributionTypeDTO> findAllStream();

  /**
   * Get one parameterDistributionType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  public Mono<ParameterDistributionTypeDTO> findOne(UUID id);

  /**
   * Delete the parameterDistributionType by id.
   *
   * @param id the id of the entity.
   */
  public Mono<Void> delete(UUID id);

  public Mono<Boolean> existsById(UUID id);
}
