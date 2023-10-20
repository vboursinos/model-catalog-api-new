package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.MlTaskType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the MlTaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlTaskTypeRepository extends ReactiveCrudRepository<MlTaskType, UUID>, MlTaskTypeRepositoryInternal {
    @Query("SELECT * FROM ml_task_type entity WHERE entity.models_id = :id")
    Flux<MlTaskType> findByModels(UUID id);

    @Query("SELECT * FROM ml_task_type entity WHERE entity.models_id IS NULL")
    Flux<MlTaskType> findAllWhereModelsIsNull();

    @Override
    <S extends MlTaskType> Mono<S> save(S entity);

    @Override
    Flux<MlTaskType> findAll();

    @Override
    Mono<MlTaskType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MlTaskTypeRepositoryInternal {
    <S extends MlTaskType> Mono<S> save(S entity);

    Flux<MlTaskType> findAllBy(Pageable pageable);

    Flux<MlTaskType> findAll();

    Mono<MlTaskType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<MlTaskType> findAllBy(Pageable pageable, Criteria criteria);
}
