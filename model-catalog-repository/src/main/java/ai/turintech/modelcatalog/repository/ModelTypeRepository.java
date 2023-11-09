package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.ModelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the ModelType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelTypeRepository extends JpaRepository<ModelType, UUID> {}
