package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MetricDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetricDTO.class);
        MetricDTO metricDTO1 = new MetricDTO();
        metricDTO1.setId(UUID.randomUUID());
        MetricDTO metricDTO2 = new MetricDTO();
        assertThat(metricDTO1).isNotEqualTo(metricDTO2);
        metricDTO2.setId(metricDTO1.getId());
        assertThat(metricDTO1).isEqualTo(metricDTO2);
        metricDTO2.setId(UUID.randomUUID());
        assertThat(metricDTO1).isNotEqualTo(metricDTO2);
        metricDTO1.setId(null);
        assertThat(metricDTO1).isNotEqualTo(metricDTO2);
    }
}
