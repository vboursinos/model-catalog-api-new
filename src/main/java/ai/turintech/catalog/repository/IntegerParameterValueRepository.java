package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.IntegerParameterValue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IntegerParameterValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegerParameterValueRepository
    extends ReactiveCrudRepository<IntegerParameterValue, Long>, IntegerParameterValueRepositoryInternal {
    @Query("SELECT * FROM integer_parameter_value entity WHERE entity.integer_parameter_id = :id")
    Flux<IntegerParameterValue> findByIntegerParameter(Long id);

    @Query("SELECT * FROM integer_parameter_value entity WHERE entity.integer_parameter_id IS NULL")
    Flux<IntegerParameterValue> findAllWhereIntegerParameterIsNull();

    @Override
    <S extends IntegerParameterValue> Mono<S> save(S entity);

    @Override
    Flux<IntegerParameterValue> findAll();

    @Override
    Mono<IntegerParameterValue> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IntegerParameterValueRepositoryInternal {
    <S extends IntegerParameterValue> Mono<S> save(S entity);

    Flux<IntegerParameterValue> findAllBy(Pageable pageable);

    Flux<IntegerParameterValue> findAll();

    Mono<IntegerParameterValue> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IntegerParameterValue> findAllBy(Pageable pageable, Criteria criteria);
}
