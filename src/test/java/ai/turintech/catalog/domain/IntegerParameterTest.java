package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegerParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegerParameter.class);
        IntegerParameter integerParameter1 = new IntegerParameter();
        integerParameter1.setId(1L);
        IntegerParameter integerParameter2 = new IntegerParameter();
        integerParameter2.setId(integerParameter1.getId());
        assertThat(integerParameter1).isEqualTo(integerParameter2);
        integerParameter2.setId(2L);
        assertThat(integerParameter1).isNotEqualTo(integerParameter2);
        integerParameter1.setId(null);
        assertThat(integerParameter1).isNotEqualTo(integerParameter2);
    }
}
