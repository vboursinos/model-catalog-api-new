package ai.turintech.modelcatalog.service;

import ai.turintech.modelcatalog.dto.ParameterDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParameterService {

  /**
   * Save a parameter.
   *
   * @param parameterDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<ParameterDTO> save(ParameterDTO parameterDTO);

  /**
   * Update a parameter.
   *
   * @param parameterDTO the entity to save.
   * @return the persisted entity.
   */
  public Mono<ParameterDTO> update(ParameterDTO parameterDTO);

  /**
   * Partially update a parameter.
   *
   * @param parameterDTO the entity to update partially.
   * @return the persisted entity.
   */
  public Mono<Optional<ParameterDTO>> partialUpdate(ParameterDTO parameterDTO);

  /**
   * Get all the parameters.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  public Mono<List<ParameterDTO>> findAll(Pageable pageable);

  public Flux<ParameterDTO> findAllStream(Pageable pageable);

  /**
   * Get one parameter by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  public Mono<ParameterDTO> findOne(UUID id);

  /**
   * Delete the parameter by id.
   *
   * @param id the id of the entity.
   */
  public Mono<Void> delete(UUID id);

  public Mono<Boolean> existsById(UUID id);
}
