package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoricalParameterValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameterValue.class);
        CategoricalParameterValue categoricalParameterValue1 = new CategoricalParameterValue();
        categoricalParameterValue1.setId(1L);
        CategoricalParameterValue categoricalParameterValue2 = new CategoricalParameterValue();
        categoricalParameterValue2.setId(categoricalParameterValue1.getId());
        assertThat(categoricalParameterValue1).isEqualTo(categoricalParameterValue2);
        categoricalParameterValue2.setId(2L);
        assertThat(categoricalParameterValue1).isNotEqualTo(categoricalParameterValue2);
        categoricalParameterValue1.setId(null);
        assertThat(categoricalParameterValue1).isNotEqualTo(categoricalParameterValue2);
    }
}
