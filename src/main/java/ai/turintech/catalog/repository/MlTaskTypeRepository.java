package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.MlTaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA repository for the MlTaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlTaskTypeRepository extends JpaRepository<MlTaskType, UUID> {}
