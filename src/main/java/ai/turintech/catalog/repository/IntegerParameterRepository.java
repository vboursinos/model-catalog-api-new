package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.IntegerParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the IntegerParameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegerParameterRepository extends JpaRepository<IntegerParameter, UUID> {}
