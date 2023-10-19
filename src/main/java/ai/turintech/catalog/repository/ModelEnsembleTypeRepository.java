package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelEnsembleType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ModelEnsembleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelEnsembleTypeRepository extends ReactiveCrudRepository<ModelEnsembleType, UUID>, ModelEnsembleTypeRepositoryInternal {
    @Override
    <S extends ModelEnsembleType> Mono<S> save(S entity);

    @Override
    Flux<ModelEnsembleType> findAll();

    @Override
    Mono<ModelEnsembleType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ModelEnsembleTypeRepositoryInternal {
    <S extends ModelEnsembleType> Mono<S> save(S entity);

    Flux<ModelEnsembleType> findAllBy(Pageable pageable);

    Flux<ModelEnsembleType> findAll();

    Mono<ModelEnsembleType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ModelEnsembleType> findAllBy(Pageable pageable, Criteria criteria);
}
