package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelEnsembleTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelEnsembleType.class);
        ModelEnsembleType modelEnsembleType1 = new ModelEnsembleType();
        modelEnsembleType1.setId(UUID.randomUUID());
        ModelEnsembleType modelEnsembleType2 = new ModelEnsembleType();
        modelEnsembleType2.setId(modelEnsembleType1.getId());
        assertThat(modelEnsembleType1).isEqualTo(modelEnsembleType2);
        modelEnsembleType2.setId(UUID.randomUUID());
        assertThat(modelEnsembleType1).isNotEqualTo(modelEnsembleType2);
        modelEnsembleType1.setId(null);
        assertThat(modelEnsembleType1).isNotEqualTo(modelEnsembleType2);
    }
}
