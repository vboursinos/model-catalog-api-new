package ai.turintech.catalog.repository;

import ai.turintech.catalog.domain.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ModelRepositoryWithBagRelationshipsImpl implements ModelRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Model> fetchBagRelationships(Optional<Model> model) {
        return model.map(this::fetchGroups).map(this::fetchIncompatibleMetrics);
    }

    @Override
    public Page<Model> fetchBagRelationships(Page<Model> models) {
        return new PageImpl<>(fetchBagRelationships(models.getContent()), models.getPageable(), models.getTotalElements());
    }

    @Override
    public List<Model> fetchBagRelationships(List<Model> models) {
        return Optional.of(models).map(this::fetchGroups).map(this::fetchIncompatibleMetrics).orElse(Collections.emptyList());
    }

    Model fetchGroups(Model result) {
        return entityManager
            .createQuery("select model from Model model left join fetch model.groups where model.id = :id", Model.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Model> fetchGroups(List<Model> models) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, models.size()).forEach(index -> order.put(models.get(index).getId(), index));
        List<Model> result = entityManager
            .createQuery("select model from Model model left join fetch model.groups where model in :models", Model.class)
            .setParameter("models", models)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Model fetchIncompatibleMetrics(Model result) {
        return entityManager
            .createQuery("select model from Model model left join fetch model.incompatibleMetrics where model.id = :id", Model.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Model> fetchIncompatibleMetrics(List<Model> models) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, models.size()).forEach(index -> order.put(models.get(index).getId(), index));
        List<Model> result = entityManager
            .createQuery("select model from Model model left join fetch model.incompatibleMetrics where model in :models", Model.class)
            .setParameter("models", models)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
