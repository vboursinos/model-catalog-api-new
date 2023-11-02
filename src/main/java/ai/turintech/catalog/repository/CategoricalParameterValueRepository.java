package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.CategoricalParameterValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the CategoricalParameterValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoricalParameterValueRepository extends JpaRepository<CategoricalParameterValue, UUID> {}
