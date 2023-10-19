package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloatParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloatParameter.class);
        FloatParameter floatParameter1 = new FloatParameter();
        floatParameter1.setId(1L);
        FloatParameter floatParameter2 = new FloatParameter();
        floatParameter2.setId(floatParameter1.getId());
        assertThat(floatParameter1).isEqualTo(floatParameter2);
        floatParameter2.setId(2L);
        assertThat(floatParameter1).isNotEqualTo(floatParameter2);
        floatParameter1.setId(null);
        assertThat(floatParameter1).isNotEqualTo(floatParameter2);
    }
}
