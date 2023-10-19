package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterType.class);
        ParameterType parameterType1 = new ParameterType();
        parameterType1.setId(UUID.randomUUID());
        ParameterType parameterType2 = new ParameterType();
        parameterType2.setId(parameterType1.getId());
        assertThat(parameterType1).isEqualTo(parameterType2);
        parameterType2.setId(UUID.randomUUID());
        assertThat(parameterType1).isNotEqualTo(parameterType2);
        parameterType1.setId(null);
        assertThat(parameterType1).isNotEqualTo(parameterType2);
    }
}
