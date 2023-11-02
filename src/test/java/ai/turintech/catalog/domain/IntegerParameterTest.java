package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class IntegerParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegerParameter.class);
        IntegerParameter integerParameter1 = new IntegerParameter();
        integerParameter1.setParameterTypeDefinitionId(UUID.fromString("e3a11245-4397-4b99-9ca5-fc40a4b30ce6"));
        IntegerParameter integerParameter2 = new IntegerParameter();
        integerParameter2.setParameterTypeDefinitionId(integerParameter1.getParameterTypeDefinitionId());
        assertThat(integerParameter1).isEqualTo(integerParameter2);
        integerParameter2.setParameterTypeDefinitionId(UUID.fromString("2c37788f-87a0-4f99-96bd-7bcdd1dc57a0"));
        assertThat(integerParameter1).isNotEqualTo(integerParameter2);
        integerParameter1.setParameterTypeDefinitionId(null);
        assertThat(integerParameter1).isNotEqualTo(integerParameter2);
    }
}
