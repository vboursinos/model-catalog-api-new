package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelFamilyType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ModelFamilyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelFamilyTypeRepository extends ReactiveCrudRepository<ModelFamilyType, UUID>, ModelFamilyTypeRepositoryInternal {
    @Override
    <S extends ModelFamilyType> Mono<S> save(S entity);

    @Override
    Flux<ModelFamilyType> findAll();

    @Override
    Mono<ModelFamilyType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ModelFamilyTypeRepositoryInternal {
    <S extends ModelFamilyType> Mono<S> save(S entity);

    Flux<ModelFamilyType> findAllBy(Pageable pageable);

    Flux<ModelFamilyType> findAll();

    Mono<ModelFamilyType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ModelFamilyType> findAllBy(Pageable pageable, Criteria criteria);
}
