package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MetricTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Metric.class);
        Metric metric1 = new Metric();
        metric1.setId(UUID.randomUUID());
        Metric metric2 = new Metric();
        metric2.setId(metric1.getId());
        assertThat(metric1).isEqualTo(metric2);
        metric2.setId(UUID.randomUUID());
        assertThat(metric1).isNotEqualTo(metric2);
        metric1.setId(null);
        assertThat(metric1).isNotEqualTo(metric2);
    }
}
