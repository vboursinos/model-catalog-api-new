package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegerParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegerParameterDTO.class);
        IntegerParameterDTO integerParameterDTO1 = new IntegerParameterDTO();
        integerParameterDTO1.setId(1L);
        IntegerParameterDTO integerParameterDTO2 = new IntegerParameterDTO();
        assertThat(integerParameterDTO1).isNotEqualTo(integerParameterDTO2);
        integerParameterDTO2.setId(integerParameterDTO1.getId());
        assertThat(integerParameterDTO1).isEqualTo(integerParameterDTO2);
        integerParameterDTO2.setId(2L);
        assertThat(integerParameterDTO1).isNotEqualTo(integerParameterDTO2);
        integerParameterDTO1.setId(null);
        assertThat(integerParameterDTO1).isNotEqualTo(integerParameterDTO2);
    }
}
