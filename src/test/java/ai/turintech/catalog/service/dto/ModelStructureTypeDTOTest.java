package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelStructureTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelStructureTypeDTO.class);
        ModelStructureTypeDTO modelStructureTypeDTO1 = new ModelStructureTypeDTO();
        modelStructureTypeDTO1.setId(UUID.randomUUID());
        ModelStructureTypeDTO modelStructureTypeDTO2 = new ModelStructureTypeDTO();
        assertThat(modelStructureTypeDTO1).isNotEqualTo(modelStructureTypeDTO2);
        modelStructureTypeDTO2.setId(modelStructureTypeDTO1.getId());
        assertThat(modelStructureTypeDTO1).isEqualTo(modelStructureTypeDTO2);
        modelStructureTypeDTO2.setId(UUID.randomUUID());
        assertThat(modelStructureTypeDTO1).isNotEqualTo(modelStructureTypeDTO2);
        modelStructureTypeDTO1.setId(null);
        assertThat(modelStructureTypeDTO1).isNotEqualTo(modelStructureTypeDTO2);
    }
}
