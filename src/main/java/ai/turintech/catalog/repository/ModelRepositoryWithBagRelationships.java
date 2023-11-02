package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Model;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ModelRepositoryWithBagRelationships {
    Optional<Model> fetchBagRelationships(Optional<Model> model);

    List<Model> fetchBagRelationships(List<Model> models);

    Page<Model> fetchBagRelationships(Page<Model> models);
}
