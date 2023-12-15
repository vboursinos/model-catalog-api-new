package ai.turintech.modelcatalog.service;

import ai.turintech.components.architecture.reactive.ReactiveAbstractUUIDIdentityCrudService;
import ai.turintech.modelcatalog.dto.ParameterDTO;
import ai.turintech.modelcatalog.entity.Parameter;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ParameterService
    extends ReactiveAbstractUUIDIdentityCrudService<ParameterDTO, Parameter, UUID> {

  /**
   * Get all the parameters in Mono.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  public Mono<List<ParameterDTO>> findAllPageable(Pageable pageable);

  /**
   * Get all the parameters in pages as a steam.
   *
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  public Flux<ParameterDTO> findAllStream(Pageable pageable);
}