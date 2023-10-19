package ai.turintech.catalog.service.mapper;

import ai.turintech.catalog.domain.Metric;
import ai.turintech.catalog.service.dto.MetricDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metric} and its DTO {@link MetricDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetricMapper extends EntityMapper<MetricDTO, Metric> {}
