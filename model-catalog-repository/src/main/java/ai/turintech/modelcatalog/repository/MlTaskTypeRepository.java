package ai.turintech.modelcatalog.repository;

import ai.turintech.modelcatalog.entity.MlTaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the MlTaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlTaskTypeRepository extends JpaRepository<MlTaskType, UUID> {}