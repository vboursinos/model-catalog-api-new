package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FloatParameterRangeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FloatParameterRangeDTO.class);
        FloatParameterRangeDTO floatParameterRangeDTO1 = new FloatParameterRangeDTO();
        floatParameterRangeDTO1.setId(1L);
        FloatParameterRangeDTO floatParameterRangeDTO2 = new FloatParameterRangeDTO();
        assertThat(floatParameterRangeDTO1).isNotEqualTo(floatParameterRangeDTO2);
        floatParameterRangeDTO2.setId(floatParameterRangeDTO1.getId());
        assertThat(floatParameterRangeDTO1).isEqualTo(floatParameterRangeDTO2);
        floatParameterRangeDTO2.setId(2L);
        assertThat(floatParameterRangeDTO1).isNotEqualTo(floatParameterRangeDTO2);
        floatParameterRangeDTO1.setId(null);
        assertThat(floatParameterRangeDTO1).isNotEqualTo(floatParameterRangeDTO2);
    }
}
