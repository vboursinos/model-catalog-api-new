package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BooleanParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BooleanParameterDTO.class);
        BooleanParameterDTO booleanParameterDTO1 = new BooleanParameterDTO();
        booleanParameterDTO1.setId(1L);
        BooleanParameterDTO booleanParameterDTO2 = new BooleanParameterDTO();
        assertThat(booleanParameterDTO1).isNotEqualTo(booleanParameterDTO2);
        booleanParameterDTO2.setId(booleanParameterDTO1.getId());
        assertThat(booleanParameterDTO1).isEqualTo(booleanParameterDTO2);
        booleanParameterDTO2.setId(2L);
        assertThat(booleanParameterDTO1).isNotEqualTo(booleanParameterDTO2);
        booleanParameterDTO1.setId(null);
        assertThat(booleanParameterDTO1).isNotEqualTo(booleanParameterDTO2);
    }
}
