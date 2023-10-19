package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoricalParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameter.class);
        CategoricalParameter categoricalParameter1 = new CategoricalParameter();
        categoricalParameter1.setId(1L);
        CategoricalParameter categoricalParameter2 = new CategoricalParameter();
        categoricalParameter2.setId(categoricalParameter1.getId());
        assertThat(categoricalParameter1).isEqualTo(categoricalParameter2);
        categoricalParameter2.setId(2L);
        assertThat(categoricalParameter1).isNotEqualTo(categoricalParameter2);
        categoricalParameter1.setId(null);
        assertThat(categoricalParameter1).isNotEqualTo(categoricalParameter2);
    }
}
