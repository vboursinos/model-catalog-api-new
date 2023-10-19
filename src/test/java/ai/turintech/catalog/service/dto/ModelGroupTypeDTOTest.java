package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelGroupTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelGroupTypeDTO.class);
        ModelGroupTypeDTO modelGroupTypeDTO1 = new ModelGroupTypeDTO();
        modelGroupTypeDTO1.setId(UUID.randomUUID());
        ModelGroupTypeDTO modelGroupTypeDTO2 = new ModelGroupTypeDTO();
        assertThat(modelGroupTypeDTO1).isNotEqualTo(modelGroupTypeDTO2);
        modelGroupTypeDTO2.setId(modelGroupTypeDTO1.getId());
        assertThat(modelGroupTypeDTO1).isEqualTo(modelGroupTypeDTO2);
        modelGroupTypeDTO2.setId(UUID.randomUUID());
        assertThat(modelGroupTypeDTO1).isNotEqualTo(modelGroupTypeDTO2);
        modelGroupTypeDTO1.setId(null);
        assertThat(modelGroupTypeDTO1).isNotEqualTo(modelGroupTypeDTO2);
    }
}
