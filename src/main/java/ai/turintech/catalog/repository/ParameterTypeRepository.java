package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ParameterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the ParameterType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterTypeRepository extends JpaRepository<ParameterType, UUID> {}
