package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.IntegerParameterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the IntegerParameterValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegerParameterValueRepository extends JpaRepository<IntegerParameterValue, UUID> {}
