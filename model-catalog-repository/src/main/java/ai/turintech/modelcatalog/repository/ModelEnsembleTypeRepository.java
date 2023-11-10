package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.ModelEnsembleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the ModelEnsembleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelEnsembleTypeRepository extends JpaRepository<ModelEnsembleType, UUID> {}