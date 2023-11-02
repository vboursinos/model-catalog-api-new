package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.FloatParameterRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the FloatParameterRange entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloatParameterRangeRepository extends JpaRepository<FloatParameterRange, UUID> {}
