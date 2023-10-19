package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterDistributionTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterDistributionTypeDTO.class);
        ParameterDistributionTypeDTO parameterDistributionTypeDTO1 = new ParameterDistributionTypeDTO();
        parameterDistributionTypeDTO1.setId(UUID.randomUUID());
        ParameterDistributionTypeDTO parameterDistributionTypeDTO2 = new ParameterDistributionTypeDTO();
        assertThat(parameterDistributionTypeDTO1).isNotEqualTo(parameterDistributionTypeDTO2);
        parameterDistributionTypeDTO2.setId(parameterDistributionTypeDTO1.getId());
        assertThat(parameterDistributionTypeDTO1).isEqualTo(parameterDistributionTypeDTO2);
        parameterDistributionTypeDTO2.setId(UUID.randomUUID());
        assertThat(parameterDistributionTypeDTO1).isNotEqualTo(parameterDistributionTypeDTO2);
        parameterDistributionTypeDTO1.setId(null);
        assertThat(parameterDistributionTypeDTO1).isNotEqualTo(parameterDistributionTypeDTO2);
    }
}
