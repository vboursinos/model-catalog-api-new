package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CategoricalParameterValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameterValue.class);
        CategoricalParameterValue categoricalParameterValue1 = new CategoricalParameterValue();
        categoricalParameterValue1.setId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        CategoricalParameterValue categoricalParameterValue2 = new CategoricalParameterValue();
        categoricalParameterValue2.setId(categoricalParameterValue1.getId());
        assertThat(categoricalParameterValue1).isEqualTo(categoricalParameterValue2);
        categoricalParameterValue2.setId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        assertThat(categoricalParameterValue1).isNotEqualTo(categoricalParameterValue2);
        categoricalParameterValue1.setId(null);
        assertThat(categoricalParameterValue1).isNotEqualTo(categoricalParameterValue2);
    }
}
