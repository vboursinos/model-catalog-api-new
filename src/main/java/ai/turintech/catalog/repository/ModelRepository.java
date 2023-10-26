package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Model;

import java.util.List;
import java.util.UUID;

import ai.turintech.catalog.service.dto.FilterDTO;
import ai.turintech.catalog.service.dto.SearchDTO;
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
    Flux<Model> findAllBy(Pageable pageable, FilterDTO filterDTO);

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

    @Query("SELECT * FROM model entity WHERE entity.ml_task_id = :id")
    Flux<Model> findByMlTask(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.ml_task_id IS NULL")
    Flux<Model> findAllWhereMlTaskIsNull();

    @Query("SELECT * FROM model entity WHERE entity.structure_id = :id")
    Flux<Model> findByStructure(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.structure_id IS NULL")
    Flux<Model> findAllWhereStructureIsNull();

    @Query("SELECT * FROM model entity WHERE entity.type_id = :id")
    Flux<Model> findByType(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.type_id IS NULL")
    Flux<Model> findAllWhereTypeIsNull();

    @Query("SELECT * FROM model entity WHERE entity.family_type_id = :id")
    Flux<Model> findByFamilyType(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.family_type_id IS NULL")
    Flux<Model> findAllWhereFamilyTypeIsNull();

    @Query("SELECT * FROM model entity WHERE entity.ensemble_type_id = :id")
    Flux<Model> findByEnsembleType(UUID id);

    @Query("SELECT * FROM model entity WHERE entity.ensemble_type_id IS NULL")
    Flux<Model> findAllWhereEnsembleTypeIsNull();

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

    Flux<Model> findAllBy(Pageable pageable, FilterDTO filterDTO, List<SearchDTO> searchDTO);

    Flux<Model> findAll();

    Mono<Model> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Model> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Model> findOneWithEagerRelationships(UUID id);

    Flux<Model> findAllWithEagerRelationships();

    Mono<Long> count(FilterDTO filterDTO, List<SearchDTO> searchParams);

    Flux<Model> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(UUID id);
}
