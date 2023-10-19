package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterDTO.class);
        ParameterDTO parameterDTO1 = new ParameterDTO();
        parameterDTO1.setId(UUID.randomUUID());
        ParameterDTO parameterDTO2 = new ParameterDTO();
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
        parameterDTO2.setId(parameterDTO1.getId());
        assertThat(parameterDTO1).isEqualTo(parameterDTO2);
        parameterDTO2.setId(UUID.randomUUID());
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
        parameterDTO1.setId(null);
        assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
    }
}
