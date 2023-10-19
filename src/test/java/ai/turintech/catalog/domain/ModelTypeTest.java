package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelType.class);
        ModelType modelType1 = new ModelType();
        modelType1.setId(UUID.randomUUID());
        ModelType modelType2 = new ModelType();
        modelType2.setId(modelType1.getId());
        assertThat(modelType1).isEqualTo(modelType2);
        modelType2.setId(UUID.randomUUID());
        assertThat(modelType1).isNotEqualTo(modelType2);
        modelType1.setId(null);
        assertThat(modelType1).isNotEqualTo(modelType2);
    }
}
