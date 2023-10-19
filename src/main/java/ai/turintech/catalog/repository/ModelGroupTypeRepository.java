package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelGroupType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ModelGroupType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelGroupTypeRepository extends ReactiveCrudRepository<ModelGroupType, UUID>, ModelGroupTypeRepositoryInternal {
    @Override
    <S extends ModelGroupType> Mono<S> save(S entity);

    @Override
    Flux<ModelGroupType> findAll();

    @Override
    Mono<ModelGroupType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ModelGroupTypeRepositoryInternal {
    <S extends ModelGroupType> Mono<S> save(S entity);

    Flux<ModelGroupType> findAllBy(Pageable pageable);

    Flux<ModelGroupType> findAll();

    Mono<ModelGroupType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ModelGroupType> findAllBy(Pageable pageable, Criteria criteria);
}
