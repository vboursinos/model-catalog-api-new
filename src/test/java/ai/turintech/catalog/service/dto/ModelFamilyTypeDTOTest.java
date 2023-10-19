package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelFamilyTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelFamilyTypeDTO.class);
        ModelFamilyTypeDTO modelFamilyTypeDTO1 = new ModelFamilyTypeDTO();
        modelFamilyTypeDTO1.setId(UUID.randomUUID());
        ModelFamilyTypeDTO modelFamilyTypeDTO2 = new ModelFamilyTypeDTO();
        assertThat(modelFamilyTypeDTO1).isNotEqualTo(modelFamilyTypeDTO2);
        modelFamilyTypeDTO2.setId(modelFamilyTypeDTO1.getId());
        assertThat(modelFamilyTypeDTO1).isEqualTo(modelFamilyTypeDTO2);
        modelFamilyTypeDTO2.setId(UUID.randomUUID());
        assertThat(modelFamilyTypeDTO1).isNotEqualTo(modelFamilyTypeDTO2);
        modelFamilyTypeDTO1.setId(null);
        assertThat(modelFamilyTypeDTO1).isNotEqualTo(modelFamilyTypeDTO2);
    }
}
