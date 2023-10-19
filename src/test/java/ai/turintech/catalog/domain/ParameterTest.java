package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parameter.class);
        Parameter parameter1 = new Parameter();
        parameter1.setId(UUID.randomUUID());
        Parameter parameter2 = new Parameter();
        parameter2.setId(parameter1.getId());
        assertThat(parameter1).isEqualTo(parameter2);
        parameter2.setId(UUID.randomUUID());
        assertThat(parameter1).isNotEqualTo(parameter2);
        parameter1.setId(null);
        assertThat(parameter1).isNotEqualTo(parameter2);
    }
}
