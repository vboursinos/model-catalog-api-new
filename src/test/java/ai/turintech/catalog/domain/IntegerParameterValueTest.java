package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class IntegerParameterValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegerParameterValue.class);
        IntegerParameterValue integerParameterValue1 = new IntegerParameterValue();
        integerParameterValue1.setId(UUID.fromString("0fc005ec-7b05-44a1-877b-adf85318fc8f"));
        IntegerParameterValue integerParameterValue2 = new IntegerParameterValue();
        integerParameterValue2.setId(integerParameterValue1.getId());
        assertThat(integerParameterValue1).isEqualTo(integerParameterValue2);
        integerParameterValue2.setId(UUID.fromString("63d2c4d5-5254-45cf-ae6f-188f66c43f2b"));
        assertThat(integerParameterValue1).isNotEqualTo(integerParameterValue2);
        integerParameterValue1.setId(null);
        assertThat(integerParameterValue1).isNotEqualTo(integerParameterValue2);
    }
}
