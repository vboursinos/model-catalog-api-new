package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.BooleanParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the BooleanParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooleanParameterRepository extends ReactiveCrudRepository<BooleanParameter, Long>, BooleanParameterRepositoryInternal {
    @Query("SELECT * FROM boolean_parameter entity WHERE entity.parameter_type_definition_id = :id")
    Flux<BooleanParameter> findByParameterTypeDefinition(Long id);

    @Query("SELECT * FROM boolean_parameter entity WHERE entity.parameter_type_definition_id IS NULL")
    Flux<BooleanParameter> findAllWhereParameterTypeDefinitionIsNull();

    @Override
    <S extends BooleanParameter> Mono<S> save(S entity);

    @Override
    Flux<BooleanParameter> findAll();

    @Override
    Mono<BooleanParameter> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface BooleanParameterRepositoryInternal {
    <S extends BooleanParameter> Mono<S> save(S entity);

    Flux<BooleanParameter> findAllBy(Pageable pageable);

    Flux<BooleanParameter> findAll();

    Mono<BooleanParameter> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<BooleanParameter> findAllBy(Pageable pageable, Criteria criteria);
}
