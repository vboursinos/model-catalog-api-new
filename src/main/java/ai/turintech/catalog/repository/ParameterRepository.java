package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Parameter;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Parameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterRepository extends ReactiveCrudRepository<Parameter, UUID>, ParameterRepositoryInternal {
    Flux<Parameter> findAllBy(Pageable pageable);

    @Query("SELECT * FROM parameter entity WHERE entity.model_id = :id")
    Flux<Parameter> findByModel(UUID id);

    @Query("SELECT * FROM parameter entity WHERE entity.model_id IS NULL")
    Flux<Parameter> findAllWhereModelIsNull();

    @Override
    <S extends Parameter> Mono<S> save(S entity);

    @Override
    Flux<Parameter> findAll();

    @Override
    Mono<Parameter> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ParameterRepositoryInternal {
    <S extends Parameter> Mono<S> save(S entity);

    Flux<Parameter> findAllBy(Pageable pageable);

    Flux<Parameter> findAll();

    Mono<Parameter> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Parameter> findAllBy(Pageable pageable, Criteria criteria);
}
