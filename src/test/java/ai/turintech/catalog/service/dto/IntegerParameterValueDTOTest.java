package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegerParameterValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegerParameterValueDTO.class);
        IntegerParameterValueDTO integerParameterValueDTO1 = new IntegerParameterValueDTO();
        integerParameterValueDTO1.setId(1L);
        IntegerParameterValueDTO integerParameterValueDTO2 = new IntegerParameterValueDTO();
        assertThat(integerParameterValueDTO1).isNotEqualTo(integerParameterValueDTO2);
        integerParameterValueDTO2.setId(integerParameterValueDTO1.getId());
        assertThat(integerParameterValueDTO1).isEqualTo(integerParameterValueDTO2);
        integerParameterValueDTO2.setId(2L);
        assertThat(integerParameterValueDTO1).isNotEqualTo(integerParameterValueDTO2);
        integerParameterValueDTO1.setId(null);
        assertThat(integerParameterValueDTO1).isNotEqualTo(integerParameterValueDTO2);
    }
}
