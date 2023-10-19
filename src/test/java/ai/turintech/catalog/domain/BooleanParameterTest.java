package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooleanParameterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooleanParameter.class);
        BooleanParameter booleanParameter1 = new BooleanParameter();
        booleanParameter1.setId(1L);
        BooleanParameter booleanParameter2 = new BooleanParameter();
        booleanParameter2.setId(booleanParameter1.getId());
        assertThat(booleanParameter1).isEqualTo(booleanParameter2);
        booleanParameter2.setId(2L);
        assertThat(booleanParameter1).isNotEqualTo(booleanParameter2);
        booleanParameter1.setId(null);
        assertThat(booleanParameter1).isNotEqualTo(booleanParameter2);
    }
}
