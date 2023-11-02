package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class CategoricalParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameter.class);
        CategoricalParameter categoricalParameter1 = new CategoricalParameter();
        categoricalParameter1.setParameterTypeDefinitionId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        CategoricalParameter categoricalParameter2 = new CategoricalParameter();
        categoricalParameter2.setParameterTypeDefinitionId(categoricalParameter1.getParameterTypeDefinitionId());
        assertThat(categoricalParameter1).isEqualTo(categoricalParameter2);
        categoricalParameter2.setParameterTypeDefinitionId(UUID.fromString("e3a11245-4397-4b99-9ca5-fc40a4b30ce6"));
        assertThat(categoricalParameter1).isNotEqualTo(categoricalParameter2);
        categoricalParameter1.setParameterTypeDefinitionId(null);
        assertThat(categoricalParameter1).isNotEqualTo(categoricalParameter2);
    }
}
