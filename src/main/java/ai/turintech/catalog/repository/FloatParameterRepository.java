package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.FloatParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the FloatParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FloatParameterRepository extends JpaRepository<FloatParameter, UUID> {}
