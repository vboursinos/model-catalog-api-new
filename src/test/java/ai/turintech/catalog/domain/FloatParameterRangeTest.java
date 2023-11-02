package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class FloatParameterRangeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloatParameterRange.class);
        FloatParameterRange floatParameterRange1 = new FloatParameterRange();
        floatParameterRange1.setId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        FloatParameterRange floatParameterRange2 = new FloatParameterRange();
        floatParameterRange2.setId(floatParameterRange1.getId());
        assertThat(floatParameterRange1).isEqualTo(floatParameterRange2);
        floatParameterRange2.setId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        assertThat(floatParameterRange1).isNotEqualTo(floatParameterRange2);
        floatParameterRange1.setId(null);
        assertThat(floatParameterRange1).isNotEqualTo(floatParameterRange2);
    }
}
