package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.FloatParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FloatParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloatParameterRepository extends ReactiveCrudRepository<FloatParameter, Long>, FloatParameterRepositoryInternal {
    @Query("SELECT * FROM float_parameter entity WHERE entity.parameter_type_definition_id = :id")
    Flux<FloatParameter> findByParameterTypeDefinition(Long id);

    @Query("SELECT * FROM float_parameter entity WHERE entity.parameter_type_definition_id IS NULL")
    Flux<FloatParameter> findAllWhereParameterTypeDefinitionIsNull();

    @Override
    <S extends FloatParameter> Mono<S> save(S entity);

    @Override
    Flux<FloatParameter> findAll();

    @Override
    Mono<FloatParameter> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FloatParameterRepositoryInternal {
    <S extends FloatParameter> Mono<S> save(S entity);

    Flux<FloatParameter> findAllBy(Pageable pageable);

    Flux<FloatParameter> findAll();

    Mono<FloatParameter> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FloatParameter> findAllBy(Pageable pageable, Criteria criteria);
}
