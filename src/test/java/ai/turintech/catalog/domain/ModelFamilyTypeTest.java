package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelFamilyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelFamilyType.class);
        ModelFamilyType modelFamilyType1 = new ModelFamilyType();
        modelFamilyType1.setId(UUID.randomUUID());
        ModelFamilyType modelFamilyType2 = new ModelFamilyType();
        modelFamilyType2.setId(modelFamilyType1.getId());
        assertThat(modelFamilyType1).isEqualTo(modelFamilyType2);
        modelFamilyType2.setId(UUID.randomUUID());
        assertThat(modelFamilyType1).isNotEqualTo(modelFamilyType2);
        modelFamilyType1.setId(null);
        assertThat(modelFamilyType1).isNotEqualTo(modelFamilyType2);
    }
}
