package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterTypeDTO.class);
        ParameterTypeDTO parameterTypeDTO1 = new ParameterTypeDTO();
        parameterTypeDTO1.setId(UUID.randomUUID());
        ParameterTypeDTO parameterTypeDTO2 = new ParameterTypeDTO();
        assertThat(parameterTypeDTO1).isNotEqualTo(parameterTypeDTO2);
        parameterTypeDTO2.setId(parameterTypeDTO1.getId());
        assertThat(parameterTypeDTO1).isEqualTo(parameterTypeDTO2);
        parameterTypeDTO2.setId(UUID.randomUUID());
        assertThat(parameterTypeDTO1).isNotEqualTo(parameterTypeDTO2);
        parameterTypeDTO1.setId(null);
        assertThat(parameterTypeDTO1).isNotEqualTo(parameterTypeDTO2);
    }
}
