package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoricalParameterValueDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameterValueDTO.class);
        CategoricalParameterValueDTO categoricalParameterValueDTO1 = new CategoricalParameterValueDTO();
        categoricalParameterValueDTO1.setId(1L);
        CategoricalParameterValueDTO categoricalParameterValueDTO2 = new CategoricalParameterValueDTO();
        assertThat(categoricalParameterValueDTO1).isNotEqualTo(categoricalParameterValueDTO2);
        categoricalParameterValueDTO2.setId(categoricalParameterValueDTO1.getId());
        assertThat(categoricalParameterValueDTO1).isEqualTo(categoricalParameterValueDTO2);
        categoricalParameterValueDTO2.setId(2L);
        assertThat(categoricalParameterValueDTO1).isNotEqualTo(categoricalParameterValueDTO2);
        categoricalParameterValueDTO1.setId(null);
        assertThat(categoricalParameterValueDTO1).isNotEqualTo(categoricalParameterValueDTO2);
    }
}
