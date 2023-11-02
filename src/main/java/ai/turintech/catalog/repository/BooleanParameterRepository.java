package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.BooleanParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the BooleanParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BooleanParameterRepository extends JpaRepository<BooleanParameter, UUID> {}
