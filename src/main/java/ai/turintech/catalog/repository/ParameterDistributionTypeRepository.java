package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ParameterDistributionType;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ParameterDistributionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterDistributionTypeRepository
    extends ReactiveCrudRepository<ParameterDistributionType, UUID>, ParameterDistributionTypeRepositoryInternal {
    @Override
    <S extends ParameterDistributionType> Mono<S> save(S entity);

    @Override
    Flux<ParameterDistributionType> findAll();

    @Override
    Mono<ParameterDistributionType> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface ParameterDistributionTypeRepositoryInternal {
    <S extends ParameterDistributionType> Mono<S> save(S entity);

    Flux<ParameterDistributionType> findAllBy(Pageable pageable);

    Flux<ParameterDistributionType> findAll();

    Mono<ParameterDistributionType> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ParameterDistributionType> findAllBy(Pageable pageable, Criteria criteria);
}
