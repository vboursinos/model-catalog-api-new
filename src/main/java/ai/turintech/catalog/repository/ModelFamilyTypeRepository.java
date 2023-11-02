package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelFamilyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the ModelFamilyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelFamilyTypeRepository extends JpaRepository<ModelFamilyType, UUID> {}
