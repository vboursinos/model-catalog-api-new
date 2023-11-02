package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.ModelStructureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the ModelStructureType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelStructureTypeRepository extends JpaRepository<ModelStructureType, UUID> {}
