package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelDTO.class);
        ModelDTO modelDTO1 = new ModelDTO();
        modelDTO1.setId(UUID.randomUUID());
        ModelDTO modelDTO2 = new ModelDTO();
        assertThat(modelDTO1).isNotEqualTo(modelDTO2);
        modelDTO2.setId(modelDTO1.getId());
        assertThat(modelDTO1).isEqualTo(modelDTO2);
        modelDTO2.setId(UUID.randomUUID());
        assertThat(modelDTO1).isNotEqualTo(modelDTO2);
        modelDTO1.setId(null);
        assertThat(modelDTO1).isNotEqualTo(modelDTO2);
    }
}
