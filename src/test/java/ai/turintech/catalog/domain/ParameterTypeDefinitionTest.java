package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ParameterTypeDefinitionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParameterTypeDefinition.class);
        ParameterTypeDefinition parameterTypeDefinition1 = new ParameterTypeDefinition();
        parameterTypeDefinition1.setId(UUID.randomUUID());
        ParameterTypeDefinition parameterTypeDefinition2 = new ParameterTypeDefinition();
        parameterTypeDefinition2.setId(parameterTypeDefinition1.getId());
        assertThat(parameterTypeDefinition1).isEqualTo(parameterTypeDefinition2);
        parameterTypeDefinition2.setId(UUID.randomUUID());
        assertThat(parameterTypeDefinition1).isNotEqualTo(parameterTypeDefinition2);
        parameterTypeDefinition1.setId(null);
        assertThat(parameterTypeDefinition1).isNotEqualTo(parameterTypeDefinition2);
    }
}
