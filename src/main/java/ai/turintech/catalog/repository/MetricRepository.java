package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Metric;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Metric entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetricRepository extends ReactiveCrudRepository<Metric, UUID>, MetricRepositoryInternal {
    @Override
    <S extends Metric> Mono<S> save(S entity);

    @Override
    Flux<Metric> findAll();

    @Override
    Mono<Metric> findById(UUID id);

    @Override
    Mono<Void> deleteById(UUID id);
}

interface MetricRepositoryInternal {
    <S extends Metric> Mono<S> save(S entity);

    Flux<Metric> findAllBy(Pageable pageable);

    Flux<Metric> findAll();

    Mono<Metric> findById(UUID id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Metric> findAllBy(Pageable pageable, Criteria criteria);
}
