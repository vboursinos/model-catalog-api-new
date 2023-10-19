package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelEnsembleTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelEnsembleTypeDTO.class);
        ModelEnsembleTypeDTO modelEnsembleTypeDTO1 = new ModelEnsembleTypeDTO();
        modelEnsembleTypeDTO1.setId(UUID.randomUUID());
        ModelEnsembleTypeDTO modelEnsembleTypeDTO2 = new ModelEnsembleTypeDTO();
        assertThat(modelEnsembleTypeDTO1).isNotEqualTo(modelEnsembleTypeDTO2);
        modelEnsembleTypeDTO2.setId(modelEnsembleTypeDTO1.getId());
        assertThat(modelEnsembleTypeDTO1).isEqualTo(modelEnsembleTypeDTO2);
        modelEnsembleTypeDTO2.setId(UUID.randomUUID());
        assertThat(modelEnsembleTypeDTO1).isNotEqualTo(modelEnsembleTypeDTO2);
        modelEnsembleTypeDTO1.setId(null);
        assertThat(modelEnsembleTypeDTO1).isNotEqualTo(modelEnsembleTypeDTO2);
    }
}
