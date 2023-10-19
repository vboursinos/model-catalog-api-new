package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelTypeDTO.class);
        ModelTypeDTO modelTypeDTO1 = new ModelTypeDTO();
        modelTypeDTO1.setId(UUID.randomUUID());
        ModelTypeDTO modelTypeDTO2 = new ModelTypeDTO();
        assertThat(modelTypeDTO1).isNotEqualTo(modelTypeDTO2);
        modelTypeDTO2.setId(modelTypeDTO1.getId());
        assertThat(modelTypeDTO1).isEqualTo(modelTypeDTO2);
        modelTypeDTO2.setId(UUID.randomUUID());
        assertThat(modelTypeDTO1).isNotEqualTo(modelTypeDTO2);
        modelTypeDTO1.setId(null);
        assertThat(modelTypeDTO1).isNotEqualTo(modelTypeDTO2);
    }
}
