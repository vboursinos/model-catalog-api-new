package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelGroupTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelGroupType.class);
        ModelGroupType modelGroupType1 = new ModelGroupType();
        modelGroupType1.setId(UUID.randomUUID());
        ModelGroupType modelGroupType2 = new ModelGroupType();
        modelGroupType2.setId(modelGroupType1.getId());
        assertThat(modelGroupType1).isEqualTo(modelGroupType2);
        modelGroupType2.setId(UUID.randomUUID());
        assertThat(modelGroupType1).isNotEqualTo(modelGroupType2);
        modelGroupType1.setId(null);
        assertThat(modelGroupType1).isNotEqualTo(modelGroupType2);
    }
}
