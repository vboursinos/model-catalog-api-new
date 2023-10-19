package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MlTaskTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MlTaskTypeDTO.class);
        MlTaskTypeDTO mlTaskTypeDTO1 = new MlTaskTypeDTO();
        mlTaskTypeDTO1.setId(UUID.randomUUID());
        MlTaskTypeDTO mlTaskTypeDTO2 = new MlTaskTypeDTO();
        assertThat(mlTaskTypeDTO1).isNotEqualTo(mlTaskTypeDTO2);
        mlTaskTypeDTO2.setId(mlTaskTypeDTO1.getId());
        assertThat(mlTaskTypeDTO1).isEqualTo(mlTaskTypeDTO2);
        mlTaskTypeDTO2.setId(UUID.randomUUID());
        assertThat(mlTaskTypeDTO1).isNotEqualTo(mlTaskTypeDTO2);
        mlTaskTypeDTO1.setId(null);
        assertThat(mlTaskTypeDTO1).isNotEqualTo(mlTaskTypeDTO2);
    }
}
