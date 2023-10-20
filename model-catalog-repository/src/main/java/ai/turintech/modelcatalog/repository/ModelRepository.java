package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.Model;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Model entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelRepository extends ReactiveCrudRepository<Model, UUID>, ModelRepositoryInternal {
    Flux<Model> findAllBy(Pageable pageable);

    @Override
    Mono<Model> findOneWithEagerRelationships(UUID id);

    @Override
    Flux<Model> findAllWithEagerRelationships();

    @Override
    Flux<Model> findAllWithEagerRelationships(Pageable page);

    @Query(
        "SELECT entity.* FROM model entity JOIN rel_model__groups joinTable ON entity.id = joinTable.groups_id WHERE joinTable.groups_id = :id"
    )
    Flux<Model> findByGroups(UUID id);

    @Query(
        "SELECT entity.* FROM model entity JOIN rel_model__incompatible_metrics joinTable ON entity.id = joinTable.incompatible_metrics_id WHERE joinTable.incompatible_metrics_id = :id"
    )
    Flux<Model> findByIncompatibleMetrics(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.parameters_id = :id")
    Flux<Model> findByParameters(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.parameters_id IS NULL")
    Flux<Model> findAllWhereParametersIsNull();

    @Override
    <S extends Model> Mono<S> save(S entity);

    @Override
    Flux<Model> findAll();

    @Override
    Mono<Model> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ModelRepositoryInternal {
    <S extends Model> Mono<S> save(S entity);

    Flux<Model> findAllBy(Pageable pageable);

    Flux<Model> findAll();

    Mono<Model> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Model> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Model> findOneWithEagerRelationships(UUID id);

    Flux<Model> findAllWithEagerRelationships();

    Flux<Model> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(UUID id);
}
