package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelStructureTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelStructureType.class);
        ModelStructureType modelStructureType1 = new ModelStructureType();
        modelStructureType1.setId(UUID.randomUUID());
        ModelStructureType modelStructureType2 = new ModelStructureType();
        modelStructureType2.setId(modelStructureType1.getId());
        assertThat(modelStructureType1).isEqualTo(modelStructureType2);
        modelStructureType2.setId(UUID.randomUUID());
        assertThat(modelStructureType1).isNotEqualTo(modelStructureType2);
        modelStructureType1.setId(null);
        assertThat(modelStructureType1).isNotEqualTo(modelStructureType2);
    }
}
