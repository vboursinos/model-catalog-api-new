package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloatParameterRangeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloatParameterRange.class);
        FloatParameterRange floatParameterRange1 = new FloatParameterRange();
        floatParameterRange1.setId(1L);
        FloatParameterRange floatParameterRange2 = new FloatParameterRange();
        floatParameterRange2.setId(floatParameterRange1.getId());
        assertThat(floatParameterRange1).isEqualTo(floatParameterRange2);
        floatParameterRange2.setId(2L);
        assertThat(floatParameterRange1).isNotEqualTo(floatParameterRange2);
        floatParameterRange1.setId(null);
        assertThat(floatParameterRange1).isNotEqualTo(floatParameterRange2);
    }
}
