package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloatParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloatParameterDTO.class);
        FloatParameterDTO floatParameterDTO1 = new FloatParameterDTO();
        floatParameterDTO1.setId(1L);
        FloatParameterDTO floatParameterDTO2 = new FloatParameterDTO();
        assertThat(floatParameterDTO1).isNotEqualTo(floatParameterDTO2);
        floatParameterDTO2.setId(floatParameterDTO1.getId());
        assertThat(floatParameterDTO1).isEqualTo(floatParameterDTO2);
        floatParameterDTO2.setId(2L);
        assertThat(floatParameterDTO1).isNotEqualTo(floatParameterDTO2);
        floatParameterDTO1.setId(null);
        assertThat(floatParameterDTO1).isNotEqualTo(floatParameterDTO2);
    }
}
