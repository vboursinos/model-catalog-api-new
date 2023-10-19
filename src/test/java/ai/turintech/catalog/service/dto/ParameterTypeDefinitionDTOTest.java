package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterTypeDefinitionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterTypeDefinitionDTO.class);
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO1 = new ParameterTypeDefinitionDTO();
        parameterTypeDefinitionDTO1.setId(UUID.randomUUID());
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO2 = new ParameterTypeDefinitionDTO();
        assertThat(parameterTypeDefinitionDTO1).isNotEqualTo(parameterTypeDefinitionDTO2);
        parameterTypeDefinitionDTO2.setId(parameterTypeDefinitionDTO1.getId());
        assertThat(parameterTypeDefinitionDTO1).isEqualTo(parameterTypeDefinitionDTO2);
        parameterTypeDefinitionDTO2.setId(UUID.randomUUID());
        assertThat(parameterTypeDefinitionDTO1).isNotEqualTo(parameterTypeDefinitionDTO2);
        parameterTypeDefinitionDTO1.setId(null);
        assertThat(parameterTypeDefinitionDTO1).isNotEqualTo(parameterTypeDefinitionDTO2);
    }
}
