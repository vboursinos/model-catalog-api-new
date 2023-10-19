package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ParameterTypeDefinition;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ParameterTypeDefinition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterTypeDefinitionRepository
    extends ReactiveCrudRepository<ParameterTypeDefinition, UUID>, ParameterTypeDefinitionRepositoryInternal {
    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.id not in (select integer_parameter_id from integer_parameter)")
    Flux<ParameterTypeDefinition> findAllWhereIntegerParameterIsNull();

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.id not in (select float_parameter_id from float_parameter)")
    Flux<ParameterTypeDefinition> findAllWhereFloatParameterIsNull();

    @Query(
        "SELECT * FROM parameter_type_definition entity WHERE entity.id not in (select categorical_parameter_id from categorical_parameter)"
    )
    Flux<ParameterTypeDefinition> findAllWhereCategoricalParameterIsNull();

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.id not in (select boolean_parameter_id from boolean_parameter)")
    Flux<ParameterTypeDefinition> findAllWhereBooleanParameterIsNull();

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.distribution_id = :id")
    Flux<ParameterTypeDefinition> findByDistribution(UUID id);

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.distribution_id IS NULL")
    Flux<ParameterTypeDefinition> findAllWhereDistributionIsNull();

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.parameter_id = :id")
    Flux<ParameterTypeDefinition> findByParameter(UUID id);

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.parameter_id IS NULL")
    Flux<ParameterTypeDefinition> findAllWhereParameterIsNull();

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.type_id = :id")
    Flux<ParameterTypeDefinition> findByType(UUID id);

    @Query("SELECT * FROM parameter_type_definition entity WHERE entity.type_id IS NULL")
    Flux<ParameterTypeDefinition> findAllWhereTypeIsNull();

    @Override
    <S extends ParameterTypeDefinition> Mono<S> save(S entity);

    @Override
    Flux<ParameterTypeDefinition> findAll();

    @Override
    Mono<ParameterTypeDefinition> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ParameterTypeDefinitionRepositoryInternal {
    <S extends ParameterTypeDefinition> Mono<S> save(S entity);

    Flux<ParameterTypeDefinition> findAllBy(Pageable pageable);

    Flux<ParameterTypeDefinition> findAll();

    Mono<ParameterTypeDefinition> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ParameterTypeDefinition> findAllBy(Pageable pageable, Criteria criteria);
}
