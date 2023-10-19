package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ParameterType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ParameterType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterTypeRepository extends ReactiveCrudRepository<ParameterType, UUID>, ParameterTypeRepositoryInternal {
    @Override
    <S extends ParameterType> Mono<S> save(S entity);

    @Override
    Flux<ParameterType> findAll();

    @Override
    Mono<ParameterType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ParameterTypeRepositoryInternal {
    <S extends ParameterType> Mono<S> save(S entity);

    Flux<ParameterType> findAllBy(Pageable pageable);

    Flux<ParameterType> findAll();

    Mono<ParameterType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ParameterType> findAllBy(Pageable pageable, Criteria criteria);
}
