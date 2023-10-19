package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterDistributionTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterDistributionType.class);
        ParameterDistributionType parameterDistributionType1 = new ParameterDistributionType();
        parameterDistributionType1.setId(UUID.randomUUID());
        ParameterDistributionType parameterDistributionType2 = new ParameterDistributionType();
        parameterDistributionType2.setId(parameterDistributionType1.getId());
        assertThat(parameterDistributionType1).isEqualTo(parameterDistributionType2);
        parameterDistributionType2.setId(UUID.randomUUID());
        assertThat(parameterDistributionType1).isNotEqualTo(parameterDistributionType2);
        parameterDistributionType1.setId(null);
        assertThat(parameterDistributionType1).isNotEqualTo(parameterDistributionType2);
    }
}
